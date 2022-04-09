package my.edu.utar.libraryapp;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAORoom {
    private DatabaseReference databaseReference;
    public DAORoom()
    {
        FirebaseDatabase db =FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Rooms");
    }
    public Task<Void> add(Room room)
    {
        return databaseReference.push().setValue(room);
    }

    public Task<Void> update(String key, HashMap<String ,Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key)
    {
        return databaseReference.child(key).removeValue();
    }

    public Query get(String key)
    {
        if(key == null)
        {
            //return databaseReference.orderByKey().limitToFirst(8);
            return databaseReference.orderByKey();
        }
        //return databaseReference.orderByKey().startAfter(key).limitToFirst(8);
        return databaseReference.orderByKey().startAfter(key);
    }

    public Query get()
    {
        return databaseReference;
    }
}