package de.lulebe.interviewer.ui.interviewCreation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.github.florent37.expansionpanel.ExpansionLayout
import de.lulebe.interviewer.CreateInterviewActivity
import de.lulebe.interviewer.R
import de.lulebe.interviewer.data.IntervalType
import de.lulebe.interviewer.ui.views.ChipMultiPickerView
import kotlinx.android.synthetic.main.fragment_createinterview_second.*
import java.util.*

class CreateInterviewSecondFragment : Fragment() {

    companion object {
        val INTERVALS = listOf("Every year", "Every 6 Months", "Every 3 Months", "Every month", "Every week", "Every day")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_createinterview_second, container, false)
        initView(rootView)
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
        chips_interval.setSelectedItems(listOf((activity as CreateInterviewActivity).schedule.intervalType.ordinal))
    }

    private fun initView(root: View) {
        val intervalPicker = root.findViewById<ChipMultiPickerView>(R.id.chips_interval)
        intervalPicker.items = INTERVALS
        intervalPicker.selectionChangedListener = {
            (activity as CreateInterviewActivity).schedule.intervalType = IntervalType.values()[it.first()]
            changed()
        }
    }
}
