package com.example.plannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
            eventDateTV.setText(eventDate);
        }
        if (eventTime != null) {
            chooseTime.setText(eventTime);
        }
        time = eventTime != null ? LocalTime.parse(eventTime) : LocalTime.now();
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = new TimePickerDialog(CalendarEventEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                        time = LocalTime.of(hourOfDay, minutes);
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        System.out.println("Time format: " + CalendarUtils.formattedTime(time));
                        System.out.println("Time: " + time);
                        chooseTime.setText(CalendarUtils.formattedTime(time));
                    }
                }, time.getHour(), time.getMinute(), false);
                timePickerDialog.show();
            }
        });
    }

    public void saveEventAction(View view) {
        String updatedName = eventNameET.getText().toString();
        String updatedDate = eventDateTV.getText().toString();
        String updatedTime = time.toString();

        if (updatedName.isEmpty() || updatedDate.isEmpty() || updatedTime.isEmpty()) {
            System.err.println("Error: All fields must be filled out.");
            return;
        }

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", updatedName);
        updatedData.put("date", updatedDate);
        updatedData.put("time", updatedTime);

        db.collection("events").document(eventId)
                .update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Event updated successfully!");
                    finish();
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error updating event: " + e.getMessage());
                });
    }
}