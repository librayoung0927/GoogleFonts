package com.mvp.fonts.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mvp.fonts.Comparator.CategoryComparator;
import com.mvp.fonts.Comparator.FamilyComparator;
import com.mvp.fonts.Comparator.KindComparator;
import com.mvp.fonts.Comparator.LastModifiedComparator;
import com.mvp.fonts.Comparator.VersionComparator;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Gson mGson;
    private Activity mActivity;
    private RecyclerView mRvFont;
    private List<RecyclerBaseItem> mFontList;
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
            case R.id.sort_default:
                break;

            case R.id.sort_category:
                Collections.sort(mFontList, new CategoryComparator());
                break;

            case R.id.sort_kind:
                Collections.sort(mFontList, new KindComparator());
                break;

            case R.id.sort_family:
                Collections.sort(mFontList, new FamilyComparator());
                break;

            case R.id.sort_version:
                Collections.sort(mFontList, new VersionComparator());
                break;

            case R.id.sort_last_modified:
                Collections.sort(mFontList, new LastModifiedComparator());
                break;
        }

        setAdapter();
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        mActivity = this;
        mFontList = new ArrayList<>();
        mGson = new Gson();
    }

    private void bindsViews() {
        mRvFont = findViewById(R.id.rvFont);
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
            mFontRecyclerAdapter = new FontRecyclerAdapter(mActivity, mFontList);
            mRvFont.setAdapter(mFontRecyclerAdapter);
        } else {
            mFontRecyclerAdapter.setList(mFontList);
        }
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

                        for (int i = 0; i < size; i++) {
                            ((Font) mFontList.get(i)).setFiles(fileList.get(i));
                        }

                        setAdapter();
                    } catch (JSONException e) {
                        LogUtils.e(TAG, e.toString());
                    }

                } else {
                    LogUtils.d(TAG, " onResponse Exception:");
                }
            }
        });
    }
}
