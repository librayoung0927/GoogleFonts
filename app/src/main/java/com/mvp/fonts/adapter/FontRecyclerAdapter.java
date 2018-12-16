package com.mvp.fonts.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mvp.fonts.R;
import com.mvp.fonts.dataModel.Font;
import com.mvp.fonts.dataModel.RecyclerBaseItem;

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
        ((FontViewHolder) holder).setItem((Font) getItem(position));
    }

    private class FontViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvFontInfo;

        FontViewHolder(View itemView) {
            super(itemView);
            bindViews();
        }

        private void bindViews() {
            mTvFontInfo = itemView.findViewById(R.id.tvFontInfo);
        }


        private void setItem(Font font) {
            String info = mResources.getString(R.string.category) + font.getCategory() + "\n\n" +
                    mResources.getString(R.string.kind) + font.getKind() + "\n\n" +
                    mResources.getString(R.string.family) + font.getFamily() + "\n\n" +
                    mResources.getString(R.string.version) + font.getVersion() + "\n\n" +
                    mResources.getString(R.string.last_modified) + font.getLastModified() + "\n\n";

            mTvFontInfo.setText(info);
        }
    }
}
