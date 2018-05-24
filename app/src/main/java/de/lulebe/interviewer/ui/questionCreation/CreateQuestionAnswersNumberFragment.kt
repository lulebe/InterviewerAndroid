package de.lulebe.interviewer.ui.questionCreation

import android.support.v4.app.Fragment
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import de.lulebe.interviewer.CreateQuestionActivity
import de.lulebe.interviewer.R
import de.lulebe.interviewer.data.QuestionDataNumber
import de.lulebe.interviewer.ui.views.ChipMultiPickerView
import kotlinx.android.synthetic.main.fragment_createquestion_answers_number.*
import java.util.*


class CreateQuestionAnswersNumberFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_createquestion_answers_number, container, false)
        initViews(rootView)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        changed()
        updateViews()
    }

    private fun changed() {
        val act = (activity as CreateQuestionActivity)
        if (act.questionData.isEmpty())
            act.questionData.add(QuestionDataNumber(
                    UUID.randomUUID(),
                    act.question.id,
                    0F,
                    10F,
                    5F,
                    QuestionDataNumber.SuccessType.gt
            ))
        act.canMoveOn = true
    }

    private fun updateViews() {
        val data = (activity as CreateQuestionActivity).questionData[0] as QuestionDataNumber
        et_min.setText(data.min.toString())
        et_max.setText(data.max.toString())
        et_success.setText(data.successThreshold.toString())
        chips_successtype.setSelectedItems(if (data.successType != null) listOf(data.successType!!.ordinal) else emptyList())
    }

    private fun initViews(root: View) {
        val etMin = root.findViewById<EditText>(R.id.et_min)
        val etMax = root.findViewById<EditText>(R.id.et_max)
        val etSuccessvalue = root.findViewById<EditText>(R.id.et_success)
        val chipsSuccesstype = root.findViewById<ChipMultiPickerView>(R.id.chips_successtype)
        chipsSuccesstype.items = listOf("at least . . .", "at most . . .", "exactly . . .")
        chipsSuccesstype.selectionChangedListener = {
            val data = (activity as CreateQuestionActivity).questionData[0] as QuestionDataNumber
            data.successType = QuestionDataNumber.SuccessType.values()[it.first()]
            changed()
        }
        etMin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val data = (activity as CreateQuestionActivity).questionData[0] as QuestionDataNumber
                if (etMin.text.isEmpty()) {
                    data.min = null
                    changed()
                    return
                }
                data.min = etMin.text.toString().toFloat()
                if (data.max != null && data.min!! >= data.max!!) {
                    data.max = data.min!! + 1
                    etMax.setText(data.max!!.toString())
                }
                if (data.successThreshold != null && data.successThreshold!! < data.min!!) {
                    data.successThreshold = data.min
                    etSuccessvalue.setText(data.successThreshold!!.toString())
                }
                changed()
            }
        })
        etMax.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val data = (activity as CreateQuestionActivity).questionData[0] as QuestionDataNumber
                if (etMax.text.isEmpty()) {
                    data.max = null
                    changed()
                    return
                }
                data.max = etMax.text.toString().toFloat()
                if (data.min != null && data.max!! <= data.min!!) {
                    data.min = data.max!! - 1
                    etMin.setText(data.min!!.toString())
                }
                if (data.successThreshold != null && data.successThreshold!! > data.max!!) {
                    data.successThreshold = data.max
                    etSuccessvalue.setText(data.successThreshold!!.toString())
                }
                changed()
            }
        })
        etSuccessvalue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val data = (activity as CreateQuestionActivity).questionData[0] as QuestionDataNumber
                if (etSuccessvalue.text.isEmpty()) {
                    data.successThreshold = null
                    changed()
                    return
                }
                data.successThreshold = etSuccessvalue.text.toString().toFloat()
                if (data.min != null && data.successThreshold!! < data.min!!) {
                    data.successThreshold = data.min
                    etSuccessvalue.setText(data.successThreshold!!.toString())
                } else if (data.max != null && data.successThreshold!! > data.max!!) {
                    data.successThreshold = data.max
                    etSuccessvalue.setText(data.successThreshold!!.toString())
                }
                changed()
            }
        })
    }
}