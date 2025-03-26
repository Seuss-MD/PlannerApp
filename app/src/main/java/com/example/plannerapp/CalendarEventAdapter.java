package com.example.plannerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventViewHolder> {
    private final List<CalendarEvent> calendarEvents;
    private final OnItemListener onItemListener;

    public CalendarEventAdapter(List<CalendarEvent> calendarEvents, OnItemListener onItemListener) {
        this.calendarEvents = calendarEvents;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_event_cell, parent, false);
        return new CalendarEventViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarEventViewHolder holder, int position) {
        CalendarEvent event = calendarEvents.get(position);
        if (event.getName() != null && !event.getName().isEmpty())
            holder.eventNameTextView.setText(event.getName());
        else
            holder.eventNameTextView.setText("No Event Name");
        LocalTime eventTime = event.getTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a"); // e.g., 10:30 AM
        holder.eventTimeTextView.setText(eventTime.format(formatter));    }

    @Override
    public int getItemCount() {
        return calendarEvents.size();
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }
}