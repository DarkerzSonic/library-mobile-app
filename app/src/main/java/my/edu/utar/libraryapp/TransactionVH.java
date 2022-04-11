package my.edu.utar.libraryapp;

import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class TransactionVH extends RecyclerView.ViewHolder {

    public TextView date, book, status;
    public TableRow transaction_row;
    public TextView borrowDate, dueDate;

    public TransactionVH(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.date_tv);
        book = itemView.findViewById(R.id.book_tv);
        status = itemView.findViewById(R.id.status_tv);
        transaction_row = itemView.findViewById(R.id.transaction_row);
        borrowDate = itemView.findViewById(R.id.borrowdate_tv);
        dueDate = itemView.findViewById(R.id.duedate_tv);
    }
}
