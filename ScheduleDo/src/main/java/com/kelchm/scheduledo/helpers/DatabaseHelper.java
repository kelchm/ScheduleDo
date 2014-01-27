package com.kelchm.scheduledo.helpers;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kelchm.scheduledo.BuildConfig;
import com.kelchm.scheduledo.models.Location;
import com.kelchm.scheduledo.models.task.Action;
import com.kelchm.scheduledo.models.task.Project;

import org.joda.time.DateTime;

/**
 * Created by kelchm on 10/19/13.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "ScheduleDo.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 41;
    // these are DAO objects we use to access the tables
    private Dao<Project, Integer> projectDao = null;
    private Dao<Action, Integer> actionDao = null;
    private Dao<Location, Integer> locationDao = null;
    private RuntimeExceptionDao<Project, Integer> projectRuntimeDao = null;
    private RuntimeExceptionDao<Action, Integer> actionRuntimeDao = null;
    private RuntimeExceptionDao<Location, Integer> locationRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try
        {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Project.class);
            TableUtils.createTable(connectionSource, Action.class);
            TableUtils.createTable(connectionSource, Location.class);
        } catch (java.sql.SQLException e)
        {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

        // Is this a debug build?
        if (BuildConfig.DEBUG)
        {
            // here we try inserting data in the on-create as a test
            RuntimeExceptionDao<Project, Integer> tempProjectsDao = getProjectsDataDao();
            RuntimeExceptionDao<Action, Integer> tempActionsDao = getActionsDataDao();
            RuntimeExceptionDao<Location, Integer> tempLocationsDao = getLocationsDataDao();

            // create some entries in the onCreate
            for (int i = 0; i < 3; i++)
            {
                Project project = new Project("Test Project " + i, DateTime.now());
                tempProjectsDao.create(project);

                for (int j = 0; j < 3; j++)
                {
                    Action action = new Action(project, "Test Action " + j, 3600000, 0.0);
                    tempActionsDao.create(action);
                }
            }

            // create some example locations
            Location testLocation1 = new Location("MCT", 40.06097534, -77.52265602);
            Location testLocation2 = new Location("H. Ric Luhrs Performing Arts Center", 40.06362142, -77.52265334);
            Location testLocation3 = new Location("Walmart", 40.06225836, -77.49791265);
            Location testLocation4 = new Location("Sheetz", 40.05476928, -77.51365185);
            tempLocationsDao.create(testLocation1);
            tempLocationsDao.create(testLocation2);
            tempLocationsDao.create(testLocation3);
            tempLocationsDao.create(testLocation4);

            Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate");
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try
        {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            // we just drop the old tables for now
            TableUtils.dropTable(connectionSource, Project.class, true);
            TableUtils.dropTable(connectionSource, Action.class, true);
            TableUtils.dropTable(connectionSource, Location.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (java.sql.SQLException e)
        {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for the Project class. It will create it or just give the cached
     * value.
     */
    public Dao<Project, Integer> getProjectsDao() throws SQLException {
        if (projectDao == null)
        {
            try
            {
                projectDao = getDao(Project.class);
            } catch (java.sql.SQLException e)
            {
                Log.e(DatabaseHelper.class.getName(), "Can't get DAO", e);
                throw new RuntimeException(e);
            }
        }
        return projectDao;
    }

    /**
     * Returns the Database Access Object (DAO) for the Action class. It will create it or just give the cached
     * value.
     */
    public Dao<Action, Integer> getActionsDao() throws SQLException {
        if (actionDao == null)
        {
            try
            {
                actionDao = getDao(Action.class);
            } catch (java.sql.SQLException e)
            {
                Log.e(DatabaseHelper.class.getName(), "Can't get DAO", e);
                throw new RuntimeException(e);
            }
        }
        return actionDao;
    }

    /**
     * Returns the Database Access Object (DAO) for the Location class. It will create it or just give the cached
     * value.
     */
    public Dao<Location, Integer> getLocationsDao() throws SQLException {
        if (locationDao == null)
        {
            try
            {
                locationDao = getDao(Location.class);
            } catch (java.sql.SQLException e)
            {
                Log.e(DatabaseHelper.class.getName(), "Can't get DAO", e);
                throw new RuntimeException(e);
            }
        }
        return locationDao;
    }

    public RuntimeExceptionDao<Project, Integer> getProjectsDataDao() {
        if (projectRuntimeDao == null)
        {
            projectRuntimeDao = getRuntimeExceptionDao(Project.class);
        }
        return projectRuntimeDao;
    }

    public RuntimeExceptionDao<Action, Integer> getActionsDataDao() {
        if (actionRuntimeDao == null)
        {
            actionRuntimeDao = getRuntimeExceptionDao(Action.class);
        }
        return actionRuntimeDao;
    }

    public RuntimeExceptionDao<Location, Integer> getLocationsDataDao() {
        if (locationRuntimeDao == null)
        {
            locationRuntimeDao = getRuntimeExceptionDao(Location.class);
        }
        return locationRuntimeDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        projectDao = null;
        actionDao = null;
        locationDao = null;
    }
}
