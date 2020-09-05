package com.masudin.firebaseupload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.masudin.firebaseupload.model.Image;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {
    EditText edtSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;

    FirebaseRecyclerOptions<Image> recyclerOptions;
    FirebaseRecyclerAdapter<Image,ViewHolder> adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("image");

        floatingActionButton = findViewById(R.id.fab);
        edtSearch = findViewById(R.id.search);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        loadData("");
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString() != null){
                    loadData(editable.toString());
                }else {
                    loadData("");
                }
            }
        });
    }

    private void loadData(String nama) {
        Query query = databaseReference.orderByChild("imageDes").startAt(nama).endAt(nama+"\uf8ff");

        recyclerOptions = new FirebaseRecyclerOptions.Builder<Image>().setQuery(query,Image.class).build();
        adapter = new FirebaseRecyclerAdapter<Image, ViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull Image model) {
                holder.tvDes.setText(model.getImageDes());
                Picasso.get().load(model.getImageUrl()).into(holder.imageView);

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HomeActivity.this,DetailActivity.class);
                        intent.putExtra("ImageKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,parent,false);

                return new ViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}