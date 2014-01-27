package com.kelchm.scheduledo.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kelchm.scheduledo.R;
import com.kelchm.scheduledo.activity.EditorActivity;
import com.kelchm.scheduledo.helpers.InboxProcessor;
import com.kelchm.scheduledo.models.task.Project;

/**
 * Created by kelchm on 10/22/13.
 */
public class InboxFragment extends ListFragment {

    public InboxFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        new InboxProcessor().execute(this);

        return view;
    }

    public Context getContext() {
        return this.getActivity().getApplicationContext();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i(InboxFragment.class.getName(), "clicked: " + id);
        Project selectedProject = (Project) l.getItemAtPosition(position);

        Intent i = new Intent(this.getActivity(), EditorActivity.class);
        i.putExtra("id", selectedProject.getId());

        startActivity(i);
    }

    @Override
    public void onResume() {
        Log.i(InboxFragment.class.getName(), "resuming");

        // Update the inbox list
        new InboxProcessor().execute(this);
        super.onResume();
    }

}
