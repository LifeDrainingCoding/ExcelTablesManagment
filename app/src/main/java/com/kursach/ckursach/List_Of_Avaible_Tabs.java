package com.kursach.ckursach;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class List_Of_Avaible_Tabs extends AppCompatActivity {
    StorageReference csvFilesDir;
    Task<ListResult> task;

    RecyclerView recyclerView;
    private static final String TAG = "List_Of_Avaible_Tabs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_avaible_tabs);
         recyclerView = findViewById(R.id.list_of_csv);
        csvFilesDir = Consts.getInstance().getCSVref();


    }

    @Override
    protected void onStart() {
        super.onStart();
       execute();

    }

    public void execute() {
        ArrayList<String> list = new ArrayList<>();



        task = csvFilesDir.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {

                String fileName = item.getName();
                list.add(fileName);
                Log.d(TAG, "execute: "+fileName );
                recyclerView.setAdapter(new RecViewAdapterForListTabs(list));
            }
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "execute: ", exception );
        });
        if(list.isEmpty()){

        }else{

        }


    }
}