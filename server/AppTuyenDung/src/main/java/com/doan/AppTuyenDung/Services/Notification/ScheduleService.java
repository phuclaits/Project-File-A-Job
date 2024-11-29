package com.doan.AppTuyenDung.Services.Notification;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@Component
public class ScheduleService {
//    @Autowired
//    MessageService messageService;
//    private static final Logger log = LoggerFactory.getLogger(ScheduleService.class);
//    @Scheduled(cron = "*/5 * * * * ?")
//    public void scheduler() throws ExecutionException, InterruptedException {
//        messageService.deleteMessExp();
//    }
}
