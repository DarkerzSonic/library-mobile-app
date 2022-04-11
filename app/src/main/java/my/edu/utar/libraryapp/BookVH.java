package my.edu.utar.libraryapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class BookVH extends RecyclerView.ViewHolder {

    public TextView txt_title, txt_author, txt_year;
    public TextView admintxt_title, admintxt_author, admintxt_year, admintxt_id;
    public ImageView img_book, adminimg;
    public CardView cardView, admincardView;
    public BookVH(@NonNull View itemView) {
        super(itemView);
        txt_title = itemView.findViewById(R.id.txt_title);
        txt_author = itemView.findViewById(R.id.txt_author);
        txt_year = itemView.findViewById(R.id.txt_year);
        cardView = itemView.findViewById(R.id.cv);
        img_book = itemView.findViewById(R.id.img_book);


        admintxt_title = itemView.findViewById(R.id.admintxt_title);
        admintxt_author = itemView.findViewById(R.id.admintxt_borrowdate);
        admintxt_year = itemView.findViewById(R.id.admintxt_status);
        admincardView = itemView.findViewById(R.id.admincv);
        adminimg = itemView.findViewById(R.id.adminimg_book);
        admintxt_id = itemView.findViewById(R.id.admintxt_id);

    }
}
