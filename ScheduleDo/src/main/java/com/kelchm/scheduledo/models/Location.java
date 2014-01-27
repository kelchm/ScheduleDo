package com.kelchm.scheduledo.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "locations")
public class Location {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private double latitude;

    @DatabaseField
    private double longitude;

    @DatabaseField
    private String name;

    public Location() {
        // ORMLite needs a no-arg constructor
    }

    public Location(String name, double latitude, double longitude) {
        this.name = name;

        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    // Simplifies use of array adapter
    @Override
    public int hashCode() {
        return id;
    }

    // Simplifies use of array adapter
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Location))
        {
            return false;
        }
        else if (this.id == ((Location) obj).id)
        {
            return true;
        }

        return false;
    }

    public android.location.Location getLocation()
    {
        android.location.Location location = new android.location.Location("database");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }
}
