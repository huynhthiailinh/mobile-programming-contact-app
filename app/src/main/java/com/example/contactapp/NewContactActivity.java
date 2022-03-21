package com.example.contactapp;

import android.content.ContentValues;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactapp.databinding.NewContactActivityBinding;

public class NewContactActivity extends AppCompatActivity {
    private NewContactActivityBinding binding;
    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
//    image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 500;
//    image picked uri
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
                showImagePickDialog();
            }
        });
    }

    private void showImagePickDialog() {
//        options to display in dialog
        String[] options = {"Camera", "Gallery"};
//        dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        handle clicks
                        if (i == 0) {
//                            camera clicked
                            pickFromCamera();
                        } else {
//                            gallery clicked
                            pickFromGallery();
                        }
                    }
                })
                .show();
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_contact_menu, menu);
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
        Intent intent = new Intent();

        intent.putExtra("avatarUri", imageUri.toString());

        String name = binding.etFirstName.getText().toString() + " " + binding.etLastName.getText().toString();
        intent.putExtra("name", name);

        String phone = binding.etPhone.getText().toString();
        intent.putExtra("phone", phone);

        String email = binding.etEmail.getText().toString();
        intent.putExtra("email", email);

        setResult(NEW_CONTACT_ACTIVITY_REQUEST_CODE, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                binding.ivAvatar.setImageURI(imageUri);
            } else if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                imageUri = data.getData();
                binding.ivAvatar.setImageURI(imageUri);
            }
        } else {
            Toast.makeText(this, "Cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}
