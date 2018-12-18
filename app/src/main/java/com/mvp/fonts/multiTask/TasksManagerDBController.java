package com.mvp.fonts.multiTask;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.mvp.fonts.R;
import com.mvp.fonts.dataModel.Font;

import java.util.ArrayList;
import java.util.List;

public class TasksManagerDBController {

    public final static String TABLE_NAME = "fonts";
    private Context mContext;
    private final SQLiteDatabase db;

    public TasksManagerDBController(Context context) {
        mContext = context;
        TasksManagerDBOpenHelper openHelper = new TasksManagerDBOpenHelper(context);

        db = openHelper.getWritableDatabase();
    }

    public List<Font> getAllTasks(String sort) {
        String sqlString;

        if (TextUtils.isEmpty(sort)) {
            sqlString = "SELECT * FROM " + TABLE_NAME;
        } else {
            sqlString = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + sort;
        }

        final Cursor c = db.rawQuery(sqlString, null);

        final List<Font> list = new ArrayList<>();
        try {
            if (!c.moveToLast()) {
                return list;
            }

            do {
                Font font = new Font();
                font.setID(c.getInt(c.getColumnIndex(Font.ID)));
                font.setName(c.getString(c.getColumnIndex(Font.NAME)));
                font.setUrl(c.getString(c.getColumnIndex(Font.URL)));
                font.setPath(c.getString(c.getColumnIndex(Font.PATH)));
                font.setKind(c.getString(c.getColumnIndex(Font.KIND)));
                font.setFamily(c.getString(c.getColumnIndex(Font.FAMILY)));
                font.setCategory(c.getString(c.getColumnIndex(Font.CATEGORY)));
                font.setVersion(c.getString(c.getColumnIndex(Font.VERSION)));
                font.setLastModified(c.getString(c.getColumnIndex(Font.LAST_MODIFIED)));
                font.setSubsetsString(c.getString(c.getColumnIndex(Font.SUBSETS)));
                list.add(font);
            } while (c.moveToPrevious());
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return list;
    }

    public Font addTask(final String url, final String path, String kind, String family, String category,
                        String version, String lastModified, String subsetsString) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null;
        }

        // have to use FileDownloadUtils.generateId to associate TasksManagerModel with FileDownloader
        final int id = FileDownloadUtils.generateId(url, path);

        Font font = new Font();
        font.setID(id);
        font.setName(mContext.getString(R.string.tasks_manager_demo_name, id));
        font.setUrl(url);
        font.setPath(path);
        font.setKind(kind);
        font.setFamily(family);
        font.setCategory(category);
        font.setVersion(version);
        font.setLastModified(lastModified);
        font.setSubsetsString(subsetsString);

        final boolean succeed = db.insert(TABLE_NAME, null, font.toContentValues()) != -1;
        return succeed ? font : null;
    }

}
