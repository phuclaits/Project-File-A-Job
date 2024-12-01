package com.doan.AppTuyenDung.Services;

import java.util.Optional;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doan.AppTuyenDung.DTO.GetAllUserAdmin.AccountDTO;
import com.doan.AppTuyenDung.DTO.Request.ChangePasswordRequest;
import com.doan.AppTuyenDung.Exception.AppException;
import com.doan.AppTuyenDung.Exception.ErrorCode;
import com.doan.AppTuyenDung.Repositories.AccountPagingAndSorting;
import com.doan.AppTuyenDung.Repositories.AccountRepository;
import com.doan.AppTuyenDung.Repositories.UserRepository;
import com.doan.AppTuyenDung.entity.Account;
import com.doan.AppTuyenDung.entity.User;
import java.util.stream.*;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository uRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    // public String changePassword(int idUser, ChangePasswordRequest changePass) {
    // Optional<User> uOptional = uRepository.findById(idUser);
    // User user = uOptional.get();
    // if(uOptional.get()==null) {
    // return "Không tìm thấy user";
    // }

    // String oldPassEncode = passwordEncoder.encode(changePass.oldPassword);

    // String newPassEncode = passwordEncoder.encode(changePass.newPassword);
    // Account account = accountRepository.findByUserId(idUser);
    // boolean isPasswordMatch = passwordEncoder.matches(changePass.oldPassword,
    // account.getPassword());
    // if (isPasswordMatch && !newPassEncode.isEmpty()) {
    // account.setPassword(newPassEncode);
    // Account accountSave = accountRepository.save(account);
    // }
    // else {
    // return "Password củ không đúng";
    // }
    // return "Thay đổi password thành công";
    // }

    public Map<String, Object> checkUserPhone(String userPhone) {
        Map<String, Object> response = new HashMap<>();

        if (userPhone == null || userPhone.isEmpty()) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }

        boolean accountOptional = accountRepository.existsByPhonenumber(userPhone);
        if (accountOptional == true) {
            response.put("result", true);
        } else {
            response.put("result", false);
        }

        return response;
    }
    public String forgetPassword(String phoneNumber) {
        Account account = accountRepository.findByPhonenumber(phoneNumber);
        if (account != null) {
            String randomPass = generateRandomPassword();
            account.setPassword(passwordEncoder.encode(randomPass));
            accountRepository.save(account);
            return randomPass;
        } else {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
    }
    private String generateRandomPassword() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
        
        String upperCaseLetters = randomChars("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 2);
        
        String lowerCaseLetters = randomChars("abcdefghijklmnopqrstuvwxyz", 2);
        
        String specialChar = randomChars("!@#$%^&*()-_=+<>?", 1);
        
        return datePart + upperCaseLetters + lowerCaseLetters + specialChar;
    }

    private String randomChars(String source, int length) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(source.length());
            result.append(source.charAt(index));
        }
        return result.toString();
    }
    public Map<String, Object> changePasswordByPhone(Integer id, String oldPassword, String newPassword) {
        Map<String, Object> response = new HashMap<>();

        Account account = accountRepository.findByUserId(id);
        if (account != null) {

            if (passwordEncoder.matches(oldPassword, account.getPassword())) {

                account.setPassword(passwordEncoder.encode(newPassword));
                accountRepository.save(account);

                response.put("errCode", 0);
                response.put("errMessage", "Mật khẩu đã được cập nhật thành công");
            } else {
                // Nếu không khớp, trả về lỗi
                response.put("errCode", 2);
                response.put("errMessage", "Mật khẩu cũ không đúng");
            }
        } else {
            // Nếu tài khoản không tồn tại
            response.put("errCode", 1);
            response.put("errMessage", "Số điện thoại không tồn tại");
        }

        return response;
    }

    public Map<String, Object> changePasswordForgotPassByPhone(String phoneNumber, String newPassword) {
        Map<String, Object> response = new HashMap<>();

        Account account = accountRepository.findByPhonenumber(phoneNumber);
        if (account != null) {
            account.setPassword(passwordEncoder.encode(newPassword));
            accountRepository.save(account);
            response.put("errCode", 0);
            response.put("errMessage", "Mật khẩu đã được cập nhật thành công");

        } else {
            // Nếu tài khoản không tồn tại
            response.put("errCode", 1);
            response.put("errMessage", "Số điện thoại không tồn tại");
        }

        return response;
    }

    // get list user => role ADMIN => /admin/list-user
    @Autowired
    private AccountPagingAndSorting accountPagingAndSorting;;

    public Page<AccountDTO> getAllUsers(String search, Pageable pageable) {

        return accountPagingAndSorting.findAllWithDetails(search, pageable);
    }

}
