package com.kelchm.scheduledo.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.kelchm.scheduledo.adapters.ScheduleListAdapter;
import com.kelchm.scheduledo.fragments.ScheduleFragment;
import com.kelchm.scheduledo.models.schedule.ActionEvent;
import com.kelchm.scheduledo.models.schedule.CalendarEvent;
import com.kelchm.scheduledo.models.schedule.Schedule;
import com.kelchm.scheduledo.models.schedule.ScheduleEvent;
import com.kelchm.scheduledo.models.task.Action;
import com.kelchm.scheduledo.models.task.Project;
import com.kelchm.scheduledo.models.task.ProjectDueDateComparator;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

/**
 * Created by kelchm on 10/26/13.
 */
public class ScheduleProcessor extends AsyncTask<Object, Integer, List<ScheduleEvent>> {

    private ScheduleFragment scheduleFragment;
    private boolean geolocationEnabled;
    private android.location.Location currentLocation;
    private Schedule schedule;
    private CalendarHelper calendarHelper;

    public ScheduleProcessor(ScheduleFragment scheduleFragment, boolean geolocationEnabled, android.location.Location currentLocation) {
        this.scheduleFragment = scheduleFragment;
        this.geolocationEnabled = geolocationEnabled;
        this.currentLocation = currentLocation;
    }

    @Override
    protected List<ScheduleEvent> doInBackground(Object... params) {
        // Set schedule start and end dates
        DateTime startDate = roundDate(DateTime.now());
        DateTime endDate = startDate.plusDays(14);

        schedule = new Schedule(startDate);

        // Get calendar events
        calendarHelper = new CalendarHelper(scheduleFragment.getActivity(), startDate, endDate);

        // Add all calendar events
        for (CalendarEvent calendarEvent : calendarHelper.getEvents())
        {
            schedule.addCalendarEvent(calendarEvent);
        }

        // Get all projects
        TasksRepository repository = new TasksRepository(scheduleFragment.getActivity());
        List<Project> projects = repository.getAllProjects();

        // Sort projects by due date
        Collections.sort(projects, new ProjectDueDateComparator());

        // Add actions to schedule
        for (Project project : projects)
        {
            boolean localProject = true;

            // Is geolocation turned on?  Does the project have a location set?
            if (geolocationEnabled && project.getLocation() != null)
            {
                float distance = project.getLocation().getLocation().distanceTo(currentLocation);

                if (distance >= 300.0)  //TODO: Make this distance a preference
                {
                    localProject = false;
                }
            }

            if (localProject)
            {
                for (Action action : project.getActions())
                {
                    ActionEvent actionEvent = new ActionEvent(action);
                    schedule.addActionEvent(actionEvent);
                }
            }
        }

        List<ScheduleEvent> events = schedule.getSchedule();

        return events;
    }

    @Override
    protected void onPostExecute(List<ScheduleEvent> events) {
        Log.i(ScheduleProcessor.class.getName(), "onPostExecute called");
        ScheduleListAdapter adapter = new ScheduleListAdapter(scheduleFragment.getContext(), events);
        scheduleFragment.setListAdapter(adapter);
        scheduleFragment.setOvercommitment(schedule.isOvercommited());
    }

    private DateTime roundDate(final DateTime dateTime) {
        final double minuteOfHour = dateTime.getMinuteOfHour();
        final double tenth = minuteOfHour / 10;
        final long round = Math.round(tenth);
        final int i = (int) (round * 10);

        if (i == 60)
        {
            return dateTime.plusHours(1).withMinuteOfHour(0);
        }
        else
        {
            return dateTime.withMinuteOfHour(i);
        }

    }

}
