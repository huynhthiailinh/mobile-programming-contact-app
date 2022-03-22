package com.example.contactapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactapp.databinding.DetailContactActivityBinding;

public class DetailContactActivity extends AppCompatActivity {
    private DetailContactActivityBinding binding;
    private static final int NEW_EDIT_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    private Contact contact;
    private AppDatabase appDatabase;
    private ContactDao contactDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetailContactActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detail contact");
        Drawable drawable= getResources().getDrawable(R.drawable.ic_baseline_close_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(drawable);

        appDatabase = AppDatabase.getInstance(this);
        contactDao = appDatabase.contactDao();

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        contact = (Contact) bundle.get("object_contact");
        loadData(contact);
    }

    private void loadData(Contact contact) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(Uri.parse(contact.getAvatarUri()),
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        binding.ivAvatar.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        binding.tvName.setText(contact.getName());
        binding.tvMobile.setText(contact.getMobile());
        binding.tvEmail.setText(contact.getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_edit:
                onClickBtnEdit();

            default:break;
        }
        return true;
    }

    private void onClickBtnEdit() {
        Intent intent = new Intent(DetailContactActivity.this, AddEditContactActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_contact", contact);
        bundle.putSerializable("is_edit_mode", true);
        intent.putExtras(bundle);
        startActivityForResult(intent, NEW_EDIT_CONTACT_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_EDIT_CONTACT_ACTIVITY_REQUEST_CODE) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
            contact = (Contact) bundle.get("edited_contact");
            loadData(contact);
        }
    }
}
