package com.example.plannerapp;

import static com.example.plannerapp.CalendarUtils.selectedDate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class CalendarDaily extends AppCompatActivity
{

    private TextView monthDayText;
    private TextView dayOfWeekTV;
    private ListView hourListView;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_daily_view);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }
        initWidgets();
    }

    private void initWidgets()
    {
        monthDayText = findViewById(R.id.monthDayText);
        dayOfWeekTV = findViewById(R.id.dayOfWeekTV);
        hourListView = findViewById(R.id.hourListView);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setDayView();
    }

    private void setDayView()
    {
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        setHourAdapter();
    }

    private void setHourAdapter()
    {
        db.collection(userId)
                .whereEqualTo("date", selectedDate.toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<CalendarHourEvent> list = new ArrayList<>();
                        System.out.println("Firestore query executed successfully.");
                        for (int hour = 0; hour < 24; hour++) {
                            LocalTime time = LocalTime.of(hour, 0);
                            ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();
                            System.out.println("Hour: " + hour);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CalendarEvent event = document.toObject(CalendarEvent.class);
                                System.out.println("Event retrieved: " + event.getName());
                                System.out.println("Event hour: " + event.getTime().getHour());
                                System.out.println("Hour of Day: " + time);
                                if (event.getTime().getHour() == time.getHour()) {
                                    System.out.println("Event added: " + event.getName());
                                    calendarEvents.add(event);
                                }
                            }

                            CalendarHourEvent calendarHourEvent = new CalendarHourEvent((time), calendarEvents);
                            list.add(calendarHourEvent);
                        }
                        CalendarHourAdapter calendarHourAdapter = new CalendarHourAdapter(getApplicationContext(), list);
                        hourListView.setAdapter(calendarHourAdapter);
                    } else {
                        System.err.println("Error getting documents: " + task.getException());
                    }
                });
    }

    private ArrayList<CalendarHourEvent> hourEventList()
    {
        ArrayList<CalendarHourEvent> list = new ArrayList<>();

        for(int hour = 0; hour < 24; hour++)
        {
            LocalTime time = LocalTime.of(hour, 0);
            ArrayList<CalendarEvent> calendarEvents = CalendarEvent.eventsForDateAndTime(selectedDate, time);
            CalendarHourEvent calendarHourEvent = new CalendarHourEvent(time, calendarEvents);
            list.add(calendarHourEvent);
        }

        return list;
    }

    public void previousDayAction(View view)
    {
        selectedDate = selectedDate.minusDays(1);
        setDayView();
    }

    public void nextDayAction(View view)
    {
        selectedDate = selectedDate.plusDays(1);
        setDayView();
    }

    public void newEventAction(View view)
    {
        startActivity(new Intent(this, CalendarEventMake.class));
    }
}