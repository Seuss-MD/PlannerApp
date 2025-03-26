package com.example.plannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.app.DatePickerDialog;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class CalendarEventEdit extends AppCompatActivity {
    private EditText eventNameET;
    private TextView eventDateTV;
    private TextView chooseTime;
    private String eventId;
    private FirebaseFirestore db;
    private TimePickerDialog timePickerDialog;
    private LocalTime time;
    private LocalDate date;
    private String amPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_event_edit);

        db = FirebaseFirestore.getInstance();

        eventNameET = findViewById(R.id.eventNameEdit);
        eventDateTV = findViewById(R.id.eventDateEdit);
        chooseTime = findViewById(R.id.eventTimeEdit);

        eventId = getIntent().getStringExtra("eventId");
        String eventName = getIntent().getStringExtra("eventName");
        String eventDate = getIntent().getStringExtra("eventDate");
        String eventTime = getIntent().getStringExtra("eventTime");

        if (eventName != null) {
            eventNameET.setText(eventName);
        }
        if (eventDate != null) {
            date = LocalDate.parse(eventDate);
            eventDateTV.setText(formatDate(date));
        }
        if (eventTime != null) {
            time = LocalTime.parse(eventTime);
            chooseTime.setText(formatTimeTo12Hour(time));
        } else {
            time = LocalTime.now();
        }

        chooseTime.setOnClickListener(view -> {
            timePickerDialog = new TimePickerDialog(CalendarEventEdit.this, (view1, hourOfDay, minutes) -> {
                time = LocalTime.of(hourOfDay, minutes);
                String formattedTime = formatTimeTo12Hour(time);
                chooseTime.setText(formattedTime);
            }, time.getHour(), time.getMinute(), false);
            timePickerDialog.show();
        });

        eventDateTV.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                date = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay);
                String formattedDate = formatDate(date);;
                eventDateTV.setText(formattedDate);
            }, year, month, day);

            datePickerDialog.show();
        });
    }

    private String formatDate(LocalDate date) {
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        int year = date.getYear();
        return String.format("%02d-%02d-%04d", month, day, year);
    }

    private String formatTimeTo12Hour(LocalTime time) {
        int hour = time.getHour();
        int minute = time.getMinute();
        String amPm = hour >= 12 ? "PM" : "AM";
        hour = (hour % 12 == 0) ? 12 : hour % 12;
        return String.format("%d:%02d %s", hour, minute, amPm);
    }

    public void saveEventAction(View view) {
        int duration = Toast.LENGTH_SHORT;


        String updatedName = eventNameET.getText().toString();
        String updatedDate = date.toString();
        String updatedTime = time.toString();



        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", updatedName);
        updatedData.put("date", updatedDate);
        updatedData.put("time", updatedTime);

        db.collection("events").document(eventId)
                .update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    Toast toast = Toast.makeText(this, "Event updated", duration);
                    toast.show();
                    finish();
                })
                .addOnFailureListener(e -> System.err.println( "Error updating event: " + e.getMessage()));
    }

    public void deleteEventAction(View view) {
        int duration = Toast.LENGTH_SHORT;
        if (eventId == null || eventId.isEmpty()) {
            System.err.println("Error: Event ID is missing.");
            return;
        }

        db.collection("events").document(eventId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast toast = Toast.makeText(this, "Event deleted", duration);
                    toast.show();
                    finish();
                })
                .addOnFailureListener(e -> System.err.println("Error deleting event: " + e.getMessage()));
    }
}