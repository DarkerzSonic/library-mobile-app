package my.edu.utar.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class StudentMainActivity extends AppCompatActivity {

    DAOTransaction daoTransaction;
    String key = null;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide the status/system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // hide the title/action bar
        getSupportActionBar().hide();

        //Initialize Bottom Navigation View.
        BottomNavigationView navView = findViewById(R.id.bottomNav_view);

        //Pass the ID's of Different destinations
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_books, R.id.navigation_profile, R.id.navigation_room )
                .build();
        //Initialize NavController.
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Firebase Authentication Object
        mAuth = FirebaseAuth.getInstance();

        // unsubscribe to FCM Overdue by default
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Overdue");

        // initialize DAOTransaction object
        daoTransaction = new DAOTransaction();

        // Validation on book status
        daoTransaction.get(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //ArrayList<Room> room_arr = new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        // retrieve record from firebase and store the key
                        Transaction transaction = data.getValue(Transaction.class);
                        transaction.setKey(data.getKey());

                        // if the students has overdue book, subscribe to FCM Overdue topic
                        if (transaction.getStatus().equals("Overdue") && mAuth.getUid().equals(transaction.getUserUID())){
                            FirebaseMessaging.getInstance().subscribeToTopic("Overdue");
                            Log.i("Overdue student","Overdue");
                        }

                        //update transaction status
                        try {
                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date()); // Using today's date
                            Date today = c.getTime();

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Date dueDate= dateFormat.parse(transaction.getDue_date()); //due date
                            long time_diff = dueDate.getTime() - today.getTime();
                            long days_difference = (time_diff / (1000*60*60*24)) % 365;

                            if(days_difference < 0)
                            {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("status", "Overdue");
                                daoTransaction.update(transaction.getKey(), hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(StudentMainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                        } catch (ParseException e) {
                            e.printStackTrace();
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