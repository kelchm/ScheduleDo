package com.kelchm.scheduledo.models.schedule;

import com.kelchm.scheduledo.models.task.Action;

import org.joda.time.DateTime;

/**
 * Created by kelchm on 12/18/13.
 */
public class ActionEvent extends ScheduleEvent {

    Action action;
    boolean isLate;

    public ActionEvent(Action action) {
        this.action = action;
    }

    @Override
    public String getTitle() {
        return action.getName();
    }

    @Override
    public long getDuration() {
        return action.getDuration();
    }

    public double getCompletion() {
        return action.getCompletion();
    }

    @Override
    public String getParentName() {
        return action.getParentProject().getName();
    }

    public DateTime getDueDate() {
        return action.getParentProject().getDueDate();
    }

    public Action getAction() {
        return action;
    }

    public void setLate() {
        isLate = true;
    }

    public boolean isLate() {
        return isLate;
    }
}
