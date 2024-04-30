package com.kursach.ckursach;

import android.content.Intent;
import android.widget.EditText;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class UpTabActivity extends AppCompatActivity {
    private  MaterialButton chooseTabBtn,ChooseTabsBtn, uploadBtn;
    private EditText editTextPath;
    private boolean isFolderChosen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_tab);
        chooseTabBtn =  findViewById(R.id.choose_path_btn);
        editTextPath =  findViewById(R.id.editTextPath);
    }


    @Override
    protected void onStart() {
        super.onStart();
        chooseTabBtn.setOnClickListener(v ->{
            isFolderChosen = false;
        openFileChooser(); });

    }

    public void openFileChooser() {
 Intent intent = new FilePicker.Builder(this).addPickDocumentFile(null).build();
        // Создаем Intent для открытия нативного окна выбора файлов


        // Указываем типы файлов, которые пользователь может выбрать
        launcher.launch(intent);
//        try {
//
//
//            launcher.launch(intent);
//        }catch (ActivityNotFoundException ex){
//            Toast.makeText(getApplicationContext(),"File Chooser Failed",Toast.LENGTH_LONG).show();
//            Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//            // Указываем типы файлов, которые пользователь может выбрать
//            intent.setType("text/csv"); // Можно указать конкретные типы MIME, если нужно ограничить выбор
//            // Устанавливаем флаги, чтобы разрешить только чтение файлов
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            try {
//
//
//                launcher.launch(intent1);
//            }catch (ActivityNotFoundException exception){
//                Toast.makeText(getApplicationContext(),"File Chooser2 Failed",Toast.LENGTH_LONG).show();
//            }
//        }

    }

    // Обработчик результата выбора файла

        private ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
if(isFolderChosen == false) {


    Intent data = result.getData();
    if (data != null) {
        String path = data.getStringExtra(Const.BundleExtras.FILE_PATH);

        File folder = new File(path);
        if (folder.listFiles() != null) {


            ArrayList<File> files = new ArrayList<File>(Arrays.asList(Objects.requireNonNull(folder.listFiles())));
            files.removeIf(new Predicate<File>() {
                @Override
                public boolean test(File file) {
                    return !file.getName().endsWith(".csv");
                }
            });
            editTextPath.setText(Arrays.deepToString(files.toArray()));
        } else {
            editTextPath.setText(path);
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

        ArrayList<File> files =  new ArrayList<File>(){{}};
        for (String path : paths) {
            File file = new File(path);
            files.add(file);
        }
            editTextPath.setText(Arrays.deepToString(files.toArray()));

    }
}
                        }
                    }
                });


        // Если на устройстве нет приложения для работы с файлами, обрабатываем ошибку


}