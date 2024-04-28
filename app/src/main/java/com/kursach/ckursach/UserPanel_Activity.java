package com.kursach.ckursach;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

public class UserPanel_Activity extends AppCompatActivity {
    private Boolean isShowed = false;
    private  Button showMetadataBtn, gotoTablesBtn;
    private TextView emailView, passwordView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    StorageReference storageRef;
    Boolean isHided =  true;
    public static final String TAG = "UserPanel_Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);
        emailView = findViewById(R.id.userEmail);
        passwordView =  findViewById(R.id.userPassword);
        auth = FirebaseAuth.getInstance();
        gotoTablesBtn =  findViewById(R.id.gotoTablesList);
        user =  auth.getCurrentUser();
        showMetadataBtn =  findViewById(R.id.metadataBtn);
        storageRef = FirebaseStorage.getInstance().getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getEmailAndPassword();
        showMetadataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserMetadataFragment dialog = new UserMetadataFragment();
                dialog.show(getSupportFragmentManager(),"User Metadata");
            }
        });
gotoTablesBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent =  new Intent(UserPanel_Activity.this, List_Of_Avaible_Tabs.class);
        startActivity(intent);
        finish();
    }
});
        //TODO: сделать получение пароля  password =   passwordView.getText().toString()+" "+user.get

    }
    public void getEmailAndPassword(){
        StorageReference sRef = storageRef.child(Consts.USERS_DIR+"/"+Consts.getInstance().getUserName()).child(Consts.getInstance().getUserName()+".txt");

sRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
    @Override
    public void onSuccess(byte[] bytes) {
      String  content = new String(bytes, StandardCharsets.UTF_8);
        String email,password = "";



        String[] parts = content.split("\n");
        email = parts[0];
        password = parts[1];
        String hidedPassword = hidePassword(password);





        if(isShowed == false){

            isShowed=true;

            email = emailView.getText().toString() +" "+user.getEmail();
            emailView.setText(email);
            passwordView.setText(passwordView.getText().toString()+" "+hidedPassword);

            String finalPassword = password;
            String finalHidedPassword = hidedPassword;
            passwordView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String string = "Password: ";
                    if(isHided){
                        isHided =  false;
                        passwordView.setText(string+" "+ finalPassword);
                    }else {

                        isHided =  true;
                        passwordView.setText(string+" "+ finalHidedPassword);
                    }

                }
            });
        }
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull @NotNull Exception e) {
        Log.e(TAG, "onFailure: ",e );
    }
});



    }
    public String hidePassword(String password){
        StringBuilder sb= new StringBuilder();
        ArrayList<String> partsOfPassword = new ArrayList<>(Arrays.asList(password.split("")));
        partsOfPassword.forEach(new Consumer<String>() {

            @Override
            public void accept(String s) {
                s = "*";
                sb.append(s);
            }
        });
        return sb.toString();
    }
}