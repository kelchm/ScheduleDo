package com.kelchm.scheduledo.models.task;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

/**
 * Created by kelchm on 10/21/13.
 */
@DatabaseTable(tableName = "actions")
public class Action extends Task {
    public static final String PROJECT_ID_FIELD_NAME = "project_id";

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = PROJECT_ID_FIELD_NAME)
    private Project parentProject;

    @DatabaseField
    private long duration;

    @DatabaseField
    private double completion;

    public Action() {
        // ORMLite needs a no-arg constructor
    }

    public Action(Project parentProject, String name, long duration, double completion) {
        this.setCreationDate();
        this.setParentProject(parentProject);
        this.setName(name);
        this.setCompletion(completion);
        this.setDuration(duration);
    }

    public void setParentProject(Project parentProject) {
        this.parentProject = parentProject;
    }

    public Project getParentProject() {
        return parentProject;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public double getCompletion() {
        return completion;
    }

    public void setCompletion(double completion) {
        this.completion = completion;
    }

    @Override
    public DateTime getDueDate() {
        return parentProject.getDueDate();
    }


}
