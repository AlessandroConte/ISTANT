package com.example.istant;

import android.content.Context;
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
import com.example.istant.model.Loan;

import java.util.ArrayList;

public class ListAdapter_loans extends ArrayAdapter<Loan> {

    public ListAdapter_loans(Context context, ArrayList<Loan> userArrayList){
        super(context,R.layout.list_item,userArrayList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Loan loan = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.listadapter_user_picture);
        TextView userName = convertView.findViewById(R.id.listadapter_user_namesurname);
        TextView lastMsg = convertView.findViewById(R.id.listadapter_user_phonenumber);
        TextView time = convertView.findViewById(R.id.msgtime);

        // imageView.setImageResource(activity.getId());
        // TODO: fix
        userName.setText(loan.getNameLoan());
        userName.setAutoSizeTextTypeWithDefaults(userName.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        lastMsg.setText(loan.getDescription());
        time.setText(loan.getId());

        return convertView;
    }
}
