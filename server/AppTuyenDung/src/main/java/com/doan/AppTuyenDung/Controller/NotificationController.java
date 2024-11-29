package com.doan.AppTuyenDung.Controller;


import com.doan.AppTuyenDung.ModelFirebase.NotiResponse;
import com.doan.AppTuyenDung.ModelFirebase.Notice;
import com.doan.AppTuyenDung.ModelFirebase.NotificationUpdatePutRequest;
import com.doan.AppTuyenDung.Services.Notification.NotificationService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    @Autowired
    private NotificationService notificationService;
    @PostMapping("/sendUser")
    public NotiResponse sendNotificationUser(@RequestBody Notice notice) throws ExecutionException, InterruptedException {
        return notificationService.sendNotificationUser(notice);
    }

    @PutMapping("/updateReadStatus")
    public NotiResponse updateReadStatus(@RequestBody NotificationUpdatePutRequest request) throws ExecutionException, InterruptedException, FirebaseMessagingException {
        return notificationService.sendReadNotification(request.getUserId(), request.getMessageId());
    }
    @PutMapping("/read-all/{userId}")
    public NotiResponse updateReadAllStatus(@PathVariable String userId) throws ExecutionException, InterruptedException, FirebaseMessagingException {
        return notificationService.sendReadAllNotification(userId);
    }
}
