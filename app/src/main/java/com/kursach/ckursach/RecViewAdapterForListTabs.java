package com.kursach.ckursach;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kursach.ckursach.placeholder.CheckBoxItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecViewAdapterForListTabs extends RecyclerView.Adapter<RecViewAdapterForListTabs.ViewHolder> {

ArrayList<CheckBoxItem>list;
static int actionCode;
private Callback callback;
public interface Callback {
    void onItemClicked(int position, CheckBoxItem item);
}




    public RecViewAdapterForListTabs(ArrayList<CheckBoxItem> list, int actionCode, Callback callback) {
    this.callback = callback;
        this.list = list;
        RecViewAdapterForListTabs.actionCode = actionCode;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView;

switch (actionCode){
    case Consts.DELETE_ACTION: itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.check_boxes_item, parent,false);break;
    case Consts.DOWNLOAD_ACTION:itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.check_boxes_item, parent,false); break;
    default: itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.csv_name_item, parent,false); break;
}

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CheckBoxItem item = list.get(position);
        if (actionCode != Consts.LIST_ACTION) {
            holder.checkBox.setText(String.valueOf(position));
            holder.checkBox.setChecked(item.isChecked());
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setChecked(isChecked);
                    callback.onItemClicked(holder.getAbsoluteAdapterPosition(), item);
                }
            });
        }else {
            holder.mIdView.setText(String.valueOf(position));
        }

        holder.mContentView.setText(item.getFileName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public  TextView mIdView = null;
        public CheckBox checkBox = null;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            if (actionCode != Consts.LIST_ACTION) {
                checkBox = view.findViewById(R.id.checkboxId);
            }else {
                mIdView = view.findViewById(R.id.csvId);
            }
            mContentView = view.findViewById(R.id.csvName);
        }

        @Override
        public @NotNull String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }



}