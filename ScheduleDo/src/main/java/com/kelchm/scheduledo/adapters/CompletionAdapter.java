package com.kelchm.scheduledo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kelchm on 11/17/13.
 */
public class CompletionAdapter extends ArrayAdapter<Double> {

    private Context context;
    private LayoutInflater mInflater;
    private int mResource;
    private int mDropDownResource;


    public CompletionAdapter(Context context, int resource, List<Double> objects) {
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
        int item = (int) (getItem(position) * 100);

        text.setText(String.valueOf(item) + "%");

        return view;
    }
}
