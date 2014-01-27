package com.kelchm.scheduledo.helpers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.kelchm.scheduledo.models.task.Action;
import com.kelchm.scheduledo.models.task.Project;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by kelchm on 10/21/13.
 */
public class TasksRepository {
    private DatabaseHelper db;
    Dao<Project, Integer> projectsDao;
    Dao<Action, Integer> actionsDao;

    public TasksRepository(Context ctx) {
        DatabaseManager dbManager = new DatabaseManager();
        db = dbManager.getHelper(ctx);
        projectsDao = db.getProjectsDao();
        actionsDao = db.getActionsDao();
    }

    public int refresh(Project project) {
        try
        {
            return projectsDao.refresh(project);
        } catch (SQLException e)
        {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int create(Project project) {
        try
        {
            int result = projectsDao.create(project);
            projectsDao.assignEmptyForeignCollection(project, "actions");
            return result;
        } catch (SQLException e)
        {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int update(Project project) {
        try
        {
            return projectsDao.update(project);
        } catch (SQLException e)
        {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int update(Action action) {
        try
        {
            return actionsDao.update(action);
        } catch (SQLException e)
        {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(Project project) {
        try
        {
            return projectsDao.delete(project);
        } catch (SQLException e)
        {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public List getAllProjects() {
        try
        {
            return projectsDao.queryForAll();
        } catch (SQLException e)
        {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public Project getProject(int id) {
        try
        {
            return projectsDao.queryForId(id);
        } catch (SQLException e)
        {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public Action getAction(int id) {
        try
        {
            return actionsDao.queryForId(id);
        } catch (SQLException e)
        {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public void removeAction(int id) {
        try
        {
            actionsDao.delete(actionsDao.queryForId(id));
        } catch (SQLException e)
        {
            // TODO: Exception Handling
            e.printStackTrace();
        }
    }
}
