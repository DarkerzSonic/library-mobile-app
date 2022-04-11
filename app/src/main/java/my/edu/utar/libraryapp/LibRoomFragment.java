package my.edu.utar.libraryapp;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibRoomFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView dateText, r1s1,r1s2,r1s3,r1s4,r2s1,r2s2,r2s3,r2s4;
    DAORoom daoRoom;
    String key = null;

    String bookDate;

    public LibRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibRoomFragment newInstance(String param1, String param2) {
        LibRoomFragment fragment = new LibRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lib_room, container, false);

        // firebase Room object
        daoRoom = new DAORoom();

        dateText = (TextView) view.findViewById(R.id.tv_dateLib);

        r1s1 = (TextView) view.findViewById(R.id.tv_r1s1);
        r1s2 = (TextView) view.findViewById(R.id.tv_r1s2);
        r1s3 = (TextView) view.findViewById(R.id.tv_r1s3);
        r1s4 = (TextView) view.findViewById(R.id.tv_r1s4);

        r2s1 = (TextView) view.findViewById(R.id.tv_r2s1);
        r2s2 = (TextView) view.findViewById(R.id.tv_r2s2);
        r2s3 = (TextView) view.findViewById(R.id.tv_r2s3);
        r2s4 = (TextView) view.findViewById(R.id.tv_r2s4);

        // set displayed date to current date
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy (EEE)");
        String dateString = sdf.format(date);
        dateText.setText(dateString);

        // store current date to bookDate
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        bookDate = dateFormat.format(date);

        // retrieve record from Database
        retrieveRecord();

        // set DatePicker listener
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        return view;
    }

    private void retrieveRecord(){
        // read Rooms data from Firebase
        daoRoom.get(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //ArrayList<Room> room_arr = new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        // retrieve record from firebase and store the key
                        Room room = data.getValue(Room.class);
                        room.setKey(data.getKey());

                        // check the room number & validate whether the room is booked
                        if (room.room_no == 1){
                            if (room.status && room.date.equals(bookDate)) {
                                if (room.time_slot == 1) {
                                    //r1s1.setClickable(false);
                                    //r1s1.setBackgroundColor(Color.GRAY);
                                    r1s1.setText(R.string.room_occupied);
                                    r1s1.setTextColor(Color.RED);
                                }
                                else if (room.time_slot == 2) {
                                    r1s2.setText(R.string.room_occupied);
                                    r1s2.setTextColor(Color.RED);
                                }
                                else if (room.time_slot == 3) {
                                    r1s3.setText(R.string.room_occupied);
                                    r1s3.setTextColor(Color.RED);
                                }
                                else{
                                    r1s4.setText(R.string.room_occupied);
                                    r1s4.setTextColor(Color.RED);
                                }
                            }
                        }
                        else{
                            if (room.status && room.date.equals(bookDate)) {
                                if (room.time_slot == 1) {
                                    r2s1.setText(R.string.room_occupied);
                                    r2s1.setTextColor(Color.RED);
                                }
                                else if (room.time_slot == 2) {
                                    r2s2.setText(R.string.room_occupied);
                                    r2s2.setTextColor(Color.RED);
                                }
                                else if (room.time_slot == 3) {
                                    r2s3.setText(R.string.room_occupied);
                                    r2s3.setTextColor(Color.RED);
                                }
                                else{
                                    r2s4.setText(R.string.room_occupied);
                                    r2s4.setTextColor(Color.RED);
                                }
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getView().getContext(), this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000*60*60*24*6);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        // refresh UI
        refreshRoomUI();
        retrieveRecord();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        // date formatter for Firebase Realtime Database
        SimpleDateFormat firebaseFormat = new SimpleDateFormat("dd/MM/yyyy");
        bookDate = firebaseFormat.format(calendar.getTime());

        // display date
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy (EEE)");
        String strDate = format.format(calendar.getTime());
        dateText.setText(strDate);
    }

    private void refreshRoomUI(){
        r1s1.setText(R.string.room_available);
        r1s1.setTextColor(Color.GREEN);
        r1s2.setText(R.string.room_available);
        r1s2.setTextColor(Color.GREEN);
        r1s3.setText(R.string.room_available);
        r1s3.setTextColor(Color.GREEN);
        r1s4.setText(R.string.room_available);
        r1s4.setTextColor(Color.GREEN);

        r2s1.setText(R.string.room_available);
        r2s1.setTextColor(Color.GREEN);
        r2s2.setText(R.string.room_available);
        r2s2.setTextColor(Color.GREEN);
        r2s3.setText(R.string.room_available);
        r2s3.setTextColor(Color.GREEN);
        r2s4.setText(R.string.room_available);
        r2s4.setTextColor(Color.GREEN);
    }
}