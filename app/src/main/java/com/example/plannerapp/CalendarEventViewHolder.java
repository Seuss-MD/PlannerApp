package com.example.plannerapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final TextView eventNameTextView;
    public final TextView eventTimeTextView;
    private final CalendarEventAdapter.OnItemListener onItemListener;

    public CalendarEventViewHolder(@NonNull View itemView, CalendarEventAdapter.OnItemListener onItemListener) {
        super(itemView);
        eventNameTextView = itemView.findViewById(R.id.eventCellTV);
        eventTimeTextView = itemView.findViewById(R.id.eventTimeTV);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition());
    }
}