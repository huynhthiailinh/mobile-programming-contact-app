package com.example.contactapp;

import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactapp.databinding.AddEditContactActivityBinding;

public class AddEditContactActivity extends AppCompatActivity {
    private AddEditContactActivityBinding binding;
    private static final int NEW_EDIT_CONTACT_ACTIVITY_REQUEST_CODE = 1;
//    image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 500;
//    image picked uri
    private Uri imageUri;
    private boolean isEditMode = false;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddEditContactActivityBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);

        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("is_edit_mode", false);

        ActionBar actionBar = getSupportActionBar();

        if (isEditMode) {
            actionBar.setTitle("Edit contact");

            Bundle bundle = getIntent().getExtras();
            if (bundle == null) {
                return;
            }
            contact = (Contact) bundle.get("object_contact");

            if (contact.getAvatarUri().equals("null")) {
                binding.ivAvatar.setImageResource(R.drawable.ic_baseline_person_24);
            } else {
                imageUri = Uri.parse(contact.getAvatarUri());

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(Uri.parse(contact.getAvatarUri()),
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                binding.ivAvatar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }

            binding.etFirstName.setText(contact.getName().split(" ")[0]);
            binding.etLastName.setText(contact.getName().split(" ")[1]);
            binding.etMobile.setText(contact.getMobile());
            binding.etEmail.setText(contact.getEmail());

        } else {
            actionBar.setTitle("Create new contact");
        }

        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_close_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(drawable);

        binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_contact_menu, menu);
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

    private void showImagePickDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            pickFromCamera();
                        } else {
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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void onClickBtnSave() {
        Intent intent = new Intent();

        intent.putExtra("avatarUri", imageUri.toString());
        intent.putExtra("name", binding.etFirstName.getText().toString()
                + " " + binding.etLastName.getText().toString());
        intent.putExtra("mobile", binding.etMobile.getText().toString());
        intent.putExtra("email", binding.etEmail.getText().toString());

        setResult(NEW_EDIT_CONTACT_ACTIVITY_REQUEST_CODE, intent);
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
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(imageUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                binding.ivAvatar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        } else {
            Toast.makeText(this, "Cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}
