package com.kelchm.scheduledo.models.task;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.kelchm.scheduledo.models.Location;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kelchm on 10/20/13.
 */
@DatabaseTable(tableName = "projects")
public class Project extends Task {

    @ForeignCollectionField(eager = true)
    private ForeignCollection<Action> actions;

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true)
    private Location location;

    public Project() {
        // ORMLite needs a no-arg constructor
    }

    public Project(String name, DateTime dueDate) {
        this.setCreationDate(DateTime.now());
        this.setName(name);
        this.setDueDate(dueDate);
        this.setPriority(Priorities.LOW);
    }

    public int getNumActions() {
        return actions.size();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public List<Action> getActions() {
        return new ArrayList<Action>(actions);
    }

    public void setActions(List<Action> actionsList) {
        actions.clear();
        actions.addAll(actionsList);
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void addAction(String name, long duration, double completion) {
        Action newAction = new Action(this, name, duration, completion);
        actions.add(newAction);
    }

    public void removeAction(Action action) {
        actions.remove(action);
    }

}
