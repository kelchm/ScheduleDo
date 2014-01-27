package com.kelchm.scheduledo.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.kelchm.scheduledo.R;
import com.kelchm.scheduledo.adapters.ActionListAdapter;
import com.kelchm.scheduledo.helpers.LocationRepository;
import com.kelchm.scheduledo.helpers.TasksRepository;
import com.kelchm.scheduledo.models.HasUpdatedProject;
import com.kelchm.scheduledo.models.HoldsProject;
import com.kelchm.scheduledo.models.Location;
import com.kelchm.scheduledo.models.task.Priorities;
import com.kelchm.scheduledo.models.task.Project;
import com.kelchm.scheduledo.views.ButtonAwesome;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Created by kelchm on 10/27/13.
 */
public class EditorFragment extends ListFragment implements HasUpdatedProject, HoldsProject, RadialTimePickerDialog.OnTimeSetListener, CalendarDatePickerDialog.OnDateSetListener {
    public static final int EDIT_ACTION_DIALOG_FRAGMENT = 1;
    public static final int ADD_ACTION_DIALOG_FRAGMENT = 2;
    private DateTime projectDueDate;
    private EditText projectNameEditText;
    private EditText projectDueDateEditText;
    private Spinner projectPrioritySpinner;
    private Spinner projectLocationSpinner;
    private ButtonAwesome dateButton;
    private ButtonAwesome timeButton;
    private Button newActionButton;
    private ListView actionsList;
    final private DateTimeFormatter dueDateFormat = DateTimeFormat.forPattern("EEE d MMM @ h:mm a");
    private ArrayAdapter<Location> locationsArrayAdapter;
    private ActionListAdapter actionListAdapter;
    private Project project;

    public EditorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editor, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Assign all views
        dateButton = (ButtonAwesome) getView().findViewById(R.id.date_button);
        timeButton = (ButtonAwesome) getView().findViewById(R.id.time_button);
        newActionButton = (Button) getView().findViewById(R.id.new_action_button);
        projectNameEditText = (EditText) getView().findViewById(R.id.parent_name);
        projectDueDateEditText = (EditText) getView().findViewById(R.id.project_due_date);
        projectPrioritySpinner = (Spinner) getView().findViewById(R.id.project_priority_spinner);
        projectLocationSpinner = (Spinner) getView().findViewById(R.id.project_location_spinner);
        actionsList = (ListView) getView().findViewById(R.id.listview_actions);

        // Complete setup
        setupOnClickListeners();
        setupPrioritySpinner();
        setupLocationSpinner();
        fillExistingProjectData();
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {

        if (projectDueDate != null)
        {
            int currentDueDay = projectDueDate.getDayOfMonth();
            int currentDueMonth = projectDueDate.getMonthOfYear();
            int currentDueYear = projectDueDate.getYear();

            projectDueDate = new DateTime(currentDueYear, currentDueMonth, currentDueDay, hour, minute);
        }
        else
        {
            projectDueDate = new DateTime(DateTime.now().getDayOfYear(), DateTime.now().getMonthOfYear(), DateTime.now().getDayOfMonth(), hour, minute);
        }

        projectDueDateEditText.setText(projectDueDate.toString(dueDateFormat));
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog, int year, int monthOfYear, int dayOfMonth) {

        // TODO: Fix bug in betterpickers? Months start at 0 instead of 1.
        monthOfYear++;

        if (projectDueDate != null)
        {
            int currentDueHour = projectDueDate.getHourOfDay();
            int currentDueMinute = projectDueDate.getMinuteOfHour();

            projectDueDate = new DateTime(year, monthOfYear, dayOfMonth, currentDueHour, currentDueMinute);
        }
        else
        {
            projectDueDate = new DateTime(year, monthOfYear, dayOfMonth, DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour());
        }

        projectDueDateEditText.setText(projectDueDate.toString(dueDateFormat));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        TasksRepository repository = new TasksRepository(this.getActivity().getApplicationContext());
        repository.refresh(project);
        project.setActions(repository.getProject(project.getId()).getActions());
        actionListAdapter.notifyDataSetChanged();
    }

    private void setupPrioritySpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Priorities> prioritiesArrayAdapter = new ArrayAdapter<Priorities>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, Priorities.values());

        // Apply the adapter to the spinner
        projectPrioritySpinner.setAdapter(prioritiesArrayAdapter);
    }

    private void setupLocationSpinner() {
        // Grab the available locations from the database
        LocationRepository locationRepository = new LocationRepository(this.getActivity());
        List<Location> locations = locationRepository.getAllLocations();

        // Create an ArrayAdapter using the string array and a default spinner layout
        locationsArrayAdapter = new ArrayAdapter<Location>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, locations);

        // Apply the adapter to the spinner
        projectLocationSpinner.setAdapter(locationsArrayAdapter);
    }

    private void setupOnClickListeners() {
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DateTime now = DateTime.now();
                CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog.newInstance(EditorFragment.this, now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth());
                calendarDatePickerDialog.show(fm, "fragment_date_picker_name");
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DateTime now = DateTime.now();
                RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog.newInstance(EditorFragment.this, now.getHourOfDay(), now.getMinuteOfHour(), false);
                timePickerDialog.show(fm, "fragment_time_picker_name");
            }
        });

        newActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditActionDialog editActionDialog = EditActionDialog.newCreateInstance();
                editActionDialog.setTargetFragment(EditorFragment.this, ADD_ACTION_DIALOG_FRAGMENT);
                editActionDialog.show(getFragmentManager().beginTransaction(), "dialog");
            }
        });

        actionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                EditActionDialog editActionDialog = EditActionDialog.newEditInstance(project.getActions().get(position));
                editActionDialog.setTargetFragment(EditorFragment.this, EDIT_ACTION_DIALOG_FRAGMENT);
                editActionDialog.show(getFragmentManager().beginTransaction(), "dialog");
            }
        });
    }

    private void fillExistingProjectData() {
        // Grab project using HoldsProject interface
        project = ((HoldsProject) getActivity()).getProject();

        projectDueDate = project.getDueDate();
        projectNameEditText.setText(project.getName());
        projectDueDateEditText.setText(projectDueDate.toString(dueDateFormat));
        projectPrioritySpinner.setSelection(project.getPriority().ordinal());
        projectLocationSpinner.setSelection(locationsArrayAdapter.getPosition(project.getLocation()));

        // setup the actions listview
        actionListAdapter = new ActionListAdapter(this.getActivity(), project);
        actionsList.setAdapter(actionListAdapter);
    }

    @Override
    public Project getUpdatedProject() {

        project.setName(projectNameEditText.getText().toString());
        project.setDueDate(projectDueDate);
        project.setLocation(((Location) projectLocationSpinner.getSelectedItem()));
        project.setActions(actionListAdapter.getActions());

        return project;
    }

    @Override
    public Project getProject() {
        return project;
    }
}