package com.kursach.ckursach;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.jetbrains.annotations.NotNull;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private final static String TAG = "LogIn_Activity";
    private MaterialButton signInButton;
    private EditText email, password;
    private TextView registerTxtView;
    private StorageReference storageRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        signInButton = findViewById(R.id.login_btn);
        email = findViewById(R.id.editTextLoginEmail);
        password =  findViewById(R.id.editTextLoginPassword);
        registerTxtView = findViewById(R.id.registerTextView);
        storageRef = FirebaseStorage.getInstance().getReference();
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onStart() {
        super.onStart();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()|| !email.getText().toString().contains("@")) {
                    Toast.makeText(MainActivity.this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
                }else {
                    signIn(email.getText().toString(), password.getText().toString());
                }
                signIn(email.getText().toString(), password.getText().toString());// TODO: 29.04.2024 delete this in release
                }
        });
        registerTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this, Register_Activity.class);
                startActivity(intent);
                finish();
            }
        });
       requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
       requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }







    private void signIn(String email, String password) {
//         [START sign_in_with_email]
        email = "ancenkokirill104@gmail.com";
        password = "dk137dark3";//todo DELETE THIS IN RELEASE

        String finalEmail1 = email;
        String finalPassword = password;
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            createUserFolder( finalEmail1, finalPassword);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END sign_in_with_email]
    }
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });
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
                    Intent intent =  new Intent(MainActivity.this, UserPanel_Activity.class);
                    startActivity(intent);
                }});

        }catch (IOException ex){
            Log.e(TAG, "createUserFolder: ", ex);
        }

    }
}