package com.example.individualassignemnt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private ArrayList<ExpenseItem> expenseList;

    public ExpenseAdapter(ArrayList<ExpenseItem> expenseList) {
        this.expenseList = expenseList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView amountTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExpenseItem expense = expenseList.get(position);
        holder.nameTextView.setText(expense.getName());

        // Convert the amount to a string and set it to the amountTextView
        String amountText = String.valueOf(expense.getTotalAmount());
        holder.amountTextView.setText(amountText);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }
}