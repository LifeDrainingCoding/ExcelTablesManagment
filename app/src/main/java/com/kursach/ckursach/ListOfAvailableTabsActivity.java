package com.kursach.ckursach;

import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListOfAvailableTabsActivity extends AppCompatActivity {
   private StorageReference csvFilesDir;
   Task<ListResult> task;
   private MaterialButton deleteTabsBtn, uploadTabsBtn;

   private RecyclerView recyclerView;


    private static final String TAG = "List_Of_Available_Tabs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_avaible_tabs);
         recyclerView = findViewById(R.id.list_of_csv);

        deleteTabsBtn = findViewById(R.id.delete_tab_btn);
        uploadTabsBtn = findViewById(R.id.upload_tab_btn);

    }

    @Override
    protected void onStart() {
        super.onStart();
        uploadTabsBtn.setOnClickListener(v -> {
            Intent intent =  new Intent(this, UpTabActivity.class);
            startActivity(intent);

        });

        Consts.getInstance().execute(recyclerView);

    }


}