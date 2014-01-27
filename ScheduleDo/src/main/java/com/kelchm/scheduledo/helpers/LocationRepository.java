package com.kelchm.scheduledo.helpers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.kelchm.scheduledo.models.Location;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by kelchm on 10/21/13.
 */
public class LocationRepository {
    private DatabaseHelper db;
    Dao<Location, Integer> locationsDao;

    public LocationRepository(Context ctx) {
        DatabaseManager dbManager = new DatabaseManager();
        db = dbManager.getHelper(ctx);
        locationsDao = db.getLocationsDao();
    }

    public int create(Location location) {
        try
        {
            return locationsDao.create(location);
        } catch (SQLException e)
        {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int update(Location location) {
        try
        {
            return locationsDao.update(location);
        } catch (SQLException e)
        {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(Location location) {
        try
        {
            return locationsDao.delete(location);
        } catch (SQLException e)
        {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public List getAllLocations() {
        try
        {
            return locationsDao.queryForAll();
        } catch (SQLException e)
        {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public Location getProject(int id) {
        try
        {
            return locationsDao.queryForId(id);
        } catch (SQLException e)
        {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
}
