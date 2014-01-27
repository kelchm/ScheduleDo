package com.kelchm.scheduledo.models.schedule;

import com.kelchm.scheduledo.models.schedule.util.HasInterval;
import com.kelchm.scheduledo.models.schedule.util.Interval;

import org.joda.time.DateTime;

/**
 * Created by kelchm on 11/4/13.
 */
public abstract class ScheduleEvent implements Comparable<ScheduleEvent>, HasInterval {

    protected DateTime startDate;
    protected DateTime endDate;

    @Override
    public Interval getInterval() {

        return Interval.toInterval(startDate.getMillis(), endDate.getMillis());
    }

    @Override
    public int compareTo(ScheduleEvent another) {
        return 0;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public abstract String getTitle();

    public abstract long getDuration();

    public abstract String getParentName();

}
