package com.mvp.fonts.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.mvp.fonts.MultiTask.TasksManager;
import com.mvp.fonts.R;
import com.mvp.fonts.dataModel.Font;
import com.mvp.fonts.dataModel.RecyclerBaseItem;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FontRecyclerAdapter extends RecyclerBaseAdapter<RecyclerBaseItem> {
    public static final int LAYOUT_TYPE_ITEM = 1;
    private Resources mResources;

    public FontRecyclerAdapter(Context context, List<RecyclerBaseItem> list) {
        super(context, list);
        mResources = context.getResources();
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder ret;
        View view;
        int layoutResId;
        layoutResId = R.layout.recycler_item_font;
        view = mLayoutInflater.inflate(layoutResId, null);
        ret = new FontViewHolder(view);
        return ret;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Font font = TasksManager.getImpl(mContext).get(position);
        ((FontViewHolder) holder).setItem(font);

        ((FontViewHolder) holder).update(font.getID(), position);
        ((FontViewHolder) holder).mBtnTaskAction.setTag(holder);
        ((FontViewHolder) holder).mTvTaskName.setText(font.getName());

        TasksManager.getImpl(mContext)
                .updateViewHolder(((FontViewHolder) holder).mId, ((FontViewHolder) holder));

        ((FontViewHolder) holder).mBtnTaskAction.setEnabled(true);


        if (TasksManager.getImpl(mContext).isReady()) {
            final int status = TasksManager.getImpl(mContext).getStatus(font.getID(), font.getPath());
            if (status == FileDownloadStatus.pending || status == FileDownloadStatus.started ||
                    status == FileDownloadStatus.connected) {
                // start task, but file not created yet
                ((FontViewHolder) holder).updateDownloading(status, TasksManager.getImpl(mContext).getSoFar(font.getID())
                        , TasksManager.getImpl(mContext).getTotal(font.getID()));
            } else if (!new File(font.getPath()).exists() &&
                    !new File(FileDownloadUtils.getTempPath(font.getPath())).exists()) {
                // not exist file
                ((FontViewHolder) holder).updateNotDownloaded(status, 0, 0);
            } else if (TasksManager.getImpl(mContext).isDownloaded(status)) {
                // already downloaded and exist
                ((FontViewHolder) holder).updateDownloaded();
            } else if (status == FileDownloadStatus.progress) {
                // downloading
                ((FontViewHolder) holder).updateDownloading(status, TasksManager.getImpl(mContext).getSoFar(font.getID())
                        , TasksManager.getImpl(mContext).getTotal(font.getID()));
            } else {
                // not start
                ((FontViewHolder) holder).updateNotDownloaded(status, TasksManager.getImpl(mContext).getSoFar(font.getID())
                        , TasksManager.getImpl(mContext).getTotal(font.getID()));
            }
        } else {
            ((FontViewHolder) holder).mTvTaskStatus.setText(R.string.tasks_manager_demo_status_loading);
            ((FontViewHolder) holder).mBtnTaskAction.setEnabled(false);
        }
    }

    public class FontViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvFontInfo;

        private TextView mTvTaskName;
        private TextView mTvTaskStatus;
        private ProgressBar mPbTask;
        private Button mBtnTaskAction;
        private int mPosition;
        private int mId;

        private void update(final int id, final int position) {
            mId = id;
            mPosition = position;
        }


        private void updateDownloaded() {
            mPbTask.setMax(1);
            mPbTask.setProgress(1);

            mTvTaskStatus.setText(R.string.tasks_manager_demo_status_completed);
            mBtnTaskAction.setText(R.string.delete);
        }

        FontViewHolder(View itemView) {
            super(itemView);
            bindViews();
            bindEvents();
        }

        private void bindViews() {
            mTvFontInfo = itemView.findViewById(R.id.tvFontInfo);
            mTvTaskName = itemView.findViewById(R.id.tvTaskName);
            mTvTaskStatus = itemView.findViewById(R.id.tvTaskStatus);
            mPbTask = itemView.findViewById(R.id.pbTask);
            mBtnTaskAction = itemView.findViewById(R.id.btnTaskAction);
        }

        private void bindEvents() {
            mBtnTaskAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() == null) {
                        return;
                    }

                    FontViewHolder holder = (FontViewHolder) v.getTag();

                    CharSequence action = ((TextView) v).getText();
                    if (action.equals(v.getResources().getString(R.string.pause))) {
                        // to pause
                        FileDownloader.getImpl().pause(holder.mId);
                    } else if (action.equals(v.getResources().getString(R.string.start))) {
                        // to start
                        final Font font = TasksManager.getImpl(mContext).get(holder.mPosition);
                        final BaseDownloadTask task = FileDownloader.getImpl().create(font.getUrl())
                                .setPath(font.getPath())
                                .setCallbackProgressTimes(100)
                                .setListener(new FileDownloadSampleListener() {

                                    private FontViewHolder checkCurrentHolder(final BaseDownloadTask task) {
                                        final FontViewHolder tag = (FontViewHolder) task.getTag();
                                        if (tag.mId != task.getId()) {
                                            return null;
                                        }

                                        return tag;
                                    }

                                    @Override
                                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                        super.pending(task, soFarBytes, totalBytes);
                                        final FontViewHolder tag = checkCurrentHolder(task);
                                        if (tag == null) {
                                            return;
                                        }

                                        tag.updateDownloading(FileDownloadStatus.pending, soFarBytes
                                                , totalBytes);
                                        tag.mTvTaskStatus.setText(R.string.tasks_manager_demo_status_pending);
                                    }

                                    @Override
                                    protected void started(BaseDownloadTask task) {
                                        super.started(task);
                                        final FontViewHolder tag = checkCurrentHolder(task);
                                        if (tag == null) {
                                            return;
                                        }

                                        tag.mTvTaskStatus.setText(R.string.tasks_manager_demo_status_started);
                                    }

                                    @Override
                                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                                        final FontViewHolder tag = checkCurrentHolder(task);
                                        if (tag == null) {
                                            return;
                                        }

                                        tag.updateDownloading(FileDownloadStatus.connected, soFarBytes
                                                , totalBytes);
                                        tag.mTvTaskStatus.setText(R.string.tasks_manager_demo_status_connected);
                                    }

                                    @Override
                                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                        super.progress(task, soFarBytes, totalBytes);
                                        final FontViewHolder tag = checkCurrentHolder(task);
                                        if (tag == null) {
                                            return;
                                        }

                                        tag.updateDownloading(FileDownloadStatus.progress, soFarBytes
                                                , totalBytes);
                                    }

                                    @Override
                                    protected void error(BaseDownloadTask task, Throwable e) {
                                        super.error(task, e);
                                        final FontViewHolder tag = checkCurrentHolder(task);
                                        if (tag == null) {
                                            return;
                                        }

                                        tag.updateNotDownloaded(FileDownloadStatus.error, task.getLargeFileSoFarBytes()
                                                , task.getLargeFileTotalBytes());
                                        TasksManager.getImpl(mContext).removeTaskForViewHolder(task.getId());
                                    }

                                    @Override
                                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                        super.paused(task, soFarBytes, totalBytes);
                                        final FontViewHolder tag = checkCurrentHolder(task);
                                        if (tag == null) {
                                            return;
                                        }

                                        tag.updateNotDownloaded(FileDownloadStatus.paused, soFarBytes, totalBytes);
                                        tag.mTvTaskStatus.setText(R.string.tasks_manager_demo_status_paused);
                                        TasksManager.getImpl(mContext).removeTaskForViewHolder(task.getId());
                                    }

                                    @Override
                                    protected void completed(BaseDownloadTask task) {
                                        super.completed(task);
                                        final FontViewHolder tag = checkCurrentHolder(task);
                                        if (tag == null) {
                                            return;
                                        }

                                        tag.updateDownloaded();
                                        TasksManager.getImpl(mContext).removeTaskForViewHolder(task.getId());
                                    }
                                });

                        TasksManager.getImpl(mContext)
                                .addTaskForViewHolder(task);

                        TasksManager.getImpl(mContext)
                                .updateViewHolder(holder.mId, holder);

                        task.start();
                    } else if (action.equals(v.getResources().getString(R.string.delete))) {
                        // to delete
                        new File(TasksManager.getImpl(mContext).get(holder.mPosition).getPath()).delete();
                        holder.mBtnTaskAction.setEnabled(true);
                        holder.updateNotDownloaded(FileDownloadStatus.INVALID_STATUS, 0, 0);
                    }
                }
            });
        }

        private void setItem(Font font) {
            StringBuilder subsetsBuilder = new StringBuilder();

            if (font.getSubsets() != null) {
                for (String string : font.getSubsets()) {
                    subsetsBuilder.append(string).append("\n");
                }
            }

            String info = mResources.getString(R.string.category) + font.getCategory() + "\n\n" +
                    mResources.getString(R.string.kind) + font.getKind() + "\n\n" +
                    mResources.getString(R.string.family) + font.getFamily() + "\n\n" +
                    mResources.getString(R.string.version) + font.getVersion() + "\n\n" +
                    mResources.getString(R.string.last_modified) + font.getLastModified() + "\n\n" +
                    mResources.getString(R.string.subsets) + "\n" + font.getSubsetsString();

            mTvFontInfo.setText(info);
        }


        private void updateNotDownloaded(final int status, final long sofar, final long total) {
            if (sofar > 0 && total > 0) {
                final float percent = sofar
                        / (float) total;
                mPbTask.setMax(100);
                mPbTask.setProgress((int) (percent * 100));
            } else {
                mPbTask.setMax(1);
                mPbTask.setProgress(0);
            }

            switch (status) {
                case FileDownloadStatus.error:
                    mTvTaskStatus.setText(R.string.tasks_manager_demo_status_error);
                    break;
                case FileDownloadStatus.paused:
                    mTvTaskStatus.setText(R.string.tasks_manager_demo_status_paused);
                    break;
                default:
                    mTvTaskStatus.setText(R.string.tasks_manager_demo_status_not_downloaded);
                    break;
            }
            mBtnTaskAction.setText(R.string.start);
        }

        private void updateDownloading(final int status, final long sofar, final long total) {
            final float percent = sofar
                    / (float) total;
            mPbTask.setMax(100);
            mPbTask.setProgress((int) (percent * 100));

            switch (status) {
                case FileDownloadStatus.pending:
                    mTvTaskStatus.setText(R.string.tasks_manager_demo_status_pending);
                    break;
                case FileDownloadStatus.started:
                    mTvTaskStatus.setText(R.string.tasks_manager_demo_status_started);
                    break;
                case FileDownloadStatus.connected:
                    mTvTaskStatus.setText(R.string.tasks_manager_demo_status_connected);
                    break;
                case FileDownloadStatus.progress:
                    mTvTaskStatus.setText(R.string.tasks_manager_demo_status_progress);
                    break;
                default:
                    mTvTaskStatus.setText(mResources.getString(
                            R.string.tasks_manager_demo_status_downloading, status));
                    break;
            }

            mBtnTaskAction.setText(R.string.pause);
        }
    }
}
