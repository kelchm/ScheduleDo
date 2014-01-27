package com.kelchm.scheduledo.models.schedule;

import android.util.Log;

import com.kelchm.scheduledo.models.schedule.util.IntervalTree;

import org.joda.time.DateTime;

import java.util.Iterator;
import java.util.List;

/**
 * Created by kelchm on 11/23/13.
 */
public class Schedule {
    private static final String TAG = Schedule.class.getName();
    private IntervalTree scheduleTree;
    private DateTime scheduleStartDate;
    boolean hasFloatingEvent;
    private boolean overcommited;

    public Schedule(DateTime startDate) {
        scheduleStartDate = startDate;
        scheduleTree = new IntervalTree();
        hasFloatingEvent = false;
        Log.i(TAG, "Schedule tree created");
    }

    public void addActionEvent(ActionEvent event) {
        // Determine event time remaining
        long duration = (long) (event.getDuration() * (1.00 - event.getCompletion()));

        // Don't schedule events of 0 duration
        if(duration > 0)
        {
            // Set initial start and end dates
            DateTime startDate = scheduleStartDate;
            DateTime endDate = scheduleStartDate.plus(duration);

            // Setup for loop
            boolean eventAdded = false;
            int iterations = 0;

            do
            {
                // Set the start and end date for the event
                event.setStartDate(startDate);
                event.setEndDate(endDate);

                // Attempt to add the event at current startDate
                eventAdded = scheduleTree.addNonOverlapping(event);

                // Iterate over events which overlap in order to find the maximum end date.
                DateTime newStartDate = scheduleStartDate;
                for (Iterator<ScheduleEvent> list = scheduleTree.getOverlapping(event).iterator(); list.hasNext(); )
                {
                    ScheduleEvent e = list.next();
                    if (e.getEndDate().getMillis() > newStartDate.getMillis())
                    {
                        newStartDate = e.getEndDate();
                    }
                }
                // This allows us to skip a large number of iterations which would otherwise be required
                startDate = newStartDate;

                // increment counter and dates
                iterations++;

                // Set the new start and end dates
                startDate = startDate.plusMillis(1);
                endDate = startDate.plus(duration).plusMillis(1);

            } while (!eventAdded);

            if(event.getDueDate().getMillis()<event.getEndDate().getMillis())
            {
                overcommited = true;
                event.setLate();
            }

            hasFloatingEvent = true;
            Log.i(TAG, "Floating event added (" + iterations + " iterations)");
        }
        else
        {
            Log.i(TAG, "Floating event not added (0 duration)");
        }
    }

    public void addCalendarEvent(CalendarEvent event) {
        if (!hasFloatingEvent)
        {
            scheduleTree.add(event);
            Log.i(TAG, "Fixed event added");
        }
        else
        {
            Log.e(TAG, "A fixed event cannot be added after a floating event");
        }
    }

    public List<ScheduleEvent> getSchedule() {
        DateTime startDate = new DateTime(0);  // Minimum Possible
        DateTime endDate = new DateTime(Long.MAX_VALUE); // Max possible
        return scheduleTree.getOverlapping(new CalendarEvent(startDate, endDate));
    }

    public List<ScheduleEvent> getSchedule(DateTime startDate, DateTime endDate) {
        return scheduleTree.getOverlapping(new CalendarEvent(startDate, endDate));
    }

    public boolean isOvercommited()
    {
        return overcommited;
    }
}
