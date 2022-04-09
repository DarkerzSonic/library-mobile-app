package my.edu.utar.libraryapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class BookVH extends RecyclerView.ViewHolder {

    public TextView txt_title, txt_author, txt_year;
    public ImageView img_book;
    public CardView cardView;
    public BookVH(@NonNull View itemView) {
        super(itemView);
        txt_title = itemView.findViewById(R.id.txt_title);
        txt_author = itemView.findViewById(R.id.txt_author);
        txt_year = itemView.findViewById(R.id.txt_year);

        cardView = itemView.findViewById(R.id.cv);
        img_book = itemView.findViewById(R.id.img_book);

    }
}
