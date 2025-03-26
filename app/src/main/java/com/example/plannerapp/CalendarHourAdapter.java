package com.example.plannerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarHourAdapter extends ArrayAdapter<CalendarHourEvent>
{
    public CalendarHourAdapter(@NonNull Context context, List<CalendarHourEvent> calendarHourEvents)
    {
        super(context, 0, calendarHourEvents);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        CalendarHourEvent event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.calendar_hour_cell, parent, false);

        setHour(convertView, event.time);
        setEvents(convertView, event.calendarEvents);

        return convertView;
    }

    private void setHour(View convertView, LocalTime time)
    {
        TextView timeTV = convertView.findViewById(R.id.timeTV);
        timeTV.setText(CalendarUtils.formattedShortTime(time));
    }

    private void setEvents(View convertView, ArrayList<CalendarEvent> calendarEvents)
    {
        TextView event1 = convertView.findViewById(R.id.event1);
        TextView event2 = convertView.findViewById(R.id.event2);
        TextView event3 = convertView.findViewById(R.id.event3);

        if(calendarEvents.size() == 0)
        {
            hideEvent(event1);
            hideEvent(event2);
            hideEvent(event3);
        }
        else if(calendarEvents.size() == 1)
        {
            setEvent(event1, calendarEvents.get(0));
            hideEvent(event2);
            hideEvent(event3);
        }
        else if(calendarEvents.size() == 2)
        {
            setEvent(event1, calendarEvents.get(0));
            setEvent(event2, calendarEvents.get(1));
            hideEvent(event3);
        }
        else if(calendarEvents.size() == 3)
        {
            setEvent(event1, calendarEvents.get(0));
            setEvent(event2, calendarEvents.get(1));
            setEvent(event3, calendarEvents.get(2));
        }
        else
        {
            setEvent(event1, calendarEvents.get(0));
            setEvent(event2, calendarEvents.get(1));
            event3.setVisibility(View.VISIBLE);
            String eventsNotShown = String.valueOf(calendarEvents.size() - 2);
            eventsNotShown += " More Events";
            event3.setText(eventsNotShown);
        }
    }

    private void setEvent(TextView textView, CalendarEvent calendarEvent)
    {
        textView.setText(calendarEvent.getName());
        if(calendarEvent.getName() == null || calendarEvent.getName().isEmpty())
            textView.setText("No Name");
        textView.setVisibility(View.VISIBLE);
    }

    private void hideEvent(TextView tv)
    {
        tv.setVisibility(View.INVISIBLE);
    }

}


