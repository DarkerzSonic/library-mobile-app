package my.edu.utar.libraryapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminBookAdapter extends RecyclerView.Adapter {
    private Context context;
    ArrayList<Transaction> list = new ArrayList<>();
    DAOTransaction daoTransaction;
    String key = null;

    public AdminBookAdapter (Context ctx)
    {
        this.context = ctx;
    }

    public void setItems(ArrayList<Transaction> book)
    {
        list.clear();
        list.addAll(book);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adminbook_layout,parent,false);
        return new BookVH(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Transaction e = null;
        this.onBindViewHolder(holder,position,e);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, Transaction e) {
        BookVH vh = (BookVH) holder;
        Transaction transaction = e == null? list.get(position): e;
        vh.admintxt_title.setText(transaction.getTitle());
        vh.admintxt_author.setText(transaction.getBorrow_date());
        vh.admintxt_year.setText(transaction.getStatus());
        Picasso.get().load(transaction.getBook_img()).into(vh.adminimg);
        vh.admincardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AdminTransactionActivity.class);
                i.putExtra("transaction", transaction);
                context.startActivity(i);
            }
        });
        vh.admintxt_id.setText(transaction.getStudent_ID());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<Transaction> filteredlist) {
        // below line is to add our filtered
        // list in our course array list.
        list = filteredlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
}
