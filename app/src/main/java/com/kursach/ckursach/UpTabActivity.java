package com.kursach.ckursach;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nareshchocha.filepickerlibrary.models.DocumentFilePickerConfig;
import com.nareshchocha.filepickerlibrary.models.PopUpConfig;
import com.nareshchocha.filepickerlibrary.models.PopUpType;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class UpTabActivity extends AppCompatActivity {
    private  MaterialButton chooseTabBtn,chooseTabsBtn, uploadBtn,backToTabsListBtn;
    private EditText editTextPath;
    private boolean isMultipleChoice;
    StorageReference storageReference;
    private static final String  TAG = "UpTabActivity";
    private  File fileToUpload;
    private ArrayList<File> filesToUpload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_tab);
        chooseTabBtn =  findViewById(R.id.choose_file_path_btn);
        chooseTabsBtn =  findViewById(R.id.choose_files_path_btn);
        uploadBtn = findViewById(R.id.upload_btn);
        backToTabsListBtn =  findViewById(R.id.back_to_list_tabs_btn);
        editTextPath =  findViewById(R.id.editTextPath);
    }


    @Override
    protected void onStart() {
        super.onStart();
        chooseTabBtn.setOnClickListener(v ->{
            isMultipleChoice = false;
        openFileChooser(isMultipleChoice); });
        chooseTabsBtn.setOnClickListener(v ->{
           isMultipleChoice = true;
           openFileChooser(isMultipleChoice);
        });
        uploadBtn.setActivated(false);
        uploadBtn.setClickable(false);
        uploadBtn.setVisibility(View.GONE);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMultipleChoice == true){
                    uploadFiles(filesToUpload);
                }else {
                    uploadFiles(fileToUpload);
                }
            }
        });
        backToTabsListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpTabActivity.this, ListOfAvailableTabsActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void openFileChooser(boolean multipleChoice) {
        if (multipleChoice == true){
         Intent intent = new FilePicker.Builder(this).addPickDocumentFile(new DocumentFilePickerConfig(null,null,true,null,null,null,null,null,null)).build();
        launcher.launch(intent);
        }else {
            Intent intent = new FilePicker.Builder(this).addPickDocumentFile(null).setPopUpConfig(new PopUpConfig("Choose profile", null, PopUpType.BOTTOM_SHEET, RecyclerView.VERTICAL)).build();
            launcher.launch(intent);
        }

    }

    // Обработчик результата выбора файла

        private ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
if(isMultipleChoice == false) {


    Intent data = result.getData();
    if (data != null) {
        String path = data.getStringExtra(Const.BundleExtras.FILE_PATH);

        fileToUpload = new File(path);
        if(FilenameUtils.getExtension(fileToUpload.toString()).equals("csv")){
            uploadBtn.setClickable(true);
            uploadBtn.setActivated(true);
            uploadBtn.setVisibility(View.VISIBLE);
            editTextPath.setText(fileToUpload.getAbsolutePath());}
        else {
            Toast.makeText(UpTabActivity.this, "incorrect file", Toast.LENGTH_SHORT).show();
        uploadBtn.setClickable(false);
        uploadBtn.setActivated(false);
            uploadBtn.setVisibility(View.GONE);

    }
}


}else {
    Intent data = result.getData();
    if (data != null) {
        ArrayList<String> paths = data.getStringArrayListExtra(Const.BundleExtras.FILE_PATH_LIST);
        paths.removeIf(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return !s.endsWith(".csv");
            }
        });

        filesToUpload =  new ArrayList<File>(){{}};
        for (String path : paths) {
            File file = new File(path);
            filesToUpload.add(file);

        }
        if(filesToUpload.isEmpty()){
            uploadBtn.setClickable(false);
            uploadBtn.setActivated(false);
            uploadBtn.setVisibility(View.GONE);
            Toast.makeText(UpTabActivity.this, "No one chosen files is correct ", Toast.LENGTH_SHORT).show();
        }else {
            uploadBtn.setVisibility(View.VISIBLE);
            uploadBtn.setClickable(true);
            uploadBtn.setActivated(true);
            editTextPath.setText(Arrays.toString(filesToUpload.toArray()));
        }
    }
}
                        }
                    }
                });
    public void uploadFiles(File file){
        backToTabsListBtn.setVisibility(View.GONE);
        backToTabsListBtn.setActivated(false);
        backToTabsListBtn.setClickable(false);
        storageReference = Consts.getInstance().getCSVref().child(FilenameUtils.getName(Consts.getInstance().phoneticTranscriber(file.toString())));
        Uri uri =  Uri.fromFile(file);
        UploadTask uploadTask = storageReference.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                backToTabsListBtn.setVisibility(View.VISIBLE);
                backToTabsListBtn.setActivated(true);
                backToTabsListBtn.setClickable(true);
                Log.i(TAG, "onSuccess:File uploaded: "+ Objects.requireNonNull(taskSnapshot.getMetadata()).getName());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.e(TAG, "onFailure: ", e );
            }
        });
    }
    public void uploadFiles(ArrayList<File> files){
        backToTabsListBtn.setVisibility(View.GONE);
        backToTabsListBtn.setActivated(false);
        backToTabsListBtn.setClickable(false);

        files.forEach(new Consumer<File>() {
            @Override
            public void accept(File file) {
                storageReference = Consts.getInstance().getCSVref().child(FilenameUtils.getName(Consts.getInstance().phoneticTranscriber(file.toString())));
                Uri uri =  Uri.fromFile(file);
                UploadTask uploadTask = storageReference.putFile(uri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Log.i(TAG, "onSuccess:File uploaded: "+ Objects.requireNonNull(taskSnapshot.getMetadata()).getName());
                        if(files.indexOf(file) == files.size()-1){

                            backToTabsListBtn.setVisibility(View.VISIBLE);
                            backToTabsListBtn.setActivated(true);
                            backToTabsListBtn.setClickable(true);
                            Toast.makeText(UpTabActivity.this, "All files uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                        Log.d(TAG, "onSuccess: "+files.indexOf(file) +" :"+files.size());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.e(TAG, "onFailure: ", e );
                    }
                });
            }
        });

    }
}
