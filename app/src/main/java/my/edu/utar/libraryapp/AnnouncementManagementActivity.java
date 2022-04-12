package my.edu.utar.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AnnouncementManagementActivity extends AppCompatActivity {

    Button addAnnouncementBtn;

    String key = null;
    DAOAnnouncement daoAnnouncement;
    private ArrayList<Announcement> announcementArrayList = new ArrayList<>();
    private AdminAnnouncementRVAdapter announcementRVAdapter;
    private RecyclerView announcementRV;
    AlertDialog.Builder builder;
    String currentDate;

    @Override
    protected void onResume() {
        super.onResume();
        retrieveRecord();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_management);

        // hide the status/system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // hide the title/action bar
        getSupportActionBar().hide();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        announcementRV = findViewById(R.id.rv_adminAnnouncement);
        announcementRV.setHasFixedSize(true);
        announcementRVAdapter = new AdminAnnouncementRVAdapter(this);
        announcementRV.setLayoutManager(manager);
        announcementRV.setAdapter(announcementRVAdapter);
        daoAnnouncement = new DAOAnnouncement();
        addAnnouncementBtn = findViewById(R.id.btn_addAnnouncement);

        // add announcement button listener
        addAnnouncementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

        // declare alert box
        builder = new AlertDialog.Builder(this);

        //retrieveRecord();

        // add onScrollListener to RecyclerView
        announcementRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//                if (totalItem < lastVisible + 3){
//                    if (!isLoading){
//                        isLoading = true;
//                        retrieveRecord();
//                    }
//                }
            }
        });
    }

    public void retrieveRecord(){
        key = null;
        announcementArrayList.clear();
        daoAnnouncement.get(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot data : snapshot.getChildren()){
                    Announcement announcement = data.getValue(Announcement.class);
                    announcement.setKey(data.getKey());
                    announcementArrayList.add(announcement);
                    key = data.getKey();
                }
                announcementRVAdapter.setItems(announcementArrayList);
                announcementRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

    private void showCustomDialog(){
        final Dialog dialog = new Dialog(AnnouncementManagementActivity.this);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_announcement_dialog);

        // initialize view of dialogs
        EditText titleText = dialog.findViewById(R.id.input_announcementTitle);
        EditText descText = dialog.findViewById(R.id.input_announcementDesc);
        Button addNewBtn = dialog.findViewById(R.id.btn_addNew);

        // dialog button listener
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // store current date to bookDate
                long date = System.currentTimeMillis();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                currentDate = dateFormat.format(date);

                String title = titleText.getText().toString();
                String desc = descText.getText().toString();

                // add announcement to Firebase
                daoAnnouncement.add(new Announcement(currentDate, title, desc))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AnnouncementManagementActivity.this, "New announcement added successfully!", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            retrieveRecord();
                        }
                        else{
                            Toast.makeText(AnnouncementManagementActivity.this, "Fail to add new announcement!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}