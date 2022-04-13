package my.edu.utar.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;

public class BookUpdateActivity extends AppCompatActivity {

    EditText title, year, author, pages, isbn;
    Button update_btn;
    DAOBook daoBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_update);

        title = (EditText) findViewById(R.id.update_title);
        author = (EditText)findViewById(R.id.update_author);
        year = (EditText)findViewById(R.id.update_year);
        pages = (EditText)findViewById(R.id.update_pages);
        isbn = (EditText)findViewById(R.id.update_isbn);
        update_btn = (Button)findViewById(R.id.btn_update);

        daoBook = new DAOBook();
        Book book = (Book) getIntent().getSerializableExtra("book");


        update_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String temp_title = title.getText().toString();
                String temp_author = author.getText().toString();
                Integer temp_year = new Integer(0);
                Integer temp_pages = new Integer(0);

                String temp_isbn = isbn.getText().toString();


                if(temp_title.isEmpty()){
                    title.setError("Title is required");
                    title.requestFocus();
                    return;
                }

                if(temp_author.isEmpty()){
                    author.setError("Author is required");
                    author.requestFocus();
                    return;
                }
                if(temp_isbn.isEmpty()){
                    isbn.setError("ISBN is required");
                    isbn.requestFocus();
                    return;
                }

                try{
                    temp_year= Integer.parseInt(year.getText().toString());
                }
                catch (NumberFormatException e){
                    year.setError("Year is required");
                    year.requestFocus();
                    return;
                }
                try {
                    temp_pages = Integer.parseInt(pages.getText().toString());
                }
                catch (NumberFormatException e)
                {
                    pages.setError("Pages is required");
                    pages.requestFocus();
                    return;
                }


                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("title", temp_title);
                hashMap.put("author", temp_author);
                hashMap.put("year", temp_year);
                hashMap.put("pages", temp_pages);
                hashMap.put("isbn", temp_isbn);
                daoBook.update(book.getKey(),hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(BookUpdateActivity.this, "Record is updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BookUpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}