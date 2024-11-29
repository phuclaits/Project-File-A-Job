package com.doan.AppTuyenDung.ModelFirebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    String sender;
    String subject;
    String receiver;
    String message;
    String attachedUrl;
    String status;
    Date readAt;
    Date sendTime;
}
