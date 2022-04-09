package my.edu.utar.libraryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BorrowBookActivity extends AppCompatActivity {

    ImageView img_book;
    TextView title, author, year, isbn, pages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);

        Intent i = getIntent();

        img_book = (ImageView) findViewById(R.id.book_iv);
        title = (TextView) findViewById(R.id.title_tv) ;
        author = (TextView) findViewById(R.id.author_tv);
        year = (TextView) findViewById(R.id.year_tv);
        isbn = (TextView) findViewById(R.id.ISBN_tv);
        pages = (TextView) findViewById(R.id.pages_tv);

        Book books = (Book) getIntent().getSerializableExtra("book");

        Picasso.get().load(books.getImage()).into(img_book);
        title.setText(books.getTitle());
        author.setText(books.getAuthor());
        year.setText(String.valueOf(books.getYear()));
        isbn.setText(books.getISBN());
        pages.setText(String.valueOf(books.getPages()));

    }
}