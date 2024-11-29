package com.doan.AppTuyenDung.ModelFirebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationUpdatePutRequest {
    String userId;
    String messageId;
}
