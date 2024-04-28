package com.kursach.ckursach;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Register_Activity extends AppCompatActivity {
    private FirebaseAuth auth;
    public static final String TAG = "Register Activity";
    private Button regBtn;
    private EditText email,password;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
       regBtn =  (MaterialButton ) findViewById(R.id.register_btn);
       email =  findViewById(R.id.editTextEmail);
       password =  findViewById(R.id.editTextTextPassword);
       storageRef = FirebaseStorage.getInstance().getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()|| !email.getText().toString().contains("@")) {
                    Toast.makeText(Register_Activity.this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
                }else {
                    createAccount(email.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            createUserFolder(email,password);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(Register_Activity.this, "Email Already Exists, use other email.", Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(Register_Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // [END create_user_with_email]
    }
    public void createUserFolder( String email,String password)  {

        ArrayList<String> strings =   new ArrayList<>(Arrays.asList(email.split("@")));
        strings.remove(1);

        String folderName = strings.get(0);
        Consts.getInstance().setUserName(folderName);
        StorageReference usersFolderRef = storageRef.child("users");
        StorageReference userFolderRef = usersFolderRef.child(folderName).child(folderName+".txt");
        FileOutputStream outputStream;
        try{
            outputStream = openFileOutput(folderName+".txt", Context.MODE_PRIVATE);
            outputStream.write((email+"\n"+password).getBytes());

        }catch (IOException ex){
            Log.e(TAG, "createUserFolder: ", ex);

        }
        FileInputStream inputStream;
        try{
            inputStream = openFileInput(folderName+".txt");
            userFolderRef.putStream(inputStream).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    Intent intent =  new Intent(Register_Activity.this, UserPanel_Activity.class);
                    startActivity(intent);
                }});

        }catch (IOException ex){
            Log.e(TAG, "createUserFolder: ", ex);
        }

    }
}