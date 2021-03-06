package com.example.contactapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Contact implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String avatarUri;

    @ColumnInfo
    private String name;

    @ColumnInfo
    private String mobile;

    @ColumnInfo
    private String email;

    public Contact() {
    }

    public Contact(String avatarUri, String name, String mobile, String email) {
        this.avatarUri = avatarUri;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
    }

    public Contact(int id, String avatarUri, String name, String mobile, String email) {
        this.id = id;
        this.avatarUri = avatarUri;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
