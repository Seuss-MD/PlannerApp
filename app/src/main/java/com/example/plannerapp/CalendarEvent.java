package com.example.plannerapp;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class CalendarEvent {
    public static ArrayList<CalendarEvent> eventsList = new ArrayList<>();

    private String id;
    private String name;
    private String date;
    private String time;


    public CalendarEvent()
    {

    }



    public CalendarEvent(String id, String name, String date, String time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }



    public static ArrayList<CalendarEvent> eventsForDate(LocalDate date)
    {
        ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();

        for(CalendarEvent calendarEvent : eventsList)
        {
            if(calendarEvent.getDate().equals(date))
                calendarEvents.add(calendarEvent);
        }

        return calendarEvents;
    }

    public static ArrayList<CalendarEvent> eventsForDateAndTime(LocalDate date, LocalTime time)
    {
        ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();

        for(CalendarEvent calendarEvent : eventsList)
        {
            int eventHour = calendarEvent.getTime().getHour();
            int cellHour = time.getHour();
            if(calendarEvent.getDate().equals(date) && eventHour == cellHour)
                calendarEvents.add(calendarEvent);
        }

        return calendarEvents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDate getDate()
    {
        return LocalDate.parse(date);
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public LocalTime getTime()
    {
        //normalizeTime();
        return LocalTime.parse(time);
    }
    public String getTimeString()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public void normalizeTime()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm");

        time = time.format(String.valueOf(formatter));

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("date", date);
        result.put("time", time);
        return result;
    }

}
