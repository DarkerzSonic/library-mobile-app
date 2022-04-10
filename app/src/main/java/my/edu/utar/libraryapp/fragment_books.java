package my.edu.utar.libraryapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_books#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_books extends Fragment {

    private EditText title, author, year, isbn, pages, status;
    RecyclerView recyclerView;
    BookAdapter adapter;
    DAOBook dao;
    SearchView searchView;
    String key = null;
    private Button submit;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<Book> books = new ArrayList<>();
    public fragment_books() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_search.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_books newInstance(String param1, String param2) {
        fragment_books fragment = new fragment_books();
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
        View view =  inflater.inflate(R.layout.fragment_books, container, false);

//        title = (EditText) view.findViewById(R.id.title);
//
//        author = (EditText) view.findViewById(R.id.author);
//
//        year = (EditText) view.findViewById(R.id.year);
//
//        isbn = (EditText) view.findViewById(R.id.isbn);
//
//        pages = (EditText) view.findViewById(R.id.pages);
//
//        status = (EditText) view.findViewById(R.id.status);
//
//        submit = (Button) view.findViewById(R.id.add_submit);
//
//
//        DAOBook dao = new DAOBook();
//
//
//
//        submit.setOnClickListener(view1 -> {
//            Book book = new Book(title.getText().toString(), author.getText().toString(), Integer.parseInt(year.getText().toString()), isbn.getText().toString(), Integer.parseInt(pages.getText().toString()), Boolean.parseBoolean(status.getText().toString()));
//
//            dao.add(book).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                    Toast.makeText(getActivity(),"Record is inserted",Toast.LENGTH_SHORT).show();
//                }
//            });
//        });

        searchView = (SearchView) view.findViewById(R.id.sv);

        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new BookAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        dao = new DAOBook();
        loadData();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });


        return view;

    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Book> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Book item : books) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
           // Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    private void loadData() {
        dao.get(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot data : snapshot.getChildren()){
                    Book book = data.getValue(Book.class);
                    book.setKey(data.getKey());
                    books.add(book);
                    key = data.getKey();
                }
                adapter.setItems(books);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
}