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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter {
    private Context context;
    ArrayList<Book> list = new ArrayList<>();
    public BookAdapter (Context ctx)
    {
        this.context = ctx;
    }

    public void setItems(ArrayList<Book> book)
    {
        list.addAll(book);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_layout,parent,false);
        return new BookVH(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Book e = null;
        this.onBindViewHolder(holder,position,e);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, Book e) {
        BookVH vh = (BookVH) holder;
        Book book = e == null? list.get(position): e;
        vh.txt_title.setText(book.getTitle());
        vh.txt_author.setText(book.getAuthor());
        vh.txt_year.setText(String.valueOf(book.getYear()));
        Picasso.get().load(book.getImage()).into(vh.img_book);
        vh.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, BorrowBookActivity.class);
                i.putExtra("book", book);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<Book> filteredlist) {
        // below line is to add our filtered
        // list in our course array list.
          list = filteredlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
}
