package com.doan.AppTuyenDung.Services.Notification;

import com.doan.AppTuyenDung.ModelFirebase.User;
import com.doan.AppTuyenDung.Repositories.Firebase.UserFirebaseRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    @Autowired
    private UserFirebaseRepository userFirebaseRepository;

    public String createUser(User user) throws ExecutionException, InterruptedException {
        return userFirebaseRepository.saveUser(user).toString();
    }
    public String addTokenForUser(String phoneNumber, String token) throws ExecutionException, InterruptedException {
        String rs = "";
        if(getUserById(phoneNumber)!=null) {
            String addNewToken = addToken(phoneNumber,token);
            rs = "User exist, add new token " + addNewToken;
        }
        else {
        	rs = "User not exist";
        }

        return rs;
    }
    private String addUser(User userRq) throws ExecutionException, InterruptedException {
            List<String> tokens = new ArrayList<>();
            User user = new User();
            user.setPhoneNumber(userRq.getPhoneNumber());
            user.setRegistrationToken(tokens);
            user.setRole(userRq.getRole());
            return "Add new user: " + userFirebaseRepository.saveUser(user).get().getUpdateTime().toString();
    }

    public User getUserById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = userFirebaseRepository.getUserById(id).get();
        return document.exists() ? document.toObject(User.class) : null;
    }

    public String deleteUser(String id) throws ExecutionException, InterruptedException {
        return userFirebaseRepository.deleteUser(id).get().getUpdateTime().toString();
    }

    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documents = userFirebaseRepository.getAllUsers().get().getDocuments();
        List<User> users = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            users.add(document.toObject(User.class));
        }
        return users;
    }
    public String deleteToken(String userId, String token) throws ExecutionException, InterruptedException {
        String rs = "";
        User u = getUserById(userId);
        if(u != null ) {
            List<String> lstToken = u.getRegistrationToken();
            if (u.getRegistrationToken().contains(token)) {
                rs = "Token exist, delete token: " + userFirebaseRepository.removeElementFromArray(userId,"registrationToken",token).get().getUpdateTime().toString();
            }
            else {
                rs = "Token " +token+" not exist";
            }
        }
        else {
            rs ="User not exist";
        }
        return rs;
    }
    public String addToken(String phoneNumber, String newToken) throws ExecutionException, InterruptedException {
        return userFirebaseRepository.addElementToArray(phoneNumber,"registrationToken", newToken).get().getUpdateTime().toString();
    }
}
