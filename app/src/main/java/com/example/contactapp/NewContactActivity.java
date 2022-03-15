package com.example.contactapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactapp.databinding.NewContactActivityBinding;

public class NewContactActivity extends AppCompatActivity {
    private NewContactActivityBinding binding;

    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;

    private static final String TAG = "NEW_CONTACT_TAG";

    private static final int WRITE_CONTACT_PERMISSION_CODE = 100;

    private static final int IMAGE_PICK_GALLERY_CODE = 200;

    private String[] contactPermissions;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NewContactActivityBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create new contact");
        Drawable drawable= getResources().getDrawable(R.drawable.ic_baseline_close_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(drawable);

        binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryIntent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.btn_save:
                onClickBtnSave();
            default:break;
        }
        return true;
    }

    private void onClickBtnSave() {
        String name = binding.etFirstName.getText().toString() + " " + binding.etLastName.getText().toString();
        String phone = binding.etPhone.getText().toString();
        String email = binding.etEmail.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        intent.putExtra("email", email);
        setResult(NEW_CONTACT_ACTIVITY_REQUEST_CODE, intent);
        finish();
    }

    private void openGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                imageUri = data.getData();
                binding.ivAvatar.setImageURI(imageUri);
            }
        } else {
            Toast.makeText(this, "Cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}
