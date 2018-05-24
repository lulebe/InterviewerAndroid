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

class CreateInterviewSecondFragment : Fragment() {

    companion object {
        val INTERVALS = listOf("Every X months", "Every X weeks", "Every X days")
        val FIRST_MONTHS = listOf("Jan", "Feb", "Mar", "Apr", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val FIRST_DATES = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31")
        val FIRST_DAYS = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
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
        super.onSaveInstanceState(outState)
    }

    private fun changed() {
        (activity as CreateInterviewActivity).canMoveOn = true
    }

    private fun updateViews() {
    }

    private fun initView(root: View, savedInstanceState: Bundle?) {
        val intervalPicker = root.findViewById<ChipMultiPickerView>(R.id.chips_interval)
        intervalPicker.items = INTERVALS
        intervalPicker.selectionChangedListener = {
            tv_selected_interval.text = INTERVALS[it.first()]
            showOccurrencePickersForInterval(IntervalType.values()[it.first()])
        }
        val etX = root.findViewById<EditText>(R.id.et_x)
        etX.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tv_selected_interval.text = INTERVALS[intervalPicker.getSelectedItemIndices().first()]
                        .replace("X", etX.text.toString())
            }

        })
        val firstMonthPicker = root.findViewById<ChipMultiPickerView>(R.id.chips_first_month)
        firstMonthPicker.items = FIRST_MONTHS
        firstMonthPicker.selectionChangedListener = {

        }
        val firstDatePicker = root.findViewById<ChipMultiPickerView>(R.id.chips_first_date)
        firstDatePicker.items = FIRST_DATES
        firstDatePicker.selectionChangedListener = {

        }
        val firstDayPicker = root.findViewById<ChipMultiPickerView>(R.id.chips_first_day)
        firstDayPicker.items = FIRST_DAYS
        firstDayPicker.selectionChangedListener = {

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
