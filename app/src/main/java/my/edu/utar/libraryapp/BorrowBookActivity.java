package my.edu.utar.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BorrowBookActivity extends AppCompatActivity {

    ImageView img_book;
    TextView title, author, year, isbn, pages;
    Button btn_borrow;
    DAOBook dao;
    DAOUser daoUser;
    DAOTransaction daoTransaction;
    String key = null;
    Book books;
    String studentID;
    private FirebaseAuth mAuth;

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
        btn_borrow = (Button) findViewById(R.id.btn_borrow);

        books = (Book) getIntent().getSerializableExtra("book");

        Picasso.get().load(books.getImage()).into(img_book);
        title.setText(books.getTitle());
        author.setText(books.getAuthor());
        year.setText(String.valueOf(books.getYear()));
        isbn.setText(books.getISBN());
        pages.setText(String.valueOf(books.getPages()));

        mAuth = FirebaseAuth.getInstance();

        dao = new DAOBook();
        daoUser = new DAOUser();
        daoTransaction = new DAOTransaction();

        getData();
        getUser();

        btn_borrow.setOnClickListener(new View.OnClickListener() {

            Boolean status = false;
            Boolean current_status = false;
            @Override
            public void onClick(View view) {

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("status", status);
                    dao.update(books.getKey(), hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Transaction transaction = new Transaction(books.getISBN(),studentID,"N/A","N/A","Pending", mAuth.getUid(), books.getTitle(), books.getImage());
                            daoTransaction.add(transaction);
                            btn_borrow.setClickable(false);
                            btn_borrow.setBackgroundColor(Color.GRAY);
                            btn_borrow.setText("Unavailable");

                            Toast.makeText(BorrowBookActivity.this, "Book borrowed successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BorrowBookActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


            }
        });

    }

    private void getData(){
        dao.get(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    //retrieve record
                    Book book = data.getValue(Book.class);
                    book.setKey(data.getKey());

                    //validation
                    if(book.getKey().equals(books.getKey())) {
                        if (!book.isStatus()) {
                            btn_borrow.setClickable(false);
                            btn_borrow.setBackgroundColor(Color.GRAY);
                            btn_borrow.setText("Unavailable");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUser(){

        daoUser.get(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //ArrayList<Room> room_arr = new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        // retrieve record from firebase and store the key
                        User user = data.getValue(User.class);
                        user.setKey(data.getKey());

                        // check whether the user info retrieved matches the current user
                        if (user.firebaseUID.equals(mAuth.getUid())){
                            studentID = user.getStudentID();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}