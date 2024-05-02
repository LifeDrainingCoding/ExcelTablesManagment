package com.kursach.ckursach;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.kursach.ckursach.placeholder.CheckBoxItem;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DownloadActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MaterialButton btnDownload,btnBackToList;
    StorageReference csvRef ;
    private static final String TAG = "DownloadActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        recyclerView =  findViewById(R.id.list_of_downloads);
        btnDownload=  findViewById(R.id.download_tab);
        btnBackToList =  findViewById(R.id.back_to_list_tabs_btn1);
        csvRef = Consts.getInstance().getCSVref();

    }


    @Override
    protected void onStart() {
        super.onStart();
        Consts.getInstance().execute(recyclerView, Consts.DOWNLOAD_ACTION);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  FilePicker.Builder(DownloadActivity.this).addPickDocumentFile(null).build();

               launcher.launch(intent);

            }
        });
        btnBackToList.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListOfAvailableTabsActivity.class);
            startActivity(intent);
            finish();
        });

    }
    private  ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if(o.getResultCode() == RESULT_OK){

                Intent data = o.getData();
                if(data != null){
                    disableButton(btnBackToList);
                    ArrayList<File> files = new ArrayList<>();

                    HashMap<Integer, CheckBoxItem> map =  Consts.getInstance().getCheckBoxItems();

                    String path = FileUtils.getFile(data.getStringExtra(Const.BundleExtras.FILE_PATH)).getParent();


                    File folderToDownload = new File(path);
                    map.forEach(new BiConsumer<Integer, CheckBoxItem>() {
                        @Override
                        public void accept(Integer integer, CheckBoxItem checkBoxItem) {
                            File file = new File(FilenameUtils.getName(checkBoxItem.getFileName()));

                            files.add(file);



                        }
                    });
                    files.forEach(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            StorageReference storageReference = csvRef.child(file.getName());

                            File file1;


                                file1 =  new File(folderToDownload.toString()+"/"+file.getName());
                       FileOutputStream outputStream = null;
                       try {
                           outputStream = new FileOutputStream(file1);

                       }catch (IOException ex){
                           throw new RuntimeException(ex);
                       }
                            storageReference.getFile(file1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Log.i(TAG, "onSuccess: "+file1.getAbsolutePath());
                                        if(files.indexOf(file) == files.size()-1){
                                            enableButton(btnBackToList);
                                        }
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Log.e(TAG, "onFailure: ",e );
                                    }
                                });



                        }
                    });



                }
            }
        }
    });
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
