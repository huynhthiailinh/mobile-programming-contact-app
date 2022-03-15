package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.example.contactapp.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;

    private ActivityMainBinding binding;
    private List<Contact> contacts;
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
//                Contact a = new Contact("Huỳnh Thị Ái Linh", "0768796932", "huynhthiailinh2105@gmail.com");
//                contactDao.insertAll(a);
            }
        });

        contacts = contactDao.getAllContacts();
        contactsAdapter = new ContactsAdapter(contacts);
        binding.rvContacts.setAdapter(contactsAdapter);
        binding.rvContacts.setLayoutManager(new LinearLayoutManager(this));

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddNewContactFormIntent();
            }
        });
    }

    private void openAddNewContactFormIntent() {
        Intent intent = new Intent(MainActivity.this, NewContactActivity.class);
        startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE) {
            String name = data.getStringExtra("name");
            String phone = data.getStringExtra("phone");
            String email = data.getStringExtra("email");

            Contact c = new Contact(name, phone, email);
            contactDao.insertAll(c);

            contacts = contactDao.getAllContacts();
            contactsAdapter = new ContactsAdapter(contacts);
            binding.rvContacts.setAdapter(contactsAdapter);
            binding.rvContacts.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}