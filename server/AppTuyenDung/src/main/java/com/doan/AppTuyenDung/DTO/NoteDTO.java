package com.doan.AppTuyenDung.DTO;

import java.util.Date;

public class NoteDTO {
    private String recorderName;
    private Integer userId;
    private String noteContent;
    private Date recordedAt;

    // Constructor
    public NoteDTO(String recorderName, Integer userId, String noteContent, Date recordedAt) {
        this.recorderName = recorderName;
        this.userId = userId;
        this.noteContent = noteContent;
        this.recordedAt = recordedAt;
    }

    // Getters and Setters
    public String getRecorderName() {
        return recorderName;
    }

    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public Date getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(Date recordedAt) {
        this.recordedAt = recordedAt;
    }
}
