package com.example.plannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalTime;


public class CalendarEventEdit extends AppCompatActivity
{
    private TimePickerDialog timePickerDialog;
    private TextView chooseTime;
    private EditText eventNameET;
    private TextView eventDateTV;
    private String amPm;

    private LocalTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_event_edit);
        initWidgets();
        chooseTime = findViewById(R.id.etChooseTime);
        time = LocalTime.now();
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
                        chooseTime.setText("Time: " + CalendarUtils.formattedTime(time));
                    }
                }, time.getHour(), time.getMinute(), false);
                timePickerDialog.show();
            }
        });
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
    }

    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        chooseTime = findViewById(R.id.etChooseTime);
    }

    public void saveEventAction(View view)
    {
        String eventName = eventNameET.getText().toString();
        CalendarEvent newCalendarEvent = new CalendarEvent(eventName, CalendarUtils.selectedDate, time);
        CalendarEvent.eventsList.add(newCalendarEvent);
        finish();
    }
}