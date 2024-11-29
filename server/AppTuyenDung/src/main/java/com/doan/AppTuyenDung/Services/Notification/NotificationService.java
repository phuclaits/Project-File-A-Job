package com.doan.AppTuyenDung.Services.Notification;

import com.doan.AppTuyenDung.ModelFirebase.NotiResponse;
import com.doan.AppTuyenDung.ModelFirebase.Notice;
import com.doan.AppTuyenDung.ModelFirebase.User;
import com.doan.AppTuyenDung.ModelFirebase.Event;
import com.doan.AppTuyenDung.ModelFirebase.Message;
import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    @Autowired
    private EventService eventService;
    @Autowired
    private FirebaseMessaging firebaseMessaging;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    
    public NotiResponse sendNotificationUser(Notice notice) throws ExecutionException, InterruptedException {
        Message messageSave = new Message();
        Map<String,String> dataSave = new HashMap<>();
        List<List<String>> listOfTokenByUser = new ArrayList<List<String>>();

        List<String> availableUser = new ArrayList<String>();

        for (int i = 0; i < notice.getUserId().size(); i++) {
            String userID = notice.getUserId().get(i);
            User user = userService.getUserById(userID);
            if(user!= null){
                listOfTokenByUser.add(user.getRegistrationToken());
                availableUser.add(userID);
            }
        }

        Map<String,String> NotiData = new HashMap<String,String>();

        NotiData.put("sender", notice.getData().get("sender"));
        NotiData.put("subject",notice.getSubject());
        NotiData.put("message",notice.getData().get("message"));
        NotiData.put("type","new");
        NotiData.put("attachedUrl", notice.getData().get("attachedUrl"));
        NotiData.put("status","sent");
        NotiData.put("sendTime",new Date().toString());

        dataSave = notice.getData();

        List<String> messageIds = new ArrayList<String>();
        for (int i = 0; i < notice.getUserId().size();i++) {
            messageSave.setSendTime(new Date());
            messageSave.setSubject(notice.getSubject());
            messageSave.setMessage(dataSave.get("message"));
            messageSave.setAttachedUrl(dataSave.get("attachedUrl"));
            messageSave.setSender(dataSave.get("sender"));
            messageSave.setReceiver(notice.getUserId().get(i));
            messageSave.setStatus("sent");

            String message = messageService.createMessageAndGetId(messageSave);
            messageIds.add(message);

            log.info(message);
        }
        int countSuccess = 0;
        int countFailed = 0;
        BatchResponse batchResponse = null;
        for(int i = 0; i<listOfTokenByUser.size();i++){
            try{
                List<String> lstToken = listOfTokenByUser.get(i);
                Notification notification = Notification.builder()
                        .setTitle(notice.getSubject())
                        .setImage(notice.getImage())
                        .build();
                MulticastMessage message = MulticastMessage.builder()
                        .addAllTokens(lstToken)
                        .setNotification(notification)
                        .putAllData(NotiData)
                        .putData("messageID",messageIds.get(i))
                        .build();
                batchResponse=firebaseMessaging.sendEachForMulticast(message);
                countSuccess += batchResponse.getSuccessCount();
                countFailed+= batchResponse.getFailureCount();
                List<SendResponse> responses = batchResponse.getResponses();
                int count = retryOnFail(batchResponse, responses, lstToken,notification,NotiData, messageIds.get(i));
            } catch(FirebaseMessagingException error){
                log.info("Firebase error:: {}",error.getMessage());
            }

        }
        return new NotiResponse(countSuccess,countFailed);
    }

    public int retryOnFail(BatchResponse batchResponse,List<SendResponse> responses, List<String> lstToken, Notification notification, Map<String,String> NotiData, String messageId ) throws FirebaseMessagingException {
            List<String> failedTokens = new ArrayList<String>();
            int countSuccess = 0;
            for(int i = 0; i<batchResponse.getResponses().size();i++){
                if(!responses.get(i).isSuccessful()){
                    failedTokens.add(lstToken.get(i));
                } else {
                    Event event = new Event();
                    event.setRetryCount(1);
                    event.setSendTime(new Date());
                    event.setSourceToken(lstToken.get(i));
                    event.setStatus("Success");
                    Map<String,String> data = NotiData;
                    event.setData(data);
                    eventService.addEvent(event);
                }
            }

            int count = 1;
            BatchResponse batchResponseFail = null;
            List<SendResponse> responseFails = null;
            while(count<=3){
                if(failedTokens.size() ==0){
                    break;
                }

                MulticastMessage messageOfFail = MulticastMessage.builder()
                        .addAllTokens(failedTokens)
                        .setNotification(notification)
                        .putAllData(NotiData)
                        .putData("messageID",messageId)
                        .build();

                batchResponseFail=firebaseMessaging.sendEachForMulticast(messageOfFail);
                responseFails = batchResponseFail.getResponses();

                log.info("Fail count:{}",batchResponseFail.getFailureCount());

                List<String> oldFailedToken = new ArrayList<String>(failedTokens);
                failedTokens = new ArrayList<>();

                for(int i = 0; i<batchResponseFail.getResponses().size();i++){
                    if(!responseFails.get(i).isSuccessful()){
                        failedTokens.add(oldFailedToken.get(i));
                        countSuccess++;
                    } else {
                        Event event = new Event();
                        event.setRetryCount(count+1);
                        event.setSendTime(new Date());
                        event.setSourceToken(oldFailedToken.get(i));
                        event.setStatus("Success");
                        event.setData(NotiData);
                        eventService.addEvent(event);
                    }
                }

                if(batchResponseFail.getFailureCount()==0){
                    return countSuccess;
                }

                count++;
            }
            if(batchResponseFail!=null && batchResponseFail.getFailureCount()>0){
                log.info("Fail detected");
                for(int i = 0; i<batchResponseFail.getResponses().size();i++){
                    if(!responseFails.get(i).isSuccessful()){
                        Event event = new Event();
                        event.setRetryCount(count);
                        event.setSendTime(new Date());
                        event.setSourceToken(failedTokens.get(i));
                        event.setStatus("Failed");
                        event.setData(NotiData);
                        eventService.addEvent(event);
                    }
                }
            }
            return countSuccess;
    }

    public int retryOnFailRead(BatchResponse batchResponse,List<SendResponse> responses, List<String> lstToken, Map<String,String> NotiData, String messageId ) throws FirebaseMessagingException {
        List<String> failedTokens = new ArrayList<String>();
        int countSuccess = 0;
        for(int i = 0; i<batchResponse.getResponses().size();i++){
            if(!responses.get(i).isSuccessful()){
                failedTokens.add(lstToken.get(i));
                countSuccess++;
            } else {
                Event event = new Event();
                event.setRetryCount(1);
                event.setSendTime(new Date());
                event.setSourceToken(lstToken.get(i));
                event.setStatus("Success");
                Map<String,String> data = NotiData;
                event.setData(data);
                eventService.addEvent(event);
            }
        }

        int count = 1;
        BatchResponse batchResponseFail = null;
        List<SendResponse> responseFails = null;
        while(count<=3){
            if(failedTokens.size() ==0){
                break;
            }

            MulticastMessage messageOfFail = MulticastMessage.builder()
                    .addAllTokens(failedTokens)
                    .putAllData(NotiData)
                    .putData("messageID",messageId)
                    .build();

            batchResponseFail=firebaseMessaging.sendEachForMulticast(messageOfFail);
            responseFails = batchResponseFail.getResponses();

            log.info("Fail count:{}",batchResponseFail.getFailureCount());

            List<String> oldFailedToken = new ArrayList<String>(failedTokens);
            failedTokens = new ArrayList<>();

            for(int i = 0; i<batchResponseFail.getResponses().size();i++){
                if(!responseFails.get(i).isSuccessful()){
                    failedTokens.add(oldFailedToken.get(i));
                    countSuccess++;
                } else {
                    Event event = new Event();
                    event.setRetryCount(count+1);
                    event.setSendTime(new Date());
                    event.setSourceToken(oldFailedToken.get(i));
                    event.setStatus("Success");
                    event.setData(NotiData);
                    eventService.addEvent(event);
                }
            }

            if(batchResponseFail.getFailureCount()==0){
                return countSuccess;
            }

            count++;
        }
        if(batchResponseFail!=null && batchResponseFail.getFailureCount()>0){
            log.info("Fail detected");
            for(int i = 0; i<batchResponseFail.getResponses().size();i++){
                if(!responseFails.get(i).isSuccessful()){
                    Event event = new Event();
                    event.setRetryCount(count);
                    event.setSendTime(new Date());
                    event.setSourceToken(failedTokens.get(i));
                    event.setStatus("Failed");
                    event.setData(NotiData);
                    eventService.addEvent(event);
                }
            }
        }
        return countSuccess;
    }

    public NotiResponse sendReadNotification(String userId, String messageId) throws ExecutionException, InterruptedException, FirebaseMessagingException {
        log.info(userId,messageId);
        String isProcess = messageService.updateStatusMessage(messageId);
        if(isProcess == null){
            return null;
        }
        int successCount = 0;
        int failedCount = 0;
        List<String> lstTokens = userService.getUserById(userId).getRegistrationToken();

        Map<String,String> NotiData = new HashMap<String,String>();
        NotiData.put("type","read");

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(lstTokens)
                .putAllData(NotiData)
                .putData("messageID",messageId)
                .build();
        try{
            BatchResponse batchResponse = firebaseMessaging.sendEachForMulticast(message);
            List<SendResponse> responses = batchResponse.getResponses();

            retryOnFailRead(batchResponse,responses,lstTokens,NotiData,messageId);

        } catch(FirebaseMessagingException error){
            log.info("Firebase error:: {}",error.getMessage());
        }
        return new NotiResponse(successCount,failedCount);
    }
    public NotiResponse sendReadAllNotification(String userId) throws ExecutionException, InterruptedException, FirebaseMessagingException {
        String isProcess = messageService.updateStatusAllMessage(userId);
        if(isProcess == null){
            return null;
        }
        int successCount = 0;
        int failedCount = 0;
        List<String> lstTokens = userService.getUserById(userId).getRegistrationToken();

        Map<String,String> NotiData = new HashMap<String,String>();
        NotiData.put("type","readAll");

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(lstTokens)
                .putAllData(NotiData)
                .putData("status", "read all")
                .build();
        try{
            BatchResponse batchResponse = firebaseMessaging.sendEachForMulticast(message);
            List<SendResponse> responses = batchResponse.getResponses();

            //retryOnFailRead(batchResponse,responses,lstTokens,NotiData,messageId);

        } catch(FirebaseMessagingException error){
            log.info("Firebase error:: {}",error.getMessage());
        }
        return new NotiResponse(successCount,failedCount);
    }
}
