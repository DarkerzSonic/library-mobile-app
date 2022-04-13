package my.edu.utar.libraryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibAdminFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button bookManagementBtn, announcementBtn, sendOverdueBtn, studentRegBtn, libLogOutBtn;
    AlertDialog.Builder builder;
    private FirebaseAuth mAuth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LibAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibAdminFragment newInstance(String param1, String param2) {
        LibAdminFragment fragment = new LibAdminFragment();
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
        View view = inflater.inflate(R.layout.fragment_lib_admin, container, false);

        // Firebase Authentication object
        mAuth = FirebaseAuth.getInstance();

        // declare alert box
        builder = new AlertDialog.Builder(getContext());
        bookManagementBtn = (Button) view.findViewById(R.id.btn_bookManagement);
        announcementBtn = (Button) view.findViewById(R.id.btn_announcement);
        sendOverdueBtn = (Button) view.findViewById(R.id.btn_sendOverdue);
        studentRegBtn = (Button) view.findViewById(R.id.btn_studentReg);
        libLogOutBtn = (Button) view.findViewById(R.id.btn_libLogOut);

        // Book Management Button
        bookManagementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(getContext(), BookManagementActivity.class);
                    getActivity().startActivity(i);

            }
        });

        // Announcement Management Button
        announcementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AnnouncementManagementActivity.class);
                getContext().startActivity(i);
            }
        });

        // Send Overdue Warning Button
        sendOverdueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Send Overdue Warning")
                        .setMessage("This function is still under development!")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
            }
        });

        // New Student Registration Button
        studentRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), StudentRegistration.class);
                getContext().startActivity(i);
            }
        });

        // Librarian Log Out Button
        libLogOutBtn.setOnClickListener(new View.OnClickListener() {
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
}