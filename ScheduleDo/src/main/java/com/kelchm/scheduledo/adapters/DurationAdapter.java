package com.kelchm.scheduledo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kelchm.scheduledo.R;

import java.util.List;

/**
 * Created by kelchm on 11/17/13.
 */
public class DurationAdapter extends ArrayAdapter<Long> {

    private Context context;
    private LayoutInflater mInflater;
    private int mResource;
    private int mDropDownResource;


    public DurationAdapter(Context context, int resource, List<Long> objects) {
        super(context, resource, objects);
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = mDropDownResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mDropDownResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
        View view;
        TextView text;

        if (convertView == null)
        {
            view = mInflater.inflate(resource, parent, false);
        }
        else
        {
            view = convertView;
        }

        text = (TextView) view;
        int minutes = (int) (getItem(position) / 60 / 1000);

        text.setText(String.valueOf(minutes) + " " + context.getString(R.string.text_minutes));

        if (minutes >= 60)
        {
            int hours = minutes / 60;
            text.setText(String.valueOf(hours) + " " + context.getString(R.string.text_hours));
        }

        return view;
    }
}
