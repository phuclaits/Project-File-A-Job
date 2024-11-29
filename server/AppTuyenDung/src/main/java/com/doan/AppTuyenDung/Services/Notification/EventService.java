package com.doan.AppTuyenDung.Services.Notification;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doan.AppTuyenDung.ModelFirebase.Event;
import com.doan.AppTuyenDung.Repositories.Firebase.EventRepository;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public String addEvent(Event event){
        return eventRepository.addEvent(event).toString();
    }
}
