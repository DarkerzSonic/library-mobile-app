package my.edu.utar.libraryapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DateAdapter extends RecyclerView.Adapter {
    private Context context;
    ArrayList<Transaction> list = new ArrayList<>();
    public DateAdapter (Context ctx)
    {
        this.context = ctx;
    }

    public void setItems(ArrayList<Transaction> transaction)
    {
        list.addAll(transaction);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.date_layout,parent,false);
        return new TransactionVH(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Transaction e = null;
        this.onBindViewHolder(holder,position,e);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, Transaction e) {
        TransactionVH vh = (TransactionVH) holder;
        Transaction transaction = e == null? list.get(position): e;
        vh.borrowDate.setText(transaction.getBorrow_date());
        vh.dueDate.setText(transaction.getDue_date());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}