package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.example.contactapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Contact> contacts;
    private ContactsAdapter contactsAdapter;

    private AppDatabase appDatabase;
    private ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        appDatabase = AppDatabase.getInstance(this);
        contactDao = appDatabase.contactDao();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Contact a = new Contact("Huỳnh Thị Ái Linh", "0768796932", "huynhthiailinh2105@gmail.com");
                contactDao.insertAll(a);
                contacts = (ArrayList<Contact>) contactDao.getAllContacts();
            }
        });

//        contacts = new ArrayList<>();
//        contacts.add(new Contact("Huỳnh Thị Ái Linh", "0768796932", "huynhthiailinh2105@gmail.com"));
//        contacts.add(new Contact("Huỳnh Thị Khánh Linh",  "0768796932", "huynhthiailinh2105@gmail.com"));
//        contacts.add(new Contact("Trần Như Trí", "0768796932", "huynhthiailinh2105@gmail.com"));

        contactsAdapter = new ContactsAdapter(contacts);
        binding.rvContacts.setAdapter(contactsAdapter);
        binding.rvContacts.setLayoutManager(new LinearLayoutManager(this));
    }
}