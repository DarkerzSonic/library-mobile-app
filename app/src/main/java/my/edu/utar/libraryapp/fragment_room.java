package my.edu.utar.libraryapp;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_room#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_room extends Fragment implements DatePickerDialog.OnDateSetListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView dateText, book1, book2;
    LinearLayout room1part1, room1part2, room2part1, room2part2;
    Button r1s1,r1s2,r1s3,r1s4,r2s1,r2s2,r2s3,r2s4;
    DAORoom daoRoom;
    String key = null;

    String bookDate;

    public fragment_room() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_favourites.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_room newInstance(String param1, String param2) {
        fragment_room fragment = new fragment_room();
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
        View view = inflater.inflate(R.layout.fragment_room, container, false);

        // firebase Room object
        daoRoom = new DAORoom();

        dateText = (TextView) view.findViewById(R.id.tv_date);
        book1 = (TextView) view.findViewById(R.id.tv_book1);
        book2 = (TextView) view.findViewById(R.id.tv_book2);

        room1part1 = (LinearLayout) view.findViewById(R.id.ll_room1part1);
        room1part2 = (LinearLayout) view.findViewById(R.id.ll_room1part2);
        room2part1 = (LinearLayout) view.findViewById(R.id.ll_room2part1);
        room2part2 = (LinearLayout) view.findViewById(R.id.ll_room2part2);

        r1s1 = (Button) view.findViewById(R.id.btn_r1s1);
        r1s2 = (Button) view.findViewById(R.id.btn_r1s2);
        r1s3 = (Button) view.findViewById(R.id.btn_r1s3);
        r1s4 = (Button) view.findViewById(R.id.btn_r1s4);

        r2s1 = (Button) view.findViewById(R.id.btn_r2s1);
        r2s2 = (Button) view.findViewById(R.id.btn_r2s2);
        r2s3 = (Button) view.findViewById(R.id.btn_r2s3);
        r2s4 = (Button) view.findViewById(R.id.btn_r2s4);

        // button listener
        r1s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Room bookRoom = new Room(1,bookDate,1,true);
                daoRoom.add(bookRoom);
                r1s1.setClickable(false);
                r1s1.setBackgroundColor(Color.GRAY);
                Toast.makeText(getContext(), "Room booked successfully!", Toast.LENGTH_LONG).show();
            }
        });

        r1s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Room bookRoom = new Room(1,bookDate,2,true);
                daoRoom.add(bookRoom);
                r1s2.setClickable(false);
                r1s2.setBackgroundColor(Color.GRAY);
                Toast.makeText(getContext(), "Room booked successfully!", Toast.LENGTH_LONG).show();
            }
        });

        r1s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Room bookRoom = new Room(1,bookDate,3,true);
                daoRoom.add(bookRoom);
                r1s3.setClickable(false);
                r1s3.setBackgroundColor(Color.GRAY);
                Toast.makeText(getContext(), "Room booked successfully!", Toast.LENGTH_LONG).show();
            }
        });

        r1s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Room bookRoom = new Room(1,bookDate,4,true);
                daoRoom.add(bookRoom);
                r1s4.setClickable(false);
                r1s4.setBackgroundColor(Color.GRAY);
                Toast.makeText(getContext(), "Room booked successfully!", Toast.LENGTH_LONG).show();
            }
        });

        r2s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Room bookRoom = new Room(2,bookDate,1,true);
                daoRoom.add(bookRoom);
                r2s1.setClickable(false);
                r2s1.setBackgroundColor(Color.GRAY);
                Toast.makeText(getContext(), "Room booked successfully!", Toast.LENGTH_LONG).show();
            }
        });
        r2s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Room bookRoom = new Room(2,bookDate,2,true);
                daoRoom.add(bookRoom);
                r2s2.setClickable(false);
                r2s2.setBackgroundColor(Color.GRAY);
                Toast.makeText(getContext(), "Room booked successfully!", Toast.LENGTH_LONG).show();
            }
        });
        r2s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Room bookRoom = new Room(2,bookDate,3,true);
                daoRoom.add(bookRoom);
                r2s3.setClickable(false);
                r2s3.setBackgroundColor(Color.GRAY);
                Toast.makeText(getContext(), "Room booked successfully!", Toast.LENGTH_LONG).show();
            }
        });
        r2s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Room bookRoom = new Room(2,bookDate,4,true);
                daoRoom.add(bookRoom);
                r2s4.setClickable(false);
                r2s4.setBackgroundColor(Color.GRAY);
                Toast.makeText(getContext(), "Room booked successfully!", Toast.LENGTH_LONG).show();
            }
        });

        //dateText.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_left, 0, R.mipmap.ic_right, 0);
        book1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_down, 0);
        book2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_down, 0);

        // set displayed date to current date
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy (EEE)");
        SimpleDateFormat test = new SimpleDateFormat("EEE");
        String testSrt = test.format(date);
        String dateString = sdf.format(date);
        dateText.setText(dateString);

        // store current date to bookDate
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        bookDate = dateFormat.format(date);

        // retrieve record from Database
        retrieveRecord();

        // Room 1 Booking
        book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (room1part1.getVisibility() == View.GONE && room1part2.getVisibility() == View.GONE) {
                    room1part1.setVisibility(View.VISIBLE);
                    room1part2.setVisibility(View.VISIBLE);
                }
                else{
                    room1part1.setVisibility(View.GONE);
                    room1part2.setVisibility(View.GONE);
                }
            }
        });

        // Room 2 Booking
        book2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (room2part1.getVisibility() == View.GONE && room2part2.getVisibility() == View.GONE) {
                    room2part1.setVisibility(View.VISIBLE);
                    room2part2.setVisibility(View.VISIBLE);
                }
                else{
                    room2part1.setVisibility(View.GONE);
                    room2part2.setVisibility(View.GONE);
                }
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        return view;
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
                            if (room.status == true && room.date.equals(bookDate)) {
                                if (room.time_slot == 1) {
                                    r1s1.setClickable(false);
                                    r1s1.setBackgroundColor(Color.GRAY);
                                }
                                else if (room.time_slot == 2) {
                                    r1s2.setClickable(false);
                                    r1s2.setBackgroundColor(Color.GRAY);
                                }
                                else if (room.time_slot == 3) {
                                    r1s3.setClickable(false);
                                    r1s3.setBackgroundColor(Color.GRAY);
                                }
                                else{
                                    r1s4.setClickable(false);
                                    r1s4.setBackgroundColor(Color.GRAY);
                                }
                            }
                        }
                        else{
                            if (room.status == true && room.date.equals(bookDate)) {
                                if (room.time_slot == 1) {
                                    r2s1.setClickable(false);
                                    r2s1.setBackgroundColor(Color.GRAY);
                                }
                                else if (room.time_slot == 2) {
                                    r2s2.setClickable(false);
                                    r2s2.setBackgroundColor(Color.GRAY);
                                }
                                else if (room.time_slot == 3) {
                                    r2s3.setClickable(false);
                                    r2s3.setBackgroundColor(Color.GRAY);
                                }
                                else{
                                    r2s4.setClickable(false);
                                    r2s4.setBackgroundColor(Color.GRAY);
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

    private void refreshRoomUI(){
        r1s1.setClickable(true);
        r1s1.setBackgroundColor(getResources().getColor(R.color.blue));
        r1s2.setClickable(true);
        r1s2.setBackgroundColor(getResources().getColor(R.color.blue));
        r1s3.setClickable(true);
        r1s3.setBackgroundColor(getResources().getColor(R.color.blue));
        r1s4.setClickable(true);
        r1s4.setBackgroundColor(getResources().getColor(R.color.blue));

        r2s1.setClickable(true);
        r2s1.setBackgroundColor(getResources().getColor(R.color.blue));
        r2s2.setClickable(true);
        r2s2.setBackgroundColor(getResources().getColor(R.color.blue));
        r2s3.setClickable(true);
        r2s3.setBackgroundColor(getResources().getColor(R.color.blue));
        r2s4.setClickable(true);
        r2s4.setBackgroundColor(getResources().getColor(R.color.blue));
    }
}