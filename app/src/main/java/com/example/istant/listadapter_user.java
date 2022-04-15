package com.example.istant;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.istant.model.User;

import java.util.ArrayList;

public class listadapter_user extends ArrayAdapter<User> {

    public listadapter_user(Context context, ArrayList<User> userArrayList){
        super(context,R.layout.listadapter_user,userArrayList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listadapter_user,parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.listadapter_user_picture);
        TextView userName = convertView.findViewById(R.id.listadapter_user_namesurname);
        TextView userDescription = convertView.findViewById(R.id.listadapter_user_phonenumber);

        //Glide.with(get.this).load(user.getPhotoUrl()).into(imageView);

        imageView.setImageURI(Uri.parse(user.getPhotoUrl()));
        userName.setText(user.getName());
        userName.setAutoSizeTextTypeWithDefaults(userName.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        userDescription.setText(user.getTelephoneNumber());
        userDescription.setAutoSizeTextTypeWithDefaults(userName.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        return convertView;
    }
}
