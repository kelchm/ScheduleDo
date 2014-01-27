package com.kelchm.scheduledo.models.schedule;

import org.joda.time.DateTime;

/**
 * Created by kelchm on 11/24/13.
 */
public class CalendarEvent extends ScheduleEvent {

    private String title;

    public CalendarEvent(DateTime startDate, DateTime endDate, String title) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
    }

    public CalendarEvent(DateTime startDate, DateTime endDate) {
        // Call the other constructor
        this(startDate, endDate, null);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public long getDuration() {
        return endDate.getMillis() - startDate.getMillis();
    }

    @Override
    public String getParentName() {
        return "Calendar";
    }
}
