package com.example.contactapp;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactapp.databinding.DetailContactActivityBinding;

public class DetailContactActivity extends AppCompatActivity {
    private DetailContactActivityBinding binding;

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

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        Contact contact = (Contact) bundle.get("object_contact");

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
    }
}
