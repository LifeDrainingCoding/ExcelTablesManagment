package com.kursach.ckursach;


import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.kursach.ckursach.placeholder.CheckBoxItem;

import java.util.ArrayList;
import java.util.HashMap;

public class Consts {
    static Consts instance;
  private String userName;
    private static final String TAG = "RecViewAdapterForListTabs";
    public static final String USERS_DIR ="/users";
    public static final String CSV_DIR ="/csvFiles";
    public static final int DOWNLOAD_ACTION= 1;
    public static final int LIST_ACTION= 3;
    public static final int DELETE_ACTION= 2;
    private HashMap<Integer,CheckBoxItem> checkBoxItems;
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
    public HashMap<Integer, CheckBoxItem> getCheckBoxItems(){


        return checkBoxItems;

    }
    public synchronized void  execute(RecyclerView recyclerView,int actionCode) {



            ArrayList<CheckBoxItem> list = new ArrayList<>();
            StorageReference csvFilesDir = getCSVref();
            Task<ListResult> task = csvFilesDir.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {

                    CheckBoxItem fileName =  new CheckBoxItem( item.getName());
                    list.add(fileName);
                    Log.d(TAG, "execute: " + fileName);
                }
                checkBoxItems = new HashMap<Integer,CheckBoxItem>();
                recyclerView.setAdapter(new RecViewAdapterForListTabs(list, actionCode, new RecViewAdapterForListTabs.Callback() {
                    @Override
                    public void onItemClicked(int position, CheckBoxItem item) {
                        if( item.isChecked()){
                            checkBoxItems.put(position, item);
                        }else{
                            checkBoxItems.remove(position);
                        }

                    }
                }));
//
            }).addOnFailureListener(exception -> {
                Log.e(TAG, "execute: ", exception);
            });

    }

}
