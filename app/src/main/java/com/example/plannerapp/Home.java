package com.example.plannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.plannerapp.Login;
import com.example.plannerapp.databinding.FragmentHomeBinding;

public class Home extends Fragment {

    private FirebaseAuth auth;
    private Button logoutButton;
    private TextView userDetailsTextView;
    private FirebaseUser user;

    // Use view binding
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment using view binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        logoutButton = binding.logout; // Access views through binding
        userDetailsTextView = binding.userDetails; // Access views through binding
        user = auth.getCurrentUser();

        if (user == null) {
            navigateToLogin();
        } else {
            userDetailsTextView.setText(user.getEmail());
        }

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            navigateToLogin();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Release binding when the view is destroyed
    }

    private void navigateToLogin() {
        Intent intent = new Intent(requireContext(), Login.class);
        startActivity(intent);
        // No need to call finish() here, as it's a Fragment, not an Activity
    }
}