package com.example.plannerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.plannerapp.CalendarUtils.daysInWeekArray;
import static com.example.plannerapp.CalendarUtils.monthYearFromDate;

public class CalendarWeekView extends AppCompatActivity implements CalendarAdapter.OnItemListener, CalendarEventAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private FirebaseFirestore db;
    private ArrayList<CalendarEvent> dailyCalendarEvents;
    private RecyclerView eventRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_week_view);
        db = FirebaseFirestore.getInstance();
        initWidgets();
        setWeekView();
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventRecyclerView = findViewById(R.id.eventRecyclerView);
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }

    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {
        if (eventRecyclerView == null) {
            System.err.println("Error: eventRecyclerView is null");
            return;
        }
        db.collection("events")
                .whereEqualTo("date", CalendarUtils.selectedDate.toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dailyCalendarEvents = new ArrayList<>();
                        if (task.getResult() != null) {
                            System.out.println("Firestore query executed successfully.");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    System.out.println("Document retrieved: " + document.getId());
                                    CalendarEvent event = document.toObject(CalendarEvent.class);
                                    event.setId(document.getId()); // Set the document ID as the event ID
                                    System.out.println("Event retrieved: " + event.getName());
                                    dailyCalendarEvents.add(event);
                                    System.out.println("Event added: " + event.getName());
                                } catch (Exception e) {
                                    System.err.println("Error deserializing document: " + e.getMessage());
                                }
                            }
                        } else {
                            System.err.println("Error: task.getResult() is null");
                        }
                        CalendarEventAdapter calendarEventAdapter = new CalendarEventAdapter(dailyCalendarEvents, this);
                        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                        eventRecyclerView.setAdapter(calendarEventAdapter); // Set adapter to RecyclerView
                        System.out.println("Adapter set with " + dailyCalendarEvents.size() + " events");

                    } else {
                        System.err.println("Error getting documents: " + task.getException());
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        CalendarEvent event = dailyCalendarEvents.get(position);
        System.out.println("Event clicked: " + event.getName());

        Intent intent = new Intent(this, CalendarEventEdit.class);
        intent.putExtra("eventId", event.getId());
        intent.putExtra("eventName", event.getName());
        intent.putExtra("eventTime", event.getTime().toString());
        intent.putExtra("displayTime", event.getTime().toString());

        intent.putExtra("eventDate", event.getDate().toString());
        startActivity(intent);
    }

    public void newEventAction(View view) {
        System.out.println("New Event button clicked");
        startActivity(new Intent(this, CalendarEventMake.class));

    }

    public void dailyAction(View view) {
        System.out.println("Daily button clicked");
        startActivity(new Intent(this, CalendarDaily.class));
    }

}