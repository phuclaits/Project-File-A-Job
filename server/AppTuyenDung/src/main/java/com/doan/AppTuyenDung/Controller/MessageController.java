package com.doan.AppTuyenDung.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.doan.AppTuyenDung.ModelFirebase.Message;
import com.doan.AppTuyenDung.ModelFirebase.MessageResponse;
import com.doan.AppTuyenDung.ModelFirebase.UnReadResponse;
import com.doan.AppTuyenDung.Services.Notification.MessageService;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/add")
    public ResponseEntity<String> addMessage(@RequestBody Message message) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(messageService.createMessage(message).toString(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable String id) throws ExecutionException, InterruptedException {
        Message message = messageService.getMessageById(id);
        return message != null ? new ResponseEntity<>(message, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable String id) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(messageService.deleteMessage(id), HttpStatus.OK);
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<Message>> getMessageBySender(@PathVariable String senderId) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(messageService.getMessageBySender(senderId), HttpStatus.OK);
    }
    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<MessageResponse>> getMessageByReceiver(@PathVariable String receiverId) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(messageService.getMessageByReceiver(receiverId), HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<MessageResponse>> getAllMessages() throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(messageService.getAllMessage(), HttpStatus.OK);
    }
    @PutMapping("/updatestatus/{messagerId}")
    public ResponseEntity<String> updataStatus(@PathVariable String messagerId) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(messageService.updateStatusMessage(messagerId), HttpStatus.OK);
    }
    @GetMapping("/un-read/{phoneNumber}")
    public ResponseEntity<UnReadResponse> counUnReadMess(@PathVariable String phoneNumber) throws ExecutionException, InterruptedException {
        UnReadResponse rp = new UnReadResponse();
        rp.setCount(messageService.counUnReadMess(phoneNumber));
        return new ResponseEntity<>(rp, HttpStatus.OK);
    }
}
