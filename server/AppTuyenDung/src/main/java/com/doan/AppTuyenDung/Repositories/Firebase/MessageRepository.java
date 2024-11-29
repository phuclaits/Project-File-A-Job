package com.doan.AppTuyenDung.Repositories.Firebase;

import com.doan.AppTuyenDung.ModelFirebase.Message;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@NoArgsConstructor
public class MessageRepository {
    private final String COLLECTION_NAME = "Messages";
    @Autowired
    private Firestore firestore;
    public ApiFuture<DocumentReference> addMessage(Message message){
        return firestore.collection(COLLECTION_NAME).add(message);
    }

    public ApiFuture<WriteResult> addMessageWithCustomId(Message message,String messageId){
        return firestore.collection(COLLECTION_NAME).document(messageId).set(message);
    }
    public ApiFuture<WriteResult> saveMessage(Message message, String documentId) {
        return firestore.collection(COLLECTION_NAME).document(documentId).set(message);
    }

    public ApiFuture<DocumentSnapshot> getMessageById(String id) {
        return firestore.collection(COLLECTION_NAME).document(id).get();
    }

    public ApiFuture<WriteResult> deleteMessage(String id) {
        return firestore.collection(COLLECTION_NAME).document(id).delete();
    }
    public ApiFuture<QuerySnapshot> getMessageByReceiver(String receiverId) {
        return firestore.collection(COLLECTION_NAME).whereEqualTo("receiver", receiverId).get();
    }
    public ApiFuture<QuerySnapshot> getMessageBySender(String senderId) {
        return firestore.collection(COLLECTION_NAME).whereEqualTo("sender", senderId).get();
    }

    public ApiFuture<QuerySnapshot> countUnRead(String phoneNumber) {
        return firestore.collection(COLLECTION_NAME)
                .whereEqualTo("status", "sent")
                .whereEqualTo("receiver", phoneNumber)
                .get();
    }
    public ApiFuture<QuerySnapshot> getAllMessages() {
        return firestore.collection(COLLECTION_NAME).get();
    }
    public void updateReadAll(String receiver) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("status", "sent")
                .whereEqualTo("receiver", receiver)
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        WriteBatch batch = firestore.batch();

        for (QueryDocumentSnapshot document : documents) {
            DocumentReference docRef = document.getReference();
            batch.update(docRef, "status", "read", "readAt", FieldValue.serverTimestamp());
        }

        ApiFuture<List<WriteResult>> futureBatch = batch.commit();
        futureBatch.get();
    }
    public void deleteAllMessagesByReceiver(String receiver) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("receiver", receiver)
                .get();
        
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        WriteBatch batch = firestore.batch();

        for (QueryDocumentSnapshot document : documents) {
            batch.delete(document.getReference());
        }

        ApiFuture<List<WriteResult>> futureBatch = batch.commit();
        futureBatch.get();
    }
}
