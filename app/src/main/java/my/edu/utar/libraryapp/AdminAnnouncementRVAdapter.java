package my.edu.utar.libraryapp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminAnnouncementRVAdapter extends RecyclerView.Adapter<AnnouncementVH> {
    // variable for our array list and context.
    private ArrayList<Announcement> announcementArrayList = new ArrayList<>();
    private Context context;
    private AdminAnnouncementRVAdapter adapter;
    DAOAnnouncement daoAnnouncement = new DAOAnnouncement();

    // alert box builder
    AlertDialog.Builder builder;

    public AdminAnnouncementRVAdapter (Context ctx){this.context = ctx;}

    public void setItems(ArrayList<Announcement> announcements){
        announcementArrayList.clear();
        announcementArrayList.addAll(announcements);}

//    // creating a constructor.
//    public AnnouncementRVAdapter(ArrayList<Announcement> announcementArrayList, Context context) {
//        this.announcementArrayList = announcementArrayList;
//        this.context = context;
//    }

    @NonNull
    @Override
    public AnnouncementVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.announcement_rv_item, parent, false);
        return new AnnouncementVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementVH holder, int position)
    {
        AnnouncementVH vh = (AnnouncementVH) holder;

        // getting data from our array list in our modal class.
        Announcement announcement = announcementArrayList.get(position);

        // on below line we are setting data to our text view.
        vh.announcementTitleTV.setText(announcement.getTitle());
        vh.announcementDateTV.setText(announcement.getDate());
        vh.announcementLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // declare alert box
                builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(announcement.getTitle())
                        .setMessage(announcement.getDescription())
                        .setCancelable(true)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                daoAnnouncement.remove(announcement.getKey());
                                Toast.makeText(view.getContext(), "Announcement deleted successfully!", Toast.LENGTH_LONG).show();
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return announcementArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
