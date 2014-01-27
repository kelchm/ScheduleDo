package com.kelchm.scheduledo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kelchm.scheduledo.R;
import com.kelchm.scheduledo.models.task.Action;
import com.kelchm.scheduledo.models.task.Project;

import java.util.List;

/**
 * Created by kelchm on 10/26/13.
 */
public class ActionListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Project project;
    private Context context;

    public ActionListAdapter(Context c, Project project) {
        inflater = LayoutInflater.from(c);
        this.project = project;
        this.context = c;
    }

    @Override
    public int getCount() {
        return project.getActions().size();
    }

    @Override
    public Object getItem(int position) {
        return project.getActions().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
        //        return project.getActions().get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null)
        {
            v = inflater.inflate(R.layout.list_item_action, parent, false);
        }

        Action action = (Action) getItem(position);

        if (action != null)
        {
            TextView actionName = (TextView) v.findViewById(R.id.action_name);

            if (actionName != null)
            {
                actionName.setText(action.getName());
            }

        }

        return v;
    }

    public String getName(int position) {
        return project.getActions().get(position).getName();
    }

    public List<Action> getActions() {
        return project.getActions();
    }
}
