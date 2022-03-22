package com.example.contactapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM Contact")
    List<Contact> getAllContacts();

    @Insert
    void insertAll(Contact... contact);

    @Update
    void updateContact(Contact contact);
}
