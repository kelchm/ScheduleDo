package com.kelchm.scheduledo.models.task;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

abstract class Task {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private DateTime creationDate;

    @DatabaseField
    private DateTime dueDate;

    @DatabaseField
    private String name;

    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    Priorities priority;

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate() {
        this.setCreationDate(DateTime.now());
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public DateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setPriority(Priorities priority) {
        this.priority = priority;
    }

    public Priorities getPriority() {
        return priority;
    }

}

