package com.kursach.ckursach;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.internal.StorageReferenceUri;
import org.apache.xmlbeans.impl.xb.xmlconfig.Qnametargetlist;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Consts {
    static Consts instance;
  private String userName;
    private static final String TAG = "RecViewAdapterForListTabs";
    public static final String USERS_DIR ="/users";
    public static final String CSV_DIR ="/csvFiles";
    private Consts(){

    }



    static Consts getInstance(){
        if(instance == null){
            instance = new Consts();
        }
        return instance;
    }
    void setUserName(String userName){
        this.userName = userName;
    }
   public String getUserName(){
        return userName;
    }
    public StorageReference getCSVref(){return FirebaseStorage.getInstance().getReference().child(CSV_DIR);

    }
    public synchronized void  execute(RecyclerView recyclerView) {
        ArrayList<String> list =  new ArrayList<>();
         StorageReference  csvFilesDir = getCSVref();
        Task<ListResult> task = csvFilesDir.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {

                String fileName = item.getName();
                list.add(fileName);
                Log.d(TAG, "execute: "+fileName );
            }
            recyclerView.setAdapter(new RecViewAdapterForListTabs(list));
//
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "execute: ", exception );
        });
    }


}
