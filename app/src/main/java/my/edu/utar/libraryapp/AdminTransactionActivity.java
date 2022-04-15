package my.edu.utar.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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

public class AdminTransactionActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
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
    TextView title, author, year, isbn, borrow_by;
    TextView due_date, borrow_date;
    Button btn_return , btn_collect;
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
        setContentView(R.layout.activity_admin_transaction);

        // hide the status/system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // hide the title/action bar
        getSupportActionBar().hide();

        Intent i = getIntent();

        img_book = findViewById(R.id.adminimg_book);
        title = findViewById(R.id.admintitle_tv);
        author = findViewById(R.id.adminauthor_tv);
        year = findViewById(R.id.adminyear_tv);
        isbn = findViewById(R.id.adminisbn_tv);
        borrow_by = findViewById(R.id.adminby_tv);
        due_date = findViewById(R.id.admintransaction_due);
        borrow_date = findViewById(R.id.admintransaction_borrow);
        btn_return = findViewById(R.id.return_btn);
        btn_collect = findViewById(R.id.collect_btn);

        transaction= (Transaction) getIntent().getSerializableExtra("transaction");

        daoTransaction = new DAOTransaction();
        daoBook = new DAOBook();

        builder = new AlertDialog.Builder(this);

        title.setText(transaction.getTitle());
        borrow_by.setText(transaction.getStudent_ID());
        isbn.setText(transaction.getISBN());
        borrow_date.setText(transaction.getBorrow_date());
        due_date.setText(transaction.getDue_date());


        long date = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String borrowDate = dateFormat.format(date);

        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Using today's date
        c.add(Calendar.DATE, 14); // Adding 14 days
        String dueDate = dateFormat.format(c.getTime());

        loadBook();

        btn_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("status", "Collected");
                hashMap.put("borrow_date", borrowDate);
                hashMap.put("due_date", dueDate);


                    daoTransaction.update(transaction.getKey(), hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            borrow_date.setText(borrowDate);
                            due_date.setText(dueDate);
                            Toast.makeText(AdminTransactionActivity.this, "Book collected", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminTransactionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            }
        });


        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("status", "Returned");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date dueDate= dateFormat.parse(transaction.getDue_date());
                    Date currentDate = new Date();
                   long time_diff = currentDate.getTime() - dueDate.getTime();
                   long day_diff = (time_diff / (1000*60*60*24)) % 365;

                   if(day_diff > 0)
                   {
                       float fine = (float) (day_diff * 0.50);
                     //  Toast.makeText(AdminTransactionActivity.this, String.valueOf(fine),Toast.LENGTH_SHORT).show();
                       builder.setTitle("Overdue")
                               .setMessage("The overdue amount is ".concat(String.valueOf(fine)))
                               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       dialogInterface.cancel();
                                   }
                               }).show();
                   }
                    daoTransaction.update(transaction.getKey(), hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AdminTransactionActivity.this, "Book returned", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminTransactionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (ParseException e) {
                    e.printStackTrace();
                }



            }

        });
    }

//    private void loadDate() {
//        daoTransaction.get(key).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for(DataSnapshot data : snapshot.getChildren()){
//                    Transaction temp_transaction = data.getValue(Transaction.class);
//                    temp_transaction.setKey(data.getKey());
//                    key = data.getKey();
//                    if(temp_transaction.getKey().equals(transaction.getKey())) {
//                        borrow_date.setText(temp_transaction.getBorrow_date());
//                        due_date.setText(temp_transaction.getDue_date());
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//
//        });
//    }

    private void loadBook() {
        daoBook.get(key1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot data : snapshot.getChildren()){
                    Book temp_book = data.getValue(Book.class);
                    temp_book.setKey(data.getKey());
                   // key1 = data.getKey();
                    //validation
                    if(temp_book.getISBN().equals(transaction.getISBN())) {
                        author.setText(temp_book.getAuthor());
                        year.setText(String.valueOf(temp_book.getYear()));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }


}