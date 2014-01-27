package com.kelchm.scheduledo.helpers;

import android.app.Application;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import com.kelchm.scheduledo.models.schedule.CalendarEvent;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by kelchm on 11/3/13.
 */
public class CalendarHelper extends Application {

    private ArrayList<CalendarEvent> calendarEvents;

    public CalendarHelper(Context context, DateTime startDate, DateTime endDate) {

        calendarEvents = new ArrayList<CalendarEvent>();

        String[] projection = new String[]{CalendarContract.Instances.TITLE, CalendarContract.Instances.EVENT_LOCATION, CalendarContract.Instances.DTSTART, CalendarContract.Instances.DTEND, CalendarContract.Instances.BEGIN, CalendarContract.Instances.END};

        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startDate.getMillis());
        ContentUris.appendId(builder, endDate.getMillis());

        Cursor eventCursor = context.getContentResolver().query(builder.build(), projection, null, null, "dtstart ASC");

        while (eventCursor.moveToNext())
        {
            long eventStartDate = eventCursor.getLong(eventCursor.getColumnIndex("begin"));
            long eventEndDate = eventCursor.getLong(eventCursor.getColumnIndex("end"));
            String title = eventCursor.getString(eventCursor.getColumnIndex("title"));

            CalendarEvent newEvent = new CalendarEvent(new DateTime(eventStartDate), new DateTime(eventEndDate), title);
            calendarEvents.add(newEvent);
        }
    }

    public ArrayList<CalendarEvent> getEvents() {
        return calendarEvents;
    }

}
