package com.doan.AppTuyenDung.Services.Notification;

import com.doan.AppTuyenDung.ModelFirebase.Message;
import com.doan.AppTuyenDung.ModelFirebase.MessageResponse;
import com.doan.AppTuyenDung.Repositories.Firebase.MessageRepository;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    @Autowired
    private MessageRepository messageRepository;


    public String createMessageAndGetId(Message message) throws ExecutionException, InterruptedException {
        DocumentReference doc = messageRepository.addMessage(message).get();
        return doc.getId();
    }

    public String createMessage(Message message) {
            return messageRepository.addMessage(message).toString();
    }

    public String createMessageWithCustomId(Message message,String Id) {
        return messageRepository.addMessage(message).toString();
    }

    public Message getMessageById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = messageRepository.getMessageById(id).get();
        return document.exists() ? document.toObject(Message.class) : null;
    }

    public String deleteMessage(String id) throws ExecutionException, InterruptedException {
        return messageRepository.deleteMessage(id).get().getUpdateTime().toString();
    }

    public List<MessageResponse> getAllMessage() throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documents = messageRepository.getAllMessages().get().getDocuments();
        List<MessageResponse> messages = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            MessageResponse response = mapToMessageResponse(document);
            messages.add(response);
        }
        return messages;
    }
    public List<MessageResponse> getMessageByReceiver(String id) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documents = messageRepository.getMessageByReceiver(id).get().getDocuments();
        List<MessageResponse> messages = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            MessageResponse response = mapToMessageResponse(document);
            messages.add(response);
        }
        messages.sort(Comparator.comparing((MessageResponse msg) -> !"sent".equals(msg.getStatus()))
                .thenComparing(msg -> msg.getSendTime(), Comparator.reverseOrder()));
        return messages;
    }
    public int counUnReadMess(String phoneNumber) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documents = messageRepository.countUnRead(phoneNumber).get().getDocuments();
        List<MessageResponse> messages = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            MessageResponse response = mapToMessageResponse(document);
            messages.add(response);
        }
        return messages.size();
    }
    public MessageResponse mapToMessageResponse(QueryDocumentSnapshot document) {
        MessageResponse response = new MessageResponse();
        Message message = document.toObject(Message.class);
        response.setMessageID(document.getId());
        response.setMessage(message.getMessage());
        response.setReceiver(message.getReceiver());
        response.setStatus(message.getStatus());
        response.setReadAt(message.getReadAt());
        response.setSubject(message.getSubject());
        response.setAttachedUrl(message.getAttachedUrl());
        response.setSender(message.getSender());
        response.setSendTime(message.getSendTime());
        return response;
    }
    public List<Message> getMessageBySender(String id) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documents = messageRepository.getMessageBySender(id).get().getDocuments();
        List<Message> messages = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            messages.add(document.toObject(Message.class));
        }
        return messages;
    }
    public String updateStatusMessage(String id) throws ExecutionException, InterruptedException {
        Message messageRS = getMessageById(id);
        if(Objects.equals(messageRS.getStatus(), "read")){
            return null;
        }
        messageRS.setStatus("read");
        messageRS.setReadAt(new Date());
        return messageRepository.saveMessage(messageRS,id).get().getUpdateTime().toString();
    }
    public String updateStatusAllMessage(String receiver) throws ExecutionException, InterruptedException {
        messageRepository.updateReadAll(receiver);
        return "ok";
    }
    public String deleteAllMessageReceiver(String receiver) throws ExecutionException, InterruptedException {
        messageRepository.deleteAllMessagesByReceiver(receiver);
        return "ok";
    }
    public void deleteMessExp() throws ExecutionException, InterruptedException {
        List<MessageResponse> lstMess = getAllMessage();
        Date now = new Date();
        LocalDate localDateNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int cnt = 0;
        for(MessageResponse m : lstMess) {
            if(m.getReadAt()!=null) {
                LocalDate localDateReatAt = m.getReadAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                long daysBetween = localDateReatAt.until(localDateNow, ChronoUnit.DAYS);
                if(daysBetween>=7) {
                    deleteMessage(m.getMessageID());
                    cnt++;
                }
            }
        }
        if(cnt > 0) {
            log.info("Check and delete expired messages");
        }
        else {
            log.info("Not expired message");
        }
    }
}
