package my.edu.utar.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

public class BookDeleteManagementActivity extends AppCompatActivity {

    Book books;
    DAOBook daoBook;

    Bundle savedInstanceState;

    @Override
    public void onRestart() {
        super.onRestart();
       finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_delete_management);

        Intent i = getIntent();

        ImageView img_book = (ImageView) findViewById(R.id.book_mgnt_book_iv);
        TextView title = (TextView) findViewById(R.id.book_mgnt_title_tv);
        TextView author = (TextView) findViewById(R.id.book_mgnt_author_tv);
        TextView year = (TextView) findViewById(R.id.book_mgnt_year_tv);
        TextView isbn = (TextView) findViewById(R.id.book_mgnt_ISBN_tv);
        TextView pages = (TextView) findViewById(R.id.book_mgnt_pages_tv);
        Button btn_delete = (Button) findViewById(R.id.btn_delete);
        Button btn_edit = (Button) findViewById(R.id.btn_edit);

        books = (Book) getIntent().getSerializableExtra("book");

        Picasso.get().load(books.getImage()).into(img_book);
        title.setText(books.getTitle());
        author.setText(books.getAuthor());
        year.setText(String.valueOf(books.getYear()));
        isbn.setText(books.getISBN());
        pages.setText(String.valueOf(books.getPages()));

        daoBook = new DAOBook();


        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daoBook.remove(books.getKey()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(BookDeleteManagementActivity.this,"Book is deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookDeleteManagementActivity.this, BookManagementActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BookDeleteManagementActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(BookDeleteManagementActivity.this, BookUpdateActivity.class);
                    i.putExtra("book",books);
                    startActivity(i);
            }
        });
    }
}