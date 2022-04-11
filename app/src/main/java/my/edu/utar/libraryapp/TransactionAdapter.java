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

public class TransactionAdapter extends RecyclerView.Adapter {
    private Context context;
    ArrayList<Transaction> list = new ArrayList<>();
    public TransactionAdapter (Context ctx)
    {
        this.context = ctx;
    }

    public void setItems(ArrayList<Transaction> transaction)
    {
        list.clear();
        list.addAll(transaction);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_layout,parent,false);
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
        vh.date.setText(transaction.getDue_date());
        vh.book.setText(transaction.getISBN());
        vh.status.setText(transaction.getStatus());
        vh.transaction_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, TransactionActivity.class);
                i.putExtra("transaction", transaction);
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
