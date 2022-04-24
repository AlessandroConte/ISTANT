package com.example.istant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.istant.model.Activity;
import java.util.ArrayList;

public class ListAdapter_activities extends RecyclerView.Adapter<ListAdapter_activities.MyViewHolder> {

    /*
    public ListAdapter_activities(Context context, ArrayList<Activity> userArrayList){
        super(context,R.layout.listadapter_activity,userArrayList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Activity activity = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listadapter_activity,parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.listadapter_loan_picture);
        TextView userName = convertView.findViewById(R.id.listadapter_loan_name);
        TextView lastMsg = convertView.findViewById(R.id.listadapter_user_phonenumber);
        TextView time = convertView.findViewById(R.id.msgtime);

        // imageView.setImageResource(activity.getId());
        // TODO: fix
        userName.setText(activity.getNameActivity());
        userName.setAutoSizeTextTypeWithDefaults(userName.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        lastMsg.setText(activity.getDescription());
        time.setText(activity.getAddress());

        return convertView;
    }

     */

    private Context context;
    private ArrayList<Activity> activityArrayList;
    private OnActivityListener onActivityListener;

    public ListAdapter_activities(Context context, ArrayList<Activity> activityArrayList, OnActivityListener onActivityListener) {
        this.context = context;
        this.activityArrayList = activityArrayList;
        this.onActivityListener = onActivityListener;
    }

    @NonNull
    @Override
    public ListAdapter_activities.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listadapter_activity, parent, false);
        return new MyViewHolder(v, onActivityListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter_activities.MyViewHolder holder, int position) {
        Activity activity = activityArrayList.get(position);
        holder.name.setText(activity.getNameActivity());
        holder.description.setText(activity.getDescription());
    }

    @Override
    public int getItemCount() {
        return activityArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name, description;
        OnActivityListener onActivityListener;

        public MyViewHolder(@NonNull View itemView, OnActivityListener onActivityListener) {
            super(itemView);
            this.onActivityListener = onActivityListener;

            name = itemView.findViewById(R.id.listadapter_activityName);
            description = itemView.findViewById(R.id.listadapter_activityDescription);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            onActivityListener.onActivityClick(getAdapterPosition());
        }
    }

    public interface OnActivityListener {
        void onActivityClick(int position);
    }
}
