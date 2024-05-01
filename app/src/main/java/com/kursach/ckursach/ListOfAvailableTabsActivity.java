package com.kursach.ckursach;
import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;

public class ListOfAvailableTabsActivity extends AppCompatActivity {
   private MaterialButton deleteTabsBtn, uploadTabsBtn,downloadTabsBtn;

   private RecyclerView recyclerView;


    private static final String TAG = "List_Of_Available_Tabs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_avaible_tabs);
         recyclerView = findViewById(R.id.list_of_csv);
        downloadTabsBtn = findViewById(R.id.download_tab_btn);
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
        deleteTabsBtn.setOnClickListener(v -> {
            Intent intent =  new Intent(this, DelTabsActivity.class);
            startActivity(intent);
        });
        downloadTabsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, DownloadActivity.class);
            startActivity(intent);
        });

        Consts.getInstance().execute(recyclerView,Consts.LIST_ACTION);



    }

    @Override
    protected void onResume() {
        super.onResume();
        Consts.getInstance().execute(recyclerView, Consts.LIST_ACTION);

    }


}