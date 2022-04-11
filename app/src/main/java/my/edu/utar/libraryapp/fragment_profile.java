package my.edu.utar.libraryapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_profile extends Fragment {

    // Firebase object key
    String key = null;

    // Firebase mAuth
    private FirebaseAuth mAuth;

    // XML components
    Button logoutButton;
    // Alert Dialog Box
    AlertDialog.Builder builder;
    // DAOUser Firebase
    DAOUser daoUser;

    ImageView imgUser;
    TextView tv_name, tv_studentID;

    RecyclerView recyclerView;
    TransactionAdapter adapter;
    DAOTransaction daoTransaction;
    ArrayList<Transaction> transactions = new ArrayList<>();
    String userID;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_profile newInstance(String param1, String param2) {
        fragment_profile fragment = new fragment_profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTransaction();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Firebase mAuth declaration
        mAuth = FirebaseAuth.getInstance();

        //DAOUser
        daoUser = new DAOUser();

//        User test = new User("1","Admin","admin@1utar.my","",false,"VWr70JX8Z1XOhPegyi93SUk3AMS2");
//
//        daoUser.add(test);

        // declare alert box
        builder = new AlertDialog.Builder(getContext());

        // link XML components
        logoutButton = (Button) view.findViewById(R.id.btn_logout);
        imgUser = (ImageView) view.findViewById(R.id.img_user);
        tv_name = (TextView) view.findViewById(R.id.tv_profileName);
        tv_studentID = (TextView) view.findViewById(R.id.tv_profileID);

        recyclerView = view.findViewById(R.id.transaction_rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new TransactionAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        daoTransaction = new DAOTransaction();
        //loadTransaction();


        // get profile from Firebase
        daoUser.get(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //ArrayList<Room> room_arr = new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        // retrieve record from firebase and store the key
                        User user = data.getValue(User.class);
                        user.setKey(data.getKey());

                        // check whether the user info retrieved matches the current user
                        if (user.firebaseUID.equals(mAuth.getUid())){
                            tv_name.setText(user.name);
                            tv_studentID.setText(user.studentID);
                            Picasso.get().load(user.getImg()).into(imgUser);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // set logout Button listener
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Log Out")
                        .setMessage("Are you sure?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAuth.signOut();
                                Toast.makeText(getContext(), "Log out successfully!", Toast.LENGTH_LONG).show();
                                getActivity().finish();
                                //dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
            }
        });

        return view;
    }

    private void loadTransaction() {
        transactions.clear();

        daoTransaction.get(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot data : snapshot.getChildren()){
                    Transaction temp_transaction = data.getValue(Transaction.class);
                    temp_transaction.setKey(data.getKey());
                    if(temp_transaction.getUserUID().equals(mAuth.getUid())){
                        transactions.add(temp_transaction);
                    }

                }

                adapter.setItems(transactions);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

}