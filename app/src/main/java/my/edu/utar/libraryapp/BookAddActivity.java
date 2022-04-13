package my.edu.utar.libraryapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class BookAddActivity extends AppCompatActivity {


    int SELECT_PHOTO = 1;
    Uri uri;
    String imgUID;
    Book temp;

    StorageReference storageReference;
    FirebaseStorage storage;

    AlertDialog.Builder builder;
    DAOBook daoBook;

    EditText title, author, year, pages, isbn;
    Button addBookBtn, uploadCoverBtn;
    ImageView coverImagePreview;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);

        title = findViewById(R.id.add_title);
        author = findViewById(R.id.add_author);
        year = findViewById(R.id.add_year);
        isbn = findViewById(R.id.add_isbn);
        pages = findViewById(R.id.add_pages);
        addBookBtn = findViewById(R.id.add_submit);
        uploadCoverBtn = findViewById(R.id.btn_uploadBookCover);
        coverImagePreview = findViewById(R.id.iv_coverImgPreview);
        progressBar = findViewById(R.id.progressBar_addBook);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        builder = new AlertDialog.Builder(this);

        daoBook = new DAOBook();

        uploadCoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PHOTO);
            }
        });

        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //validation
                if (title.getText().toString().isEmpty()){
                    title.setError("Please enter the student's IC No.!");
                    title.requestFocus();
                    return;
                }

                if (author.getText().toString().isEmpty()){
                    author.setError("Please enter the student name!");
                    author.requestFocus();
                    return;
                }

                if (year.getText().toString().isEmpty()){
                    year.setError("Please enter the student ID!");
                    year.requestFocus();
                    return;
                }

                if (isbn.getText().toString().isEmpty()){
                    isbn.setError("Please enter the student ID!");
                    isbn.requestFocus();
                    return;
                }

                if (pages.getText().toString().isEmpty()){
                    pages.setError("Please enter the student ID!");
                    pages.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                temp = new Book(title.getText().toString(), author.getText().toString(), Integer.parseInt(year.getText().toString()), isbn.getText().toString(), "",Integer.parseInt(pages.getText().toString()), true); //create temp book
                uploadImage();
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
                coverImagePreview.setImageBitmap(bitmap);
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
                            Toast.makeText(BookAddActivity.this,"Image uploaded to Firebase successfully!", Toast.LENGTH_SHORT).show();

                            // get the image location at Firebase Storage
                            temp.setImage(imgUID);

                            // add new student to Firebase user list
                            daoBook.add(temp);

                            builder.setTitle("Add Book Success")
                                    .setMessage("Book has been added to the system successfully!")
                                    .setCancelable(true)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
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