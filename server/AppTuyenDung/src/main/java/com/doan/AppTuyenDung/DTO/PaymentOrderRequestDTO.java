package com.doan.AppTuyenDung.DTO;

import lombok.Data;

@Data
public class PaymentOrderRequestDTO {
    private String payerID;
    private String paymentId;
    private String token;
    private Integer packageId;
    private Integer amount;
    private Integer userId;
}
