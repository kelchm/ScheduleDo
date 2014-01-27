package com.kelchm.scheduledo.fragments;


import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kelchm.scheduledo.R;
import com.kelchm.scheduledo.drawables.ColorAnimationDrawable;

/**
 * Created by kelchm on 10/22/13.
 */
public class ExperimentsFragment extends Fragment implements View.OnClickListener {

    Button button, someButton, maxButton, resetButton;
    ColorAnimationDrawable animatedActionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_experiments, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        button = (Button) getView().findViewById(R.id.button_none);
        button.setOnClickListener(this);

        someButton = (Button) getView().findViewById(R.id.button_some);
        someButton.setOnClickListener(this);

        maxButton = (Button) getView().findViewById(R.id.button_max);
        maxButton.setOnClickListener(this);

        resetButton = (Button) getView().findViewById(R.id.button_reset);
        resetButton.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        animatedActionBar = new ColorAnimationDrawable();
        getActivity().getActionBar().setBackgroundDrawable(animatedActionBar);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button_none:
                animatedActionBar.start(0x00A1DD9A);
                break;
            case R.id.button_some:
                animatedActionBar.start(0x00DDD09A);
                break;
            case R.id.button_max:
                animatedActionBar.start(0x00DD9A9A);
                break;
            case R.id.button_reset:
                animatedActionBar.start(0x00DDDDDD);
                break;
        }
    }
}
