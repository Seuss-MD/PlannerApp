package com.example.plannerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.plannerapp.databinding.FragmentCalendarBinding
import com.example.plannerapp.databinding.FragmentTimerBinding
import java.util.Timer
import java.util.TimerTask
import java.sql.Time
import java.util.*
import kotlin.math.min


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Timer : Fragment() {
    private lateinit var binding: FragmentTimerBinding
    private lateinit var dataHelper: DataHelper
    private var timer: Timer? = null

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(inflater, container, false)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        dataHelper = DataHelper(requireContext())

        binding.startButton.setOnClickListener { startStopAction() }
        binding.resetButton.setOnClickListener { resetAction() }

        if (dataHelper.timerCounting()) {
            startTimer()
        } else {
            stopTimer()
            if (dataHelper.startTime() != null && dataHelper.stopTime() != null) {
                val time = Date().time - calcRestartTime().time
                binding.timeTextView.text = timeStringFromLong(time)
            }
        }

        startTimerTask() // Start the timer task
    }

    private fun startTimerTask() {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                if (dataHelper.timerCounting()) {
                    val time = Date().time - dataHelper.startTime()!!.time
                    requireActivity().runOnUiThread {
                        binding.timeTextView.text = timeStringFromLong(time)
                    }
                }
            }
        }, 0, 500) // Schedule the task to execute every 500ms
    }



    private fun resetAction()
    {
        dataHelper.setStopTime(null)
        dataHelper.setStartTime(null)
        stopTimer()
        binding.timeTextView.text = timeStringFromLong(0)
    }



    private fun stopTimer()
    {
        dataHelper.setTimerCounting(false)
        binding.startButton.text = getString(R.string.start)
    }


    private fun startTimer()
    {
        dataHelper.setTimerCounting(true)
        binding.startButton.text = getString(R.string.stop)
    }

    private fun startStopAction()
    {
        if (dataHelper.timerCounting())
        {
            dataHelper.setStopTime(Date())
            stopTimer()
        }
        else
        {
            if (dataHelper.stopTime() !=  null)
            {
                dataHelper.setStartTime(calcRestartTime())
                dataHelper.setStopTime(null)
            }
            else
            {
                dataHelper.setStartTime(Date())
            }
            startTimer()
        }

    }

    private fun calcRestartTime(): Date
    {
        val diff = dataHelper.startTime()!!.time- dataHelper.stopTime()!!.time
        return Date(System.currentTimeMillis() +diff)
    }

    private fun timeStringFromLong(ms: Long): String?
    {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000*60) %60)
        val hours = (ms/(1000*60*60)%24)
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String?
    {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        dataHelper.setStopTime(null)
        dataHelper.setStartTime(null)
        binding.timeTextView.text = timeStringFromLong(0)
    }

}
