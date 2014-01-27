package com.kelchm.scheduledo.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.kelchm.scheduledo.adapters.ProjectListAdapter;
import com.kelchm.scheduledo.fragments.InboxFragment;
import com.kelchm.scheduledo.models.task.Project;
import com.kelchm.scheduledo.models.task.ProjectDueDateComparator;

import java.util.Collections;
import java.util.List;

/**
 * Created by kelchm on 10/26/13.
 */
public class InboxProcessor extends AsyncTask<InboxFragment, Integer, List<Project>> {
    private static final String TAG = InboxProcessor.class.getName();

    InboxFragment inbox;

    @Override
    protected List<Project> doInBackground(InboxFragment... params) {

        Log.i(TAG, "Beginning processing projects for inbox");

        // Grab InboxFragment parameter
        this.inbox = params[0];

        // Setup repository for accessing projects
        TasksRepository repository = new TasksRepository(inbox.getActivity());

        // Get projects and sort by due date
        List<Project> projects = repository.getAllProjects();
        Collections.sort(projects, new ProjectDueDateComparator());

        return projects;
    }

    @Override
    protected void onPostExecute(List<Project> projects) {
        Log.i(TAG, "Done processing projects for inbox");
        ProjectListAdapter adapter = new ProjectListAdapter(inbox.getContext(), projects);
        inbox.setListAdapter(adapter);
    }

}
