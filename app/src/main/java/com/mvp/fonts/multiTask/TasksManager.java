package com.mvp.fonts.multiTask;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.mvp.fonts.adapter.FontRecyclerAdapter;
import com.mvp.fonts.dataModel.Font;
import com.mvp.fonts.ui.MainActivity;

import java.lang.ref.WeakReference;
import java.util.List;

public class TasksManager {
    private static final String TAG = TasksManager.class.getSimpleName();
    private static Context mContext;

    private static class HolderClass {
        private static TasksManager INSTANCE
                = new TasksManager("");
    }

    public static TasksManager getImpl(Context context) {
        mContext = context;
        return HolderClass.INSTANCE;
    }

    public static void sortInstance(String sort) {
        HolderClass.INSTANCE = null;
        HolderClass.INSTANCE = new TasksManager(sort);
    }

    private TasksManagerDBController dbController;
    private List<Font> mFontList;

    private TasksManager(String sort) {
        dbController = new TasksManagerDBController(mContext);
        mFontList = dbController.getAllTasks(sort);
    }

    private SparseArray<BaseDownloadTask> taskSparseArray = new SparseArray<>();

    public void addTaskForViewHolder(final BaseDownloadTask task) {
        taskSparseArray.put(task.getId(), task);
    }

    public void removeTaskForViewHolder(final int id) {
        taskSparseArray.remove(id);
    }

    public void updateViewHolder(final int id, final FontRecyclerAdapter.FontViewHolder holder) {
        final BaseDownloadTask task = taskSparseArray.get(id);
        if (task == null) {
            return;
        }

        task.setTag(holder);
    }

    private void releaseTask() {
        taskSparseArray.clear();
    }

    private FileDownloadConnectListener listener;

    private void registerServiceConnectionListener(final WeakReference<MainActivity>
                                                           activityWeakReference) {
        if (listener != null) {
            FileDownloader.getImpl().removeServiceConnectListener(listener);
        }

        listener = new FileDownloadConnectListener() {

            @Override
            public void connected() {
                if (activityWeakReference == null
                        || activityWeakReference.get() == null) {
                    return;
                }

                activityWeakReference.get().postNotifyDataChanged();
            }

            @Override
            public void disconnected() {
                if (activityWeakReference == null
                        || activityWeakReference.get() == null) {
                    return;
                }

                activityWeakReference.get().postNotifyDataChanged();
            }
        };

        FileDownloader.getImpl().addServiceConnectListener(listener);
    }

    private void unregisterServiceConnectionListener() {
        FileDownloader.getImpl().removeServiceConnectListener(listener);
        listener = null;
    }

    public void onCreate(final WeakReference<MainActivity> activityWeakReference) {
        if (!FileDownloader.getImpl().isServiceConnected()) {
            FileDownloader.getImpl().bindService();
            registerServiceConnectionListener(activityWeakReference);
        }
    }

    public void onDestroy() {
        unregisterServiceConnectionListener();
        releaseTask();
    }

    public boolean isReady() {
        return FileDownloader.getImpl().isServiceConnected();
    }

    public Font get(final int position) {
        return mFontList.get(position);
    }

    private Font getById(final int id) {
        for (Font font : mFontList) {
            if (font.getID() == id) {
                return font;
            }
        }

        return null;
    }

    /**
     * @param status Download Status
     * @return has already downloaded
     * @see FileDownloadStatus
     */
    public boolean isDownloaded(final int status) {
        return status == FileDownloadStatus.completed;
    }

    public int getStatus(final int id, String path) {
        return FileDownloader.getImpl().getStatus(id, path);
    }

    public long getTotal(final int id) {
        return FileDownloader.getImpl().getTotal(id);
    }

    public long getSoFar(final int id) {
        return FileDownloader.getImpl().getSoFar(id);
    }

    public int getTaskCounts() {
        return mFontList.size();
    }

    public Font addTask(final String url, final String kind, final String family,
                        final String category, final String version, final String lastModified, final String subsetsString) {
        return addTask(url, createPath(url), kind, family, category, version, lastModified, subsetsString);
    }

    private Font addTask(final String url, final String path, final String kind, final String family,
                         final String category, final String version, final String lastModified,
                         final String subsetsString) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null;
        }

        final int id = FileDownloadUtils.generateId(url, path);
        Font font = getById(id);
        if (font != null) {
            return font;
        }
        final Font newFont = dbController.addTask(url, path, kind, family, category, version, lastModified, subsetsString);
        if (newFont != null) {
            mFontList.add(newFont);
        }

        return newFont;
    }

    private String createPath(final String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        return FileDownloadUtils.getDefaultSaveFilePath(url);
    }
}
