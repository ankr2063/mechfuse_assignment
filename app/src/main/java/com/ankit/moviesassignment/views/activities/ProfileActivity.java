package com.ankit.moviesassignment.views.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ankit.moviesassignment.R;
import com.ankit.moviesassignment.model.classmodel.Userdata;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;

import static com.ankit.moviesassignment.views.activities.MainActivity.userdata;

public class ProfileActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    EditText name_et, email_et;
    TextView update;
    CircleImageView profile_pic;
    ImageView upload_image;
    Uri selectedImage;
    String filepath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name_et = findViewById(R.id.name_et);
        email_et = findViewById(R.id.email_et);
        update = findViewById(R.id.update);
        profile_pic = findViewById(R.id.profile_pic);
        upload_image = findViewById(R.id.upload_image_button);

        if(userdata.getFilepath()!=null) {
            File f = new File(userdata.getFilepath());
            Picasso.with(this).load(f).fit().centerCrop().into(profile_pic);
        }

        name_et.setText(userdata.getName());
        email_et.setText(userdata.getEmail());

        upload_image.setOnClickListener(v -> {
            if(!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                EasyPermissions.requestPermissions(this, "Read file", 1, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.putExtra("return-data", true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"),
                        2);
            }
        });

        name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((!name_et.getText().toString().equals(userdata.getName()))||(!email_et.getText().toString().equals(userdata.getEmail()))){
                    update.setVisibility(View.VISIBLE);
                    update.setOnClickListener(v -> {
                        userdata.setEmail(email_et.getText().toString());
                        userdata.setName(name_et.getText().toString());
                        Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                        name_et.clearFocus();
                        update.setVisibility(View.GONE);
                    });
                }else {
                    update.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((!name_et.getText().toString().equals(userdata.getName()))||(!email_et.getText().toString().equals(userdata.getEmail()))){
                    update.setVisibility(View.VISIBLE);
                    update.setOnClickListener(v -> {
                        userdata.setEmail(email_et.getText().toString());
                        userdata.setName(name_et.getText().toString());
                        Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                        email_et.clearFocus();
                        update.setVisibility(View.GONE);
                    });
                }else {
                    update.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("main_id", 1);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            selectedImage = data.getData();
            filepath = getRealPathFromURIPath(selectedImage, this);

            File f = new File(filepath);
            Picasso.with(this).load(f).into(profile_pic);
            userdata.setFilepath(filepath);
        }
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }
}
