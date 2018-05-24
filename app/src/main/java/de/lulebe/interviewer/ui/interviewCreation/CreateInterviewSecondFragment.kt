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
        val INTERVALS = listOf("Every X months", "Every X weeks", "Every X days")
        val FIRST_MONTHS = listOf("Jan", "Feb", "Mar", "Apr", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val MAX_DATES_IN_MONTHS = listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        val FIRST_DATES = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31")
        val FIRST_DAYS = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_createinterview_second, container, false)
        initView(rootView, savedInstanceState)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        updateViews()
        changed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("layout1Expanded", layout1.isExpanded)
        outState.putBoolean("layout2Expanded", layout2.isExpanded)
        super.onSaveInstanceState(outState)
    }

    private fun changed() {
        (activity as CreateInterviewActivity).canMoveOn = true
    }

    private fun updateViews() {
        chips_interval.setSelectedItems(listOf((activity as CreateInterviewActivity).schedule.intervalType.ordinal))
        tv_selected_interval.text = INTERVALS[(activity as CreateInterviewActivity).schedule.intervalType.ordinal]
        val month = (activity as CreateInterviewActivity).schedule.startDate.get(Calendar.MONTH)
        chips_first_month.setSelectedItems(listOf(month))
        chips_first_date.setSelectedItems(listOf((activity as CreateInterviewActivity).schedule.startDate.get(Calendar.DATE)-1))
        chips_first_date.getSelectedItemIndices().firstOrNull()?.let { selectedDateIndex ->
            if (selectedDateIndex >= MAX_DATES_IN_MONTHS[month])
                chips_first_date.setSelectedItems(listOf(MAX_DATES_IN_MONTHS[month]-1))
        }
        chips_first_date.items = FIRST_DATES.take(MAX_DATES_IN_MONTHS[month])
        chips_first_day.setSelectedItems(listOf(
                (activity as CreateInterviewActivity).schedule.startDate.get(Calendar.DAY_OF_WEEK)-1
        ))
        showOccurrencePickersForInterval((activity as CreateInterviewActivity).schedule.intervalType)
    }

    private fun initView(root: View, savedInstanceState: Bundle?) {
        val intervalPicker = root.findViewById<ChipMultiPickerView>(R.id.chips_interval)
        intervalPicker.items = INTERVALS
        intervalPicker.selectionChangedListener = {
            tv_selected_interval.text = INTERVALS[it.first()]
            showOccurrencePickersForInterval(IntervalType.values()[it.first()])
            (activity as CreateInterviewActivity).schedule.intervalType = IntervalType.values()[it.first()]
            changed()
        }
        val etX = root.findViewById<EditText>(R.id.et_x)
        etX.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tv_selected_interval.text = INTERVALS[intervalPicker.getSelectedItemIndices().first()]
                        .replace("X", etX.text.toString())
                (activity as CreateInterviewActivity).schedule.x = etX.text.toString().toInt()
                changed()
            }

        })
        val firstMonthPicker = root.findViewById<ChipMultiPickerView>(R.id.chips_first_month)
        val firstDayPicker = root.findViewById<ChipMultiPickerView>(R.id.chips_first_day)
        val firstDatePicker = root.findViewById<ChipMultiPickerView>(R.id.chips_first_date)
        firstMonthPicker.items = FIRST_MONTHS
        firstDatePicker.items = FIRST_DATES
        firstDayPicker.items = FIRST_DAYS
        firstMonthPicker.selectionChangedListener = {
            (activity as CreateInterviewActivity).schedule.startDate.set(Calendar.MONTH, it.first())
            firstDatePicker.getSelectedItemIndices().firstOrNull()?.let { selectedDateIndex ->
                if (selectedDateIndex >= MAX_DATES_IN_MONTHS[it.first()])
                    firstDatePicker.setSelectedItems(listOf(MAX_DATES_IN_MONTHS[it.first()]-1))
            }
            firstDatePicker.items = FIRST_DATES.take(MAX_DATES_IN_MONTHS[it.first()])
            changed()
        }
        firstDatePicker.selectionChangedListener = {
            (activity as CreateInterviewActivity).schedule.startDate.set(Calendar.DATE, it.first())
            changed()
        }
        firstDayPicker.selectionChangedListener = {
            (activity as CreateInterviewActivity).schedule.startDate.timeInMillis = 0
            (activity as CreateInterviewActivity).schedule.startDate.set(Calendar.DAY_OF_WEEK, it.first()+1)
            changed()
        }
        savedInstanceState?.let {
            val l1 = root.findViewById<ExpansionLayout>(R.id.layout1)
            val l2 = root.findViewById<ExpansionLayout>(R.id.layout2)
            l1.expand(false)
            l1.collapse(false)
            l2.expand(false)
            l2.collapse(false)
            if (it.getBoolean("layout1Expanded"))
                l1.expand(false)
            if (it.getBoolean("layout2Expanded"))
                l2.expand(false)
        }
    }

    private fun showOccurrencePickersForInterval(intervalType: IntervalType) {
        tv_info_intervalfirst.visibility = View.GONE
        when (intervalType) {
            IntervalType.MONTHS -> {
                tv_hint_first_month.visibility = View.VISIBLE
                chips_first_month.visibility = View.VISIBLE
                tv_hint_first_date.visibility = View.VISIBLE
                chips_first_date.visibility = View.VISIBLE
                tv_hint_first_day.visibility = View.GONE
                chips_first_day.visibility = View.GONE
            }
            IntervalType.WEEKS -> {
                tv_hint_first_month.visibility = View.GONE
                chips_first_month.visibility = View.GONE
                tv_hint_first_date.visibility = View.GONE
                chips_first_date.visibility = View.GONE
                tv_hint_first_day.visibility = View.VISIBLE
                chips_first_day.visibility = View.VISIBLE
            }
            IntervalType.DAYS -> {
                tv_hint_first_month.visibility = View.GONE
                chips_first_month.visibility = View.GONE
                tv_hint_first_date.visibility = View.VISIBLE
                chips_first_date.visibility = View.VISIBLE
                tv_hint_first_day.visibility = View.GONE
                chips_first_day.visibility = View.GONE
            }
        }
    }
}
