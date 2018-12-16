package com.mvp.fonts.MultiTask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mvp.fonts.dataModel.Font;

public class TasksManagerDBOpenHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "fonts.db";
    public final static int DATABASE_VERSION = 2;

    public TasksManagerDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TasksManagerDBController.TABLE_NAME
                + String.format(
                "("
                        + "%s INTEGER PRIMARY KEY, " // id, download id
                        + "%s VARCHAR, " // name
                        + "%s VARCHAR, " // url
                        + "%s VARCHAR, " // path
                        + "%s VARCHAR, " // kind
                        + "%s VARCHAR, " // family
                        + "%s VARCHAR, " // category
                        + "%s VARCHAR, " // version
                        + "%s VARCHAR, " // lastModified
                        + "%s VARCHAR " // subsets
                        + ")"
                , Font.ID
                , Font.NAME
                , Font.URL
                , Font.PATH
                , Font.KIND
                , Font.FAMILY
                , Font.CATEGORY
                , Font.VERSION
                , Font.LAST_MODIFIED
                , Font.SUBSETS
        ));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.delete(TasksManagerDBController.TABLE_NAME, null, null);
        }
    }

}
