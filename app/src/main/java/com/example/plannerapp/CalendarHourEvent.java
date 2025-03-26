package com.example.plannerapp;

import java.time.LocalTime;
import java.util.ArrayList;

class CalendarHourEvent
{
    LocalTime time;
    ArrayList<CalendarEvent> calendarEvents;

    public CalendarHourEvent(LocalTime time, ArrayList<CalendarEvent> calendarEvents)
    {
        this.time = time;
        this.calendarEvents = calendarEvents;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    public ArrayList<CalendarEvent> getEvents()
    {
        return calendarEvents;
    }

    public void setEvents(ArrayList<CalendarEvent> calendarEvents)
    {
        this.calendarEvents = calendarEvents;
    }
}
