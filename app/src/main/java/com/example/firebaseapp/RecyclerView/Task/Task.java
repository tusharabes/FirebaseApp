package com.example.firebaseapp.RecyclerView.Task;

public class Task {

    String title,description,publisher,time;

    public  Task()
    {

    }
    public Task(String title, String description, String publisher, String time) {
        this.title = title;
        this.description = description;
        this.publisher = publisher;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
