package com.doan.AppTuyenDung.ModelFirebase;

import lombok.Data;

import java.util.Date;

@Data
public class MessageResponse {
    String messageID;
    String sender;
    String subject;
    String receiver;
    String message;
    String attachedUrl;
    String status;
    Date readAt;
    Date sendTime;
}
