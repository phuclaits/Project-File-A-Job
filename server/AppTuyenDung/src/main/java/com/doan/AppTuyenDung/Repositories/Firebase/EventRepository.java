package com.doan.AppTuyenDung.Repositories.Firebase;

import com.doan.AppTuyenDung.ModelFirebase.Event;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class EventRepository {

    private final String COLLECTION_NAME = "Events";
    @Autowired
    private Firestore firestore;

    public ApiFuture<DocumentReference> addEvent(Event event){
        return firestore.collection(COLLECTION_NAME).add(event);
    }
}
