package com.mvp.fonts.ui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liulishuo.filedownloader.FileDownloadMonitor;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.mvp.fonts.multiTask.GlobalMonitor;
import com.mvp.fonts.multiTask.TasksManager;
import com.mvp.fonts.R;
import com.mvp.fonts.adapter.FontRecyclerAdapter;
import com.mvp.fonts.constants.Field;
import com.mvp.fonts.dataModel.Font;
import com.mvp.fonts.dataModel.RecyclerBaseItem;
import com.mvp.fonts.interfaces.OnAPIListener;
import com.mvp.fonts.module.apiParams.InParamsAllFonts;
import com.mvp.fonts.utils.APIUtils;
import com.mvp.fonts.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Gson mGson;
    private Context mContext;
    private Activity mActivity;
    private RecyclerView mRvFont;
    private List<RecyclerBaseItem> mFontList;
    private ProgressBar mProgressBar;
    private ProgressBar mProgressBarHorizontal;
    private TextView mTvCount;
    private FontRecyclerAdapter mFontRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bindsViews();
        bindEvents();
        pullGoogleAllFonts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sort_type, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 如果無database 版本可直接用此方法做排序
//            case R.id.sort_default:
//                break;
//
//            case R.id.sort_category:
//                Collections.sort(mFontList, new CategoryComparator());
//                break;
//
//            case R.id.sort_kind:
//                Collections.sort(mFontList, new KindComparator());
//                break;
//
//            case R.id.sort_family:
//                Collections.sort(mFontList, new FamilyComparator());
//                break;
//
//            case R.id.sort_version:
//                Collections.sort(mFontList, new VersionComparator());
//                break;
//
//            case R.id.sort_last_modified:
//                Collections.sort(mFontList, new LastModifiedComparator());
//                break;

            case R.id.sort_default:
                mFontRecyclerAdapter.setSort("");
                break;

            case R.id.sort_category:
                mFontRecyclerAdapter.setSort(Font.CATEGORY);
                break;

            case R.id.sort_kind:
                mFontRecyclerAdapter.setSort(Font.KIND);
                break;

            case R.id.sort_family:
                mFontRecyclerAdapter.setSort(Font.FAMILY);
                break;

            case R.id.sort_version:
                mFontRecyclerAdapter.setSort(Font.VERSION);
                break;

            case R.id.sort_last_modified:
                mFontRecyclerAdapter.setSort(Font.LAST_MODIFIED);
                break;
        }

        setAdapter();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        TasksManager.getImpl(mContext).onDestroy();
        mFontRecyclerAdapter = null;
        FileDownloader.getImpl().pauseAll();
        FileDownloader.getImpl().unBindServiceIfIdle();
        FileDownloadMonitor.releaseGlobalMonitor();
        super.onDestroy();
    }

    private void init() {
        mContext = getApplicationContext();
        FileDownloader.setupOnApplicationOnCreate(getApplication())
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000)
                        // set read timeout.
                ))
                .commit();


        FileDownloadMonitor.setGlobalMonitor(GlobalMonitor.getImpl());
        mActivity = this;
        mFontList = new ArrayList<>();
        mGson = new Gson();
        TasksManager.getImpl(mContext).onCreate(new WeakReference<>(this));
    }

    private void bindsViews() {
        mRvFont = findViewById(R.id.rvFont);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBarHorizontal = findViewById(R.id.progressbarHorizontal);
        mTvCount = findViewById(R.id.tvCount);
    }

    private void bindEvents() {
        mRvFont.setLayoutManager(new LinearLayoutManager(mActivity));
        mRvFont.setHasFixedSize(true);
        mRvFont.setItemViewCacheSize(20);
        mRvFont.setDrawingCacheEnabled(true);
        mRvFont.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    private void setAdapter() {
        if (mFontRecyclerAdapter == null) {
            mFontRecyclerAdapter = new FontRecyclerAdapter(mContext, mFontList);
            mRvFont.setAdapter(mFontRecyclerAdapter);
        } else {
            mFontRecyclerAdapter.clear();
            mFontRecyclerAdapter.setList(mFontList);
        }
    }

    private static class InsertIntoDatabase extends AsyncTask<Void, Void, Void> {
        private WeakReference<MainActivity> mActivityWeakReference;

        private InsertIntoDatabase(MainActivity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Activity activity = mActivityWeakReference.get();
            ((MainActivity) activity).mProgressBarHorizontal.setVisibility(View.INVISIBLE);
            ((MainActivity) activity).mProgressBar.setVisibility(View.INVISIBLE);
            ((MainActivity) activity).mTvCount.setVisibility(View.INVISIBLE);
            ((MainActivity) activity).setAdapter();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Activity activity = mActivityWeakReference.get();
            if (((MainActivity) activity).mFontList.size() > 0) {
                int size = ((MainActivity) activity).mFontList.size();

                for (int i = 0; i < size; i++) {
                    Font font = (Font) ((MainActivity) activity).mFontList.get(i);
                    HashMap.Entry<String, String> entry = font.getFiles().entrySet().iterator().next();
                    String url = entry.getValue();

                    StringBuilder subsetsBuilder = new StringBuilder();

                    if (font.getSubsets() != null) {
                        for (String string : font.getSubsets()) {
                            subsetsBuilder.append(string).append("\n");
                        }
                    }

                    TasksManager.getImpl(((MainActivity) activity).mContext).addTask(url, font.getKind(), font.getFamily(),
                            font.getCategory(), font.getVersion(), font.getLastModified(), subsetsBuilder.toString());

                    ((MainActivity) activity).updateProgress(i, size);
                    LogUtils.d(TAG, " progress : " + String.valueOf(i));
                }
            }
            return null;
        }
    }

    private void updateProgress(final int i, final int size) {
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mProgressBarHorizontal.setProgress(i);
                mTvCount.setText(String.format(getString(R.string.count), i, size));
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void pullGoogleAllFonts() {
        InParamsAllFonts ip = new InParamsAllFonts();
        APIUtils.request(mActivity, ip, new OnAPIListener() {
            @Override
            public void onError(int statusCode, String error) {
            }

            @Override
            public void onPreExecute() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHorizontal.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        mTvCount.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray fontJsonArray = object.getJSONArray(Field.ITEMS);

                        // map<String,String> files 另外處理
                        Type fileType = new TypeToken<HashMap<String, String>>() {
                        }.getType();
                        List<HashMap<String, String>> fileList = new ArrayList<>();

                        int size = fontJsonArray.length();
                        for (int i = 0; i < size; i++) {
                            fileList.add((HashMap<String, String>) mGson.fromJson(fontJsonArray.getJSONObject(i).getJSONObject("files").toString(), fileType));
                        }

                        Type listType = new TypeToken<List<Font>>() {
                        }.getType();

                        mFontList = mGson.fromJson(fontJsonArray.toString(), listType);
                        mProgressBarHorizontal.setMax(size);
                        for (int i = 0; i < size; i++) {
                            ((Font) mFontList.get(i)).setFiles(fileList.get(i));
                        }

                        new InsertIntoDatabase(((MainActivity) mActivity)).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                    } catch (JSONException e) {
                        LogUtils.e(TAG, e.toString());
                    }

                } else {
                    LogUtils.d(TAG, " onResponse Exception:");
                }
            }
        });
    }

    public void postNotifyDataChanged() {
        if (mFontRecyclerAdapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mFontRecyclerAdapter != null) {
                        mFontRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
