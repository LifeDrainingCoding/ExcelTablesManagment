package com.kursach.ckursach;

import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.*;
import com.kursach.ckursach.placeholder.PlaceholderContent.PlaceholderItem;
import com.kursach.ckursach.databinding.FragmentUserMetadataBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecViewAdapterForListTabs extends RecyclerView.Adapter<RecViewAdapterForListTabs.ViewHolder> {

ArrayList<String>list;


private static final String TAG = "RecViewAdapterForListTabs";
    public RecViewAdapterForListTabs(ArrayList<String> list) {
     this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
    View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.csv_name_item, parent,false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdView.setText(String.valueOf(position));
        holder.mContentView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);

            mIdView = view.findViewById(R.id.csvId);
            mContentView = view.findViewById(R.id.csvName);
        }

        @Override
        public @NotNull String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}