package com.masudin.firebaseupload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageView;
    TextView tvDESC;
    Button btnDelete;

    DatabaseReference databaseReference, dataRef;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.imageDetail);
        tvDESC = findViewById(R.id.desDetail);
        btnDelete = findViewById(R.id.deleteImage);

        String imageKey = getIntent().getStringExtra("ImageKey");

        dataRef = FirebaseDatabase.getInstance().getReference().child("image").child(imageKey);
        storageReference = FirebaseStorage.getInstance().getReference().child("images").child(imageKey + ".jpg");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("image");
        databaseReference.child(imageKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String imageDes = dataSnapshot.child("imageDes").getValue().toString();
                    String imageUrl = dataSnapshot.child("imageUrl").getValue().toString();

                    Picasso.get().load(imageUrl).into(imageView);
                    tvDESC.setText(imageDes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            }
                        });
                    }
                });
            }
        });
    }
}