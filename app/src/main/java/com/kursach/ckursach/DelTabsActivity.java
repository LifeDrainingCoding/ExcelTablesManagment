package com.kursach.ckursach;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.StorageReference;
import com.kursach.ckursach.placeholder.CheckBoxItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DelTabsActivity extends AppCompatActivity {
private static final String TAG = "DelTabsActivity";
    private static final Logger log = LogManager.getLogger(DelTabsActivity.class);
    private  MaterialButton deleteBtn, backToListBtn;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_tabs);
        deleteBtn=findViewById(R.id.delete_selected_tab);
        backToListBtn = findViewById(R.id.back_to_list_tabs_btn2);

        recyclerView = findViewById(R.id.list_of_deletables);
        Consts.getInstance().execute(recyclerView,Consts.DELETE_ACTION);



    }

    @Override
    protected void onStart() {
        super.onStart();
        deleteBtn.setOnClickListener(v -> {
           deleteTabs();

        });

    }

    public  void  deleteTabs(){
        StorageReference reference = Consts.getInstance().getCSVref();
        HashMap<Integer, CheckBoxItem> map = Consts.getInstance().getCheckBoxItems();
        disableButton(backToListBtn);
        ArrayList<CheckBoxItem> checkBoxItems = new ArrayList<>();
        map.forEach(new BiConsumer<Integer, CheckBoxItem>() {
            @Override
            public void accept(Integer integer, CheckBoxItem checkBoxItem) {
                checkBoxItems.add(checkBoxItem);
            }
        });
        checkBoxItems.forEach(new Consumer<CheckBoxItem>() {
            @Override
            public void accept(CheckBoxItem checkBoxItem) {
                 StorageReference fileref = reference.child(checkBoxItem.getFileName());

                fileref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused){
                        Log.i(TAG, "onSuccess: "+checkBoxItem.getFileName()+" is deleted");
                        if(checkBoxItems.indexOf(checkBoxItem) == checkBoxItems.size()-1){
                            enableButton(backToListBtn);
                          Consts.getInstance().execute(recyclerView,Consts.DELETE_ACTION);
                          
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.e(TAG, "onFailure: ",e );
                        Log.i(TAG, "onFailure: " + checkBoxItem.getFileName());
                    }
                });
            }
        });


    }
    public void disableButton(Button button){
        button.setActivated(false);
        button.setEnabled(false);
        button.setVisibility(View.GONE);
    }
    public void enableButton(Button button){
        button.setActivated(true);
        button.setEnabled(true);
        button.setVisibility(View.VISIBLE);
    }
}