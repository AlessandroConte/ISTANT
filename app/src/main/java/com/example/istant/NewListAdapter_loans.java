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

public class NewListAdapter_loans extends RecyclerView.Adapter<NewListAdapter_loans.MyViewHolder> {

    private Context context;
    private ArrayList<Loan> loanArrayList;
    private OnLoanListener_ onLoanListener;

    public NewListAdapter_loans(Context context, ArrayList<Loan> loanArrayList, OnLoanListener_ onLoanListener) {
        this.context = context;
        this.loanArrayList = loanArrayList;
        this.onLoanListener = onLoanListener;
    }


    @NonNull
    @Override
    public NewListAdapter_loans.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listadapter_loan, parent, false);
        return new MyViewHolder(v, onLoanListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewListAdapter_loans.MyViewHolder holder, int position) {
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
        OnLoanListener_ onLoanListener;

        public MyViewHolder(@NonNull View itemView, OnLoanListener_ onLoanListener) {
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

    public interface OnLoanListener_ {
        void onLoanClick(int position);
    }
}
