package com.pdrozz.instagramclone.model;

public class FeedDataModel {

    private String date,idauthor,idpost;
    private long data;

    public FeedDataModel(){}

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdauthor() {
        return idauthor;
    }

    public void setIdauthor(String idauthor) {
        this.idauthor = idauthor;
    }

    public String getIdpost() {
        return idpost;
    }

    public void setIdpost(String idpost) {
        this.idpost = idpost;
    }
}
