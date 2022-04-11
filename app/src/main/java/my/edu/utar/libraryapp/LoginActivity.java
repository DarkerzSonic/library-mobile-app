package my.edu.utar.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText tf_username, tf_password;
    Button loginButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // hide the status/system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // hide the title/action bar
        getSupportActionBar().hide();

        // get FirebaseAuth Object
        mAuth = FirebaseAuth.getInstance();

        // declare & link to XML
        tf_username = (EditText) findViewById(R.id.tf_username);
        tf_password = (EditText) findViewById(R.id.tf_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        progressBar = (ProgressBar) findViewById(R.id.login_progressBar);

        // add Sign In button listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = tf_username.getText().toString().trim();
                String password = tf_password.getText().toString().trim();
                // check if username is null
                 if (username.isEmpty()){
                     tf_username.setError("Username is required!");
                     tf_username.requestFocus();
                     return;
                 }

                 // check if the username is in email format
                if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
                    tf_username.setError("Please enter your university email!");
                    tf_username.requestFocus();
                    return;
                }

                 // check if password < 6 chars or null
                if (password.isEmpty()){
                    tf_password.setError("Password is required!");
                    tf_password.requestFocus();
                    return;
                }

                if (password.length() < 6){
                    tf_password.setError("Password must be at least 6 characters!");
                    tf_password.requestFocus();
                    return;
                }

                // enable progress bar
                progressBar.setVisibility(View.VISIBLE);

                // Firebase sign-in
                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // redirect to library home page
                            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);  // disable progress bar visibility
                            if (username.equals("admin@1utar.my")) {
                                Intent i = new Intent(LoginActivity.this, LibMainActivity.class);
                                startActivity(i);
                            }
                            else
                            {
                                Intent i = new Intent(LoginActivity.this, StudentMainActivity.class);
                                startActivity(i);
                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Invalid username or password!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }
}