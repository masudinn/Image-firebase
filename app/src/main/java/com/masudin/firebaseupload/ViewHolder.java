package com.masudin.firebaseupload;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView tvDes;
    View view;

    public ViewHolder(@NonNull View itemView) {

        super(itemView);
        imageView = itemView.findViewById(R.id.imagecard);
        tvDes = itemView.findViewById(R.id.descard);

        view = itemView;
    }
}
