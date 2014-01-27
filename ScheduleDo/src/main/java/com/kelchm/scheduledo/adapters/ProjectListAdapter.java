package com.kelchm.scheduledo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kelchm.scheduledo.R;
import com.kelchm.scheduledo.models.task.Project;

import java.util.List;

/**
 * Created by kelchm on 10/26/13.
 */
public class ProjectListAdapter extends BaseAdapter {

    private List<Project> projects;
    private LayoutInflater inflater;

    public ProjectListAdapter(Context c, List<Project> projects) {
        inflater = LayoutInflater.from(c);
        this.projects = projects;
    }

    @Override
    public int getCount() {
        if (projects != null)
        {
            return projects.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return projects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null)
        {
            v = inflater.inflate(R.layout.list_item_project, parent, false);
        }

        Project project = (Project) getItem(position);

        if (project != null)
        {
            TextView projectName = (TextView) v.findViewById(R.id.project_name);
            TextView projectId = (TextView) v.findViewById(R.id.project_id);
            TextView projectDueDate = (TextView) v.findViewById(R.id.project_date);
            TextView projectDueTime = (TextView) v.findViewById(R.id.project_time);
            TextView projectActionCount = (TextView) v.findViewById(R.id.project_action_count);

            if (projectName != null)
            {
                projectName.setText(project.getName());
            }
            if (projectId != null)
            {
                projectId.setText(String.valueOf(project.getId()));
            }
            if (projectDueDate != null)
            {
                if (project.getDueDate() != null)
                {
                    projectDueDate.setText(project.getDueDate().toString("MMM d"));
                }
            }
            if (projectDueTime != null)
            {
                if (project.getDueDate() != null)
                {
                    projectDueTime.setText(project.getDueDate().toString("h:mm a"));
                }
            }
            if (projectActionCount != null)
            {
                projectActionCount.setText(String.valueOf(project.getNumActions()));
            }
        }

        return v;
    }
}
