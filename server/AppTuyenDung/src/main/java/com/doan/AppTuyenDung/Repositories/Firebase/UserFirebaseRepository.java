package com.doan.AppTuyenDung.Repositories.Firebase;

import com.doan.AppTuyenDung.ModelFirebase.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserFirebaseRepository {
    private final String COLLECTION_NAME = "User";
    @Autowired
    private  Firestore firestore;

    public ApiFuture<DocumentReference> UserRegister(User user){
        return firestore.collection(COLLECTION_NAME).add(user);
    }

    public String UserDelete(String phoneNumber){
        ApiFuture<WriteResult> result = firestore.collection(COLLECTION_NAME).document(phoneNumber).delete();
        return result.toString();
    }
    public ApiFuture<WriteResult> saveUser(User user) {
        return firestore.collection(COLLECTION_NAME).document(user.getPhoneNumber()).set(user);
    }

    public ApiFuture<DocumentSnapshot> getUserById(String phoneNumber) {
        return firestore.collection(COLLECTION_NAME).document(phoneNumber).get();
    }

    public ApiFuture<WriteResult> deleteUser(String phoneNumber) {
        return firestore.collection(COLLECTION_NAME).document(phoneNumber).delete();
    }

    public ApiFuture<QuerySnapshot> getAllUsers() {
        return firestore.collection(COLLECTION_NAME).get();
    }

    public ApiFuture<WriteResult> addElementToArray(String phoneNumber, String arrayField, String newElement) {
        return firestore.collection(COLLECTION_NAME)
                .document(phoneNumber)
                .update(arrayField, FieldValue.arrayUnion(newElement));
    }

    public ApiFuture<WriteResult> removeElementFromArray(String phoneNumber, String arrayField, String elementToRemove) {
        return firestore.collection(COLLECTION_NAME)
                .document(phoneNumber)
                .update(arrayField, FieldValue.arrayRemove(elementToRemove));
    }

}
