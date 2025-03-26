package com.example.plannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;


public class CalendarEventMake extends AppCompatActivity
{
    private TimePickerDialog timePickerDialog;
    private Button chooseTime;
    private Button chooseDate;
    private EditText displayTime;
    private EditText eventNameET;
    private EditText displayDate;
    private String amPm;
    private LocalTime time;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String userId;

    private LocalDate date;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_event_make);
        initWidgets();
        db = FirebaseFirestore.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        chooseTime = findViewById(R.id.etChooseTime);
        chooseDate = findViewById(R.id.etChooseDate);
        displayTime = findViewById(R.id.displayTime);
        displayDate = findViewById(R.id.displayDate);
        time = LocalTime.now();
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = new TimePickerDialog(CalendarEventMake.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                        time = LocalTime.of(hourOfDay, minutes);
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        displayTime.setText("Time: " + CalendarUtils.formattedTime(time));
                    }
                }, time.getHour(), time.getMinute(), false);
                timePickerDialog.show();
            }
        });
        chooseDate.setOnClickListener(v -> {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            int year = calendar.get(java.util.Calendar.YEAR);
            int month = calendar.get(java.util.Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                date = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay);
                String formattedDate = formatDate(date);;
                displayDate.setText(formattedDate);
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

    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        displayDate = findViewById(R.id.displayDate);
        chooseTime = findViewById(R.id.etChooseTime);
    }

    public void saveEventAction(View view)
    {
        int duration = Toast.LENGTH_SHORT;
        String eventName = eventNameET.getText().toString();
        CalendarEvent newCalendarEvent = new CalendarEvent(null, eventName, CalendarUtils.selectedDate.toString(), time.toString());

        db.collection(userId)
                .add(newCalendarEvent.toMap())
                .addOnSuccessListener(documentReference -> {
                    Toast toast = Toast.makeText(this, "Event created and saved", duration);
                    toast.show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast toast = Toast.makeText(this, "Error saving event", duration);
                    toast.show();
                    finish();
                });
    }

}