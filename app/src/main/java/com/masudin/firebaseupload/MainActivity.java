package com.masudin.firebaseupload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText edtDesc;
    private TextView tvProgress;
    private ProgressBar progressBar;
    private Button btnUpload;

    private static final int REQUEST_CODE = 101;
    Uri imageUri;
    boolean isImageAdd = false;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.addImage);
        edtDesc = findViewById(R.id.edtDes);
        tvProgress = findViewById(R.id.percent);
        progressBar = findViewById(R.id.progress_horizontal);
        btnUpload = findViewById(R.id.btnUpload);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("image");
        storageReference = FirebaseStorage.getInstance().getReference().child("images");

        tvProgress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String imageDes = edtDesc.getText().toString();
                if(isImageAdd != false && imageDes!= null){
                    uploadImage(imageDes);
                }
            }
        });

    }

    private void uploadImage(final String imageDes) {
        tvProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        final String key = databaseReference.push().getKey();
        storageReference.child(key + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("imageDes",imageDes);
                        hashMap.put("imageUrl",uri.toString());

                        databaseReference.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent (getApplicationContext(),HomeActivity.class));
                                Toast.makeText(MainActivity.this, "Success Upload !", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                tvProgress.setText(progress + " % ");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && data !=  null){
            imageUri = data.getData();
            isImageAdd =true;
            imageView.setImageURI(imageUri);
        }
    }
}