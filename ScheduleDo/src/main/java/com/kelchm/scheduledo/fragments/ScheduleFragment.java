package com.kelchm.scheduledo.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.kelchm.scheduledo.R;
import com.kelchm.scheduledo.activity.MainActivity;
import com.kelchm.scheduledo.adapters.ScheduleListAdapter;
import com.kelchm.scheduledo.helpers.ScheduleProcessor;
import com.kelchm.scheduledo.models.schedule.ActionEvent;
import com.kelchm.scheduledo.models.task.Action;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by kelchm on 10/22/13.
 */
public class ScheduleFragment extends Fragment {
    private StickyListHeadersListView scheduleList;
    private static final int MENU_ENABLE_LOCATION = Menu.FIRST;
    private static final int MENU_DISABLE_LOCATION = Menu.FIRST + 1;
    private static final int MENU_REFRESH = Menu.FIRST + 2;
    private Menu menu;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    public static final int DIALOG_FRAGMENT = 1;

    public ScheduleFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferencesEditor = sharedPreferences.edit();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onResume() {
        //TODO: Find a way to prevent unnecesary calls to processSchedule
        processSchedule();

        super.onResume();
    }

    public Context getContext() {
        return this.getActivity().getApplicationContext();
    }

    public void setListAdapter(final ScheduleListAdapter adapter) {
        scheduleList = (StickyListHeadersListView) getView().findViewById(R.id.list);
        scheduleList.setAdapter(adapter);

        scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                Log.i("foo", "click! " + position);
                if (adapter.getItem(position) instanceof ActionEvent)
                {
                    Action action = ((ActionEvent) adapter.getItem(position)).getAction();
                    EditActionDialog editActionDialog = EditActionDialog.newEditInstance(action);
                    editActionDialog.setTargetFragment(ScheduleFragment.this, DIALOG_FRAGMENT);
                    editActionDialog.show(getFragmentManager().beginTransaction(), "dialog");
                }
                else
                {
                    Toast.makeText(getContext(), R.string.toast_calendar_event, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;

        if (sharedPreferences.getBoolean(getString(R.string.pref_geolocation_enabled), false))
        {
            enableGeolocation();
        }
        else
        {
            disableGeolocation();
        }

        this.menu.add(0, MENU_REFRESH, Menu.NONE, "Refresh");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action buttons
        switch (item.getItemId())
        {
            case MENU_REFRESH:
                processSchedule();
                return true;

            case MENU_DISABLE_LOCATION:
                disableGeolocation();
                return true;

            case MENU_ENABLE_LOCATION:
                enableGeolocation();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case DIALOG_FRAGMENT:

                if (resultCode == Activity.RESULT_OK)
                {
                    processSchedule();
                }
                else if (resultCode == Activity.RESULT_CANCELED)
                {
                    // Do Nothing
                }

                break;
        }
    }

    private void enableGeolocation() {
        menu.removeItem(MENU_ENABLE_LOCATION);
        menu.add(0, MENU_DISABLE_LOCATION, Menu.NONE, "Disable Geolocation");

        sharedPreferencesEditor.putBoolean(getString(R.string.pref_geolocation_enabled), true);
        sharedPreferencesEditor.commit();

        processSchedule();
    }

    private void disableGeolocation() {
        menu.removeItem(MENU_DISABLE_LOCATION);
        menu.add(0, MENU_ENABLE_LOCATION, Menu.NONE, "Enable Geolocation");

        sharedPreferencesEditor.putBoolean(getString(R.string.pref_geolocation_enabled), false);
        sharedPreferencesEditor.commit();

        processSchedule();
    }

    private void processSchedule() {
        boolean geolocationEnabled = sharedPreferences.getBoolean(getString(R.string.pref_geolocation_enabled), false);

        ScheduleProcessor processor;

        if (geolocationEnabled)
        {
            // Get the location manager
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

            // Get current location
            Location currentLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));

            processor = new ScheduleProcessor(this, geolocationEnabled, currentLocation);
        }
        else
        {
            processor = new ScheduleProcessor(this, geolocationEnabled, null);
        }

        processor.execute();
    }

    public void setOvercommitment(boolean status) {
        if (status)
        {
            ((MainActivity) getActivity()).getAnimatedActionBar().start(0x00DD9A9A);
        }
        else
        {
            ((MainActivity) getActivity()).getAnimatedActionBar().start(0x00A1DD9A);
        }
    }
}
