package com.doan.AppTuyenDung.ModelFirebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.signature.qual.Identifier;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    String sourceToken;
    int retryCount;
    String status;
    Map<String,String> data;
    Date sendTime;

}
