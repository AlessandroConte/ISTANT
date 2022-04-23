package com.example.istant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.istant.model.Loan;
import java.util.ArrayList;

public class ListAdapter_loans extends RecyclerView.Adapter<ListAdapter_loans.MyViewHolder> {

    private Context context;
    private ArrayList<Loan> loanArrayList;
    private OnLoanListener onLoanListener;

    public ListAdapter_loans(Context context, ArrayList<Loan> loanArrayList, OnLoanListener onLoanListener) {
        this.context = context;
        this.loanArrayList = loanArrayList;
        this.onLoanListener = onLoanListener;
    }


    @NonNull
    @Override
    public ListAdapter_loans.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listadapter_loan, parent, false);
        return new MyViewHolder(v, onLoanListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter_loans.MyViewHolder holder, int position) {
        Loan loan = loanArrayList.get(position);
        holder.name.setText(loan.getNameLoan());
        holder.dateStart.setText(loan.getDateStart().toString()); // TODO: fix
        holder.dateEnd.setText(loan.getDateEnd().toString()); // TODO: fix
    }

    @Override
    public int getItemCount() {
        return loanArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, dateStart, dateEnd;
        OnLoanListener onLoanListener;

        public MyViewHolder(@NonNull View itemView, OnLoanListener onLoanListener) {
            super(itemView);
            this.onLoanListener = onLoanListener;

            name = itemView.findViewById(R.id.listadapter_loanName);
            dateStart = itemView.findViewById(R.id.listadapter_loanDateStart);
            dateEnd = itemView.findViewById(R.id.listadapter_loanDateEnd);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onLoanListener.onLoanClick(getAdapterPosition());
        }
    }

    public interface OnLoanListener{
        void onLoanClick(int position);
    }

    /*
    public ListAdapter_loans(Context context, ArrayList<Loan> userArrayList){
        super(context,R.layout.list_item,userArrayList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Loan loan = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listadapter_loan,parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.listadapter_loan_picture);
        TextView userName = convertView.findViewById(R.id.listadapter_loan_name);

        // imageView.setImageResource(activity.getId());
        // TODO: fix
        userName.setText(loan.getNameLoan());
        userName.setAutoSizeTextTypeWithDefaults(userName.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        return convertView;
    }

     */

}
