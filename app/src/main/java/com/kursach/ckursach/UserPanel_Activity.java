package com.kursach.ckursach;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserPanel_Activity extends AppCompatActivity {
    private Boolean isShowed = false;
    private  Button showMetadataBtn;
    private TextView emailView, passwordView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);
        emailView = findViewById(R.id.userEmail);
        passwordView =  findViewById(R.id.userPassword);
        auth = FirebaseAuth.getInstance();
        user =  auth.getCurrentUser();
        showMetadataBtn =  findViewById(R.id.metadataBtn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getEmail();
        showMetadataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserMetadataFragment dialog = new UserMetadataFragment();
                dialog.show(getSupportFragmentManager(),"User Metadata");
            }
        });

        //TODO: сделать получение пароля  password =   passwordView.getText().toString()+" "+user.get

    }
    public void getEmail(){
        if(isShowed == false){

        isShowed=true;
        String email,password;
        email = emailView.getText().toString() +" "+user.getEmail();
        emailView.setText(email);
        }
    }
}