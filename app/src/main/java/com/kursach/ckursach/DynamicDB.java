package com.kursach.ckursach;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DynamicDB extends SQLiteOpenHelper {
    public static final String TAG = "DynamicDB";
    private final static String DATABASE_NAME = "DynamicDB.db";
    private final static int DATABASE_VERSION = 1;
    private static final Logger log = LogManager.getLogger(DynamicDB.class);

    public DynamicDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
    public void createTableFromCsv(Context context, String csvFilePath) {
        String TABLE_NAME = FilenameUtils.getBaseName(csvFilePath);
        String COLUMN_NAME = "column_name";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            List<String> columnNames = new ArrayList<>();
            List<String> columnTypes = new ArrayList<>();


            BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(csvFilePath))));
            String line;
            try {


                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    columnNames.add(parts[0]);
                    columnTypes.add(parts[1]);
                }
            }catch (ArrayIndexOutOfBoundsException ex){
                Toast.makeText(context, "Wrong format", Toast.LENGTH_LONG).show();
                Log.e(TAG, "createTableFromCsv: ",ex );
            }
            reader.close();

            StringBuilder sqlQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
            sqlQuery.append(TABLE_NAME).append(" (");
            for (int i = 0; i < columnNames.size(); i++) {
                sqlQuery.append(COLUMN_NAME).append(" ").append(columnTypes.get(i));
                if (i < columnNames.size() - 1) {
                    sqlQuery.append(", ");
                }
            }
            sqlQuery.append(")");

            db.execSQL(sqlQuery.toString());
            Toast.makeText(context, "Successfully imported csv to local DB", Toast.LENGTH_LONG).show();
            Log.i(TAG, "createTableFromCsv: Successfully created table");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}
