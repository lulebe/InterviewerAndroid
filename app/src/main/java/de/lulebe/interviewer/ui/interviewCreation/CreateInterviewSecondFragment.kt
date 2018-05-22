package de.lulebe.interviewer.ui.interviewCreation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.florent37.expansionpanel.ExpansionHeader
import com.github.florent37.expansionpanel.ExpansionLayout
import de.lulebe.interviewer.CreateInterviewActivity
import de.lulebe.interviewer.R
import de.lulebe.interviewer.ui.views.ChipMultiPickerView
import kotlinx.android.synthetic.main.fragment_createinterview_second.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class CreateInterviewSecondFragment : Fragment() {

    companion object {
        val MONTHS = listOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
        val DATES = listOf(
                "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"
        )
        val DAYS = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_createinterview_second, container, false)
        initView(rootView, savedInstanceState)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        changed()
        updateViews()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("layout1Expanded", layout1.isExpanded)
        outState.putBoolean("layout2Expanded", layout2.isExpanded)
        outState.putBoolean("layout3Expanded", layout3.isExpanded)
        super.onSaveInstanceState(outState)
    }

    private fun changed() {
        (activity as CreateInterviewActivity).canMoveOn = true
    }

    private fun updateViews() {
        val notifications = (activity as CreateInterviewActivity).notificationsList[0]
        chips_months.setSelectedItems(notifications.months)
        tv_selected_months.text = MONTHS
                .filterIndexed{ index, _ -> notifications.months.contains(index) }
                .joinToString(", ")
        chips_dates.setSelectedItems(notifications.dates.map{it-1})
        tv_selected_dates.text = DATES
                .filterIndexed { index, _ -> notifications.dates.map{it-1}.contains(index) }
                .joinToString(", ")
        chips_days.setSelectedItems(notifications.days)
        tv_selected_days.text = DAYS
                .filterIndexed { index, _ -> notifications.days.contains(index) }
                .joinToString(", ")
    }

    private fun initView(root: View, savedInstanceState: Bundle?) {
        val monthsPicker = root.findViewById<ChipMultiPickerView>(R.id.chips_months)
        monthsPicker.items = MONTHS
        monthsPicker.selectionChangedListener = {
            tv_selected_months.text = MONTHS.filterIndexed { index, _ -> it.contains(index) }.joinToString(", ")
            (activity as CreateInterviewActivity).notificationsList[0].months = it
        }
        val datesPicker = root.findViewById<ChipMultiPickerView>(R.id.chips_dates)
        datesPicker.items = DATES
        datesPicker.selectionChangedListener = {
            tv_selected_dates.text = DATES.filterIndexed { index, _ -> it.contains(index) }.joinToString(", ")
            (activity as CreateInterviewActivity).notificationsList[0].dates = it.map({ it+1 })
        }
        val daysPicker = root.findViewById<ChipMultiPickerView>(R.id.chips_days)
        daysPicker.items = DAYS
        daysPicker.selectionChangedListener = {
            tv_selected_days.text = DAYS.filterIndexed { index, _ -> it.contains(index) }.joinToString(", ")
            (activity as CreateInterviewActivity).notificationsList[0].days = it
        }
        savedInstanceState?.let {
            val l1 = root.findViewById<ExpansionLayout>(R.id.layout1)
            val l2 = root.findViewById<ExpansionLayout>(R.id.layout2)
            val l3 = root.findViewById<ExpansionLayout>(R.id.layout3)
            l1.expand(false)
            l1.collapse(false)
            l2.expand(false)
            l2.collapse(false)
            l3.expand(false)
            l3.collapse(false)
            if (it.getBoolean("layout1Expanded"))
                l1.expand(false)
            if (it.getBoolean("layout2Expanded"))
                l2.expand(false)
            if (it.getBoolean("layout3Expanded"))
                l3.expand(false)
        }
    }
}
