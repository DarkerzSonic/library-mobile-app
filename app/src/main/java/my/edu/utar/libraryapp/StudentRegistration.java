package my.edu.utar.libraryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class StudentRegistration extends AppCompatActivity {
    int SELECT_PHOTO = 1;
    Uri uri;
    String imgUID;
    User temp;

    StorageReference storageReference;
    FirebaseStorage storage;
    private FirebaseAuth mAuth;
    AlertDialog.Builder builder;
    DAOUser daoUser;

    // XML components
    EditText text_studentName, text_studentID, text_studentEmail, text_studentIC;
    Button registerStudentBtn, uploadImageBtn;
    ImageView studentImgPreview;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        // link XML components
        text_studentName = findViewById(R.id.tf_studentName);
        text_studentID = findViewById(R.id.tf_studentID);
        text_studentEmail = findViewById(R.id.tf_studentEmail);
        text_studentIC = findViewById(R.id.tf_studentIC);
        registerStudentBtn = findViewById(R.id.btn_registerStudent);
        uploadImageBtn = findViewById(R.id.btn_uploadImage);
        studentImgPreview = findViewById(R.id.iv_studentImgPreview);
        progressBar = findViewById(R.id.progressBar_register);

        // Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // declare alert box
        builder = new AlertDialog.Builder(this);

        // declare FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // declare DAOUser object
        daoUser = new DAOUser();

        // upload image listener
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PHOTO);
            }
        });

        // register student listener
        registerStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display progressBar
                progressBar.setVisibility(View.VISIBLE);

                // check if the student email is in email format
                if (!Patterns.EMAIL_ADDRESS.matcher(text_studentEmail.getText()).matches()){
                    text_studentEmail.setError("Please enter a valid email!");
                    text_studentEmail.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                // check if any of the fields is empty
                if (text_studentIC.getText().toString().isEmpty()){
                    text_studentIC.setError("Please enter the student's IC No.!");
                    text_studentIC.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                if (text_studentName.getText().toString().isEmpty()){
                    text_studentName.setError("Please enter the student name!");
                    text_studentName.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                if (text_studentID.getText().toString().isEmpty()){
                    text_studentID.setError("Please enter the student ID!");
                    text_studentID.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                // create temp User
                temp = new User(text_studentID.getText().toString(), text_studentName.getText().toString(), text_studentEmail.getText().toString(),"",text_studentIC.getText().toString(),true,"");

                // sign up new student to Firebase Authentication
                mAuth.createUserWithEmailAndPassword(temp.getEmail(),temp.getIdentityNo())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    // get the new user's Firebase UID
                                    temp.setFirebaseUID(task.getResult().getUser().getUid());

                                    // upload student image to Firebase Cloud Storage
                                    uploadImage();
                                }
                                else{
                                    builder.setTitle("Error")
                                            .setMessage("Duplicate login credentials in the system!")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    dialogInterface.cancel();
                                                }
                                            }).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                studentImgPreview.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(){
        if (uri != null){
            // Defining the child of storageReference
            StorageReference ref = storageReference.child(UUID.randomUUID().toString());

            // add listeners for uploading
            ref.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgUID = uri.toString();
                            Toast.makeText(StudentRegistration.this,"Image uploaded to Firebase successfully!", Toast.LENGTH_SHORT).show();

                            // get the image location at Firebase Storage
                            temp.setImg(imgUID);

                            // add new student to Firebase user list
                            daoUser.add(temp);
                            builder.setTitle("Registration Success")
                                    .setMessage("New student credentials added to the system successfully!")
                                    .setCancelable(true)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            dialogInterface.cancel();
                                            finish();
                                        }
                                    }).show();
                        }
                    });
                }
            });
        }
    }
}