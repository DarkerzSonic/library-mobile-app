package my.edu.utar.libraryapp;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AnnouncementVH extends RecyclerView.ViewHolder {
    // creating a variable for our text view and image view.
    public TextView announcementTitleTV, announcementDateTV;
    public LinearLayout announcementLayout;

    public AnnouncementVH(@NonNull View itemView) {
        super(itemView);

        // initializing our variables.
        announcementTitleTV = itemView.findViewById(R.id.tv_announcementTitle);
        announcementDateTV = itemView.findViewById(R.id.tv_announcementDate);
        announcementLayout = itemView.findViewById(R.id.ll_announcement);

    }
}
