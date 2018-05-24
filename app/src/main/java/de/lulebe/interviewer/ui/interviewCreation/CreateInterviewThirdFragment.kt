package de.lulebe.interviewer.ui.interviewCreation

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TimePicker
import de.lulebe.interviewer.CreateInterviewActivity
import de.lulebe.interviewer.R
import de.lulebe.interviewer.data.IntervalType
import kotlinx.android.synthetic.main.fragment_createinterview_third.*

class CreateInterviewThirdFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_createinterview_third, container, false)
        initViews(rootView)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        updateViews()
        changed()
    }

    private fun changed() {
        (activity as CreateInterviewActivity).canMoveOn = true
    }

    private fun updateViews() {
        picker_notificationtime.currentHour = (activity as CreateInterviewActivity).notification.hours
        picker_notificationtime.currentMinute = (activity as CreateInterviewActivity).notification.minutes
        np_days_before.value = (activity as CreateInterviewActivity).notification.daysBefore
    }

    private fun initViews(root: View) {
        val timePicker = root.findViewById<TimePicker>(R.id.picker_notificationtime)
        timePicker.setIs24HourView(true)
        val daysBeforePicker = root.findViewById<NumberPicker>(R.id.np_days_before)
        daysBeforePicker.minValue = 0
        daysBeforePicker.maxValue = 7
    }
}
