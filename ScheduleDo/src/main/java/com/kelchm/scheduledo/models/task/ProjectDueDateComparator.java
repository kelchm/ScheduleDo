package com.kelchm.scheduledo.models.task;

import java.util.Comparator;

/**
 * Created by kelchm on 12/18/13.
 */
public class ProjectDueDateComparator implements Comparator<Project> {
    @Override
    public int compare(Project lhs, Project rhs) {
        long dif = (lhs.getDueDate().getMillis() - rhs.getDueDate().getMillis());

        return (int) dif;  //TODO: This is a hack.  Fix me!
    }
}
