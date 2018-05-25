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
import android.widget.RadioGroup
import de.lulebe.interviewer.CreateQuestionActivity
import de.lulebe.interviewer.R
import de.lulebe.interviewer.data.AnswerType
import de.lulebe.interviewer.ui.views.ChipMultiPickerView
import kotlinx.android.synthetic.main.fragment_createquestion_question.*


class CreateQuestionQuestionFragment : Fragment() {

    companion object {

        private val ANSWER_TYPES = listOf("Text", "Yes / No", "Number", "Time", "Duration", "Multiple choice", "Media")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_createquestion_question, container, false)
        initViews(rootView)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        updateViews()
        changed()
    }

    private fun changed() {
        val act = (activity as CreateQuestionActivity)
        act.canMoveOn = act.question.question.isNotEmpty()
    }

    private fun updateViews() {
        val act = (activity as CreateQuestionActivity)
        et_question.setText(act.question.question)
        chips_answertype.setSelectedItems(listOf(act.question.answerType.ordinal))
    }

    private fun initViews(root: View) {
        root.findViewById<EditText>(R.id.et_question).addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                (activity as CreateQuestionActivity).question.question = s.toString()
                changed()
            }
        })
        val chipsAnswertype = root.findViewById<ChipMultiPickerView>(R.id.chips_answertype)
        chipsAnswertype.items = ANSWER_TYPES
        chipsAnswertype.selectionChangedListener = {
            (activity as CreateQuestionActivity).question.answerType = AnswerType.values()[it.first()]
            (activity as CreateQuestionActivity).questionData.clear()
            changed()
        }
    }

}
