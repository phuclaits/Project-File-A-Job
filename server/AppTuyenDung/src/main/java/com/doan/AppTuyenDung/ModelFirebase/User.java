package com.doan.AppTuyenDung.ModelFirebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    String phoneNumber;
    String role;
    List<String> registrationToken;
}
