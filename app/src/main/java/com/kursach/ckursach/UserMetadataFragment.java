package com.kursach.ckursach;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.button.MaterialButton;
import com.kursach.ckursach.placeholder.PlaceholderContent;

/**
 * A fragment representing a list of Items.
 */
public class UserMetadataFragment extends DialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserMetadataFragment() {
    }

    // TODO: Customize parameter initialization

    public static UserMetadataFragment newInstance(int columnCount) {
        UserMetadataFragment fragment = new UserMetadataFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_metadata_list, container, false);

        // Set the adapter
        MaterialButton  closeButton = view.findViewById(R.id.closeDialogBtn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
            Context context = view.getContext();
            RecyclerView recyclerView = view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new UserMetadataRecyclerViewAdapter(PlaceholderContent.ITEMS));

        return view;
    }
}