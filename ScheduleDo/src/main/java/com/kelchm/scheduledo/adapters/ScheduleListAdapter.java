package com.kelchm.scheduledo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kelchm.scheduledo.R;
import com.kelchm.scheduledo.helpers.DateTimeHelper;
import com.kelchm.scheduledo.models.schedule.ActionEvent;
import com.kelchm.scheduledo.models.schedule.ScheduleEvent;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by kelchm on 11/17/13.
 */
public class ScheduleListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<ScheduleEvent> scheduleEvents;
    private LayoutInflater inflater;
    final private DateTimeFormatter headerDateFormat = DateTimeFormat.forPattern("EEE d MMM");
    final private DateTimeFormatter eventTimeFormat = DateTimeFormat.forPattern("h:mm a");

    public ScheduleListAdapter(Context c, List<ScheduleEvent> events) {
        inflater = LayoutInflater.from(c);
        scheduleEvents = events;
    }

    @Override
    public int getCount() {
        return scheduleEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return scheduleEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getName(int position) {
        return scheduleEvents.get(position).getTitle();
    }

    public long getDuration(int position) {
        return scheduleEvents.get(position).getDuration();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null)
        {
            v = inflater.inflate(R.layout.list_item_schedule, parent, false);
        }

        ScheduleEvent event = (ScheduleEvent) getItem(position);

        if (event != null)
        {

            TextView eventDateRange = (TextView) v.findViewById(R.id.event_time);
            TextView eventTitle = (TextView) v.findViewById(R.id.event_title);
            TextView parentName = (TextView) v.findViewById(R.id.parent_name);

            // Display the time range for the event
            if (eventDateRange != null)
            {
                String startTime = event.getStartDate().toString(eventTimeFormat);
                String endTime = event.getEndDate().toString(eventTimeFormat);
                eventDateRange.setText(startTime + " - " + endTime);
            }

            // Display the title
            if (!event.getTitle().isEmpty())
            {
                eventTitle.setText(event.getTitle());
            }

            // Display the parent's name
            if (!event.getParentName().isEmpty())
            {
                parentName.setText(event.getParentName());
            }

            // TODO: This is a mess, fix it!
            if (event instanceof ActionEvent)
            {
                if (((ActionEvent) event).isLate())
                {
                    eventTitle.setTextColor(Color.RED);
                }
            }


        }

        return v;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null)
        {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.list_item_header_schedule, parent, false);
            holder.day = (TextView) convertView.findViewById(R.id.day);
            holder.relativeDay = (TextView) convertView.findViewById(R.id.relative_day);
            convertView.setTag(holder);
        }
        else
        {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        DateTime headerDate = new DateTime(getHeaderId(position));

        //set header text as date
        String headerDateString = headerDate.toString(headerDateFormat);
        holder.day.setText(headerDateString);

        //display 'Today' or 'Tomorrow' if appropriate
        if (DateTimeHelper.isYesterday(headerDate))
        {
            holder.relativeDay.setText(R.string.text_yesterday);
            holder.relativeDay.setPadding(0, 0, 6, 0);
        }
        else if (DateTimeHelper.isToday(headerDate))
        {
            holder.relativeDay.setText(R.string.text_today);
            holder.relativeDay.setPadding(0, 0, 6, 0);
        }
        else if (DateTimeHelper.isTomorrow(headerDate))
        {
            holder.relativeDay.setText(R.string.text_tomorrow);
            holder.relativeDay.setPadding(0, 0, 6, 0);
        }
        else
        {
            holder.relativeDay.setText("");
        }

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        DateTime headerTime = scheduleEvents.get(position).getStartDate().dayOfMonth().roundFloorCopy();
        return headerTime.getMillis();
    }

    class HeaderViewHolder {
        TextView day;
        TextView relativeDay;
    }
}