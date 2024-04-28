package com.kursach.ckursach;

import android.content.Context;
import android.content.ContextWrapper;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.internal.StorageReferenceUri;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Consts {
    static Consts instance;
  private String userName;
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

}
