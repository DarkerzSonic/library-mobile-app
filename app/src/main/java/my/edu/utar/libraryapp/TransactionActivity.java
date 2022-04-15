package my.edu.utar.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TransactionActivity extends AppCompatActivity {

    String key = null;
    String key1 = null;
    RecyclerView recyclerView;
    DateAdapter adapter;
    DAOTransaction daoTransaction;
    ArrayList<Transaction> transactions = new ArrayList<>();

    DAOBook daoBook;
    Book book;

    Transaction transaction;
    ImageView img_book;
    TextView title, author, year, isbn;
    TextView due_date, borrow_date;
    Button extend_btn;
    // Reload the activity when back button is navigated back to this Activity
    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        // hide the status/system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // hide the title/action bar
        getSupportActionBar().hide();

        Intent i = getIntent();

        img_book = findViewById(R.id.book_img);
        title = findViewById(R.id.title_tv1);
        author = findViewById(R.id.author_tv1);
        year = findViewById(R.id.year_tv1);
        isbn = findViewById(R.id.isbn_tv1);
        extend_btn = findViewById(R.id.extend_btn);
        due_date = findViewById(R.id.transaction_due);
        borrow_date = findViewById(R.id.transaction_borrow);

        transaction = (Transaction) getIntent().getSerializableExtra("transaction");

        daoTransaction = new DAOTransaction();
        daoBook = new DAOBook();

        loadDate();
        loadBook();
        isbn.setText(transaction.getISBN());

        extend_btn.setOnClickListener(new View.OnClickListener() {
            String newDueDate;
            @Override
            public void onClick(View view) {
                try {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date borrowDate= dateFormat.parse(transaction.getBorrow_date()); //get borrow date
                    Date oldDate= dateFormat.parse(transaction.getDue_date()); //get current due date
                    Calendar c = Calendar.getInstance();
                    c.setTime(oldDate);
                    c.add(Calendar.DATE, 7); // Adding 7 days to current due date


                    Date newTime = c.getTime();  // new due date
                    long time_diff = newTime.getTime() - borrowDate.getTime();
                    long days_difference = (time_diff / (1000*60*60*24)) % 365;  //find diff between new due date and borrow date

                    newDueDate = dateFormat.format(newTime);

                    if(days_difference < 21) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("due_date", newDueDate);
                        daoTransaction.update(transaction.getKey(), hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(TransactionActivity.this, "Duration extended", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TransactionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        daoTransaction.get(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot data : snapshot.getChildren()) {
                                    Transaction temp_transaction = data.getValue(Transaction.class);
                                    temp_transaction.setKey(data.getKey());
                                    //validation
                                    if (temp_transaction.getKey().equals(transaction.getKey())) {
                                        borrow_date.setText(temp_transaction.getBorrow_date());
                                        due_date.setText(temp_transaction.getDue_date());
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }


                        });
                    }
                    else{
                        Toast.makeText(TransactionActivity.this,"Duration exceeded!", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void loadDate() {
        daoTransaction.get(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot data : snapshot.getChildren()){
                    Transaction temp_transaction = data.getValue(Transaction.class);
                    temp_transaction.setKey(data.getKey());
                    key = data.getKey();
                    if(temp_transaction.getKey().equals(transaction.getKey())) {
                        borrow_date.setText(temp_transaction.getBorrow_date());
                        due_date.setText(temp_transaction.getDue_date());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

    private void loadBook() {
        daoBook.get(key1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot data : snapshot.getChildren()){
                    Book book = data.getValue(Book.class);
                    book.setKey(data.getKey());
                    key1 = data.getKey();
                    //validation
                    if(book.getISBN().equals(transaction.getISBN())) {
                        Picasso.get().load(book.getImage()).into(img_book);
                        title.setText(book.getTitle());
                        author.setText(book.getAuthor());
                        year.setText(String.valueOf(book.getYear()));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }


}