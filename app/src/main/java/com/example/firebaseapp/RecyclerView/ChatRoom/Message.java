package com.example.firebaseapp.RecyclerView.ChatRoom;

public class Message {
    private String senderId,message;

    Message()
    {

    }
    public Message(String senderId,  String message) {
        this.senderId = senderId;
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
