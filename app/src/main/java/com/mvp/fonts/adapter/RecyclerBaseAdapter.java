package com.mvp.fonts.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.mvp.fonts.dataModel.RecyclerBaseItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerBaseAdapter<T extends RecyclerBaseItem> extends RecyclerView.Adapter{
   private static final String TAG = RecyclerBaseAdapter.class.getSimpleName();
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mList;

    AdapterView.OnItemClickListener onItemClickListener;

    public RecyclerBaseAdapter(Context mContext, List<T> list) {
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        setList(list);
    }

    public void remove(int position) {
        if(mList != null) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(T item, int position) {
        if(mList == null)
            mList = new ArrayList<>();
        mList.add(position, item);
        notifyItemInserted(position);
    }

    public void setList(List<T> list){
        Log.d(TAG, "setList");

        if(this.mList == null)
            this.mList = new ArrayList<>();
        else {
            this.mList.clear();
            notifyDataSetChanged();
        }

        if(list != null) {
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public List<T> getList(){
        return mList;
    }

    public void addAll(List<T> list){
        if(list != null) {
            int orgItemCount = getItemCount();
            int extraItemCount = list != null ? list.size() : 0;
            if (this.mList == null)
                this.mList = new ArrayList<>();

            this.mList.addAll(list);
            notifyItemRangeInserted(orgItemCount, extraItemCount);
        }
    }


    public void clear(){
        int orgItemCount = getItemCount();

        this.mList.clear();
        if(orgItemCount > 0)
            notifyItemRangeRemoved(0, orgItemCount);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        onItemClickListener = listener;
    }



    public T getItem(int position){
        return (mList != null && position > -1 && position < mList.size())? mList.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return mList != null? mList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, String.format("getItemViewType :%d", position));
        return getItemCount() > 0 && position >= 0 && position < getItemCount()? getItem(position).getLayoutType() : -1;
    }

    public abstract RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position);

}
