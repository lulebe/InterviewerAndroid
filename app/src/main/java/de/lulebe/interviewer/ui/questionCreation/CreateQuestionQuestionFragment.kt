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
import kotlinx.android.synthetic.main.fragment_createquestion_question.*


class CreateQuestionQuestionFragment : Fragment() {

    companion object {

        private val radioIds = mapOf(
                Pair(R.id.radio_answertype_text, AnswerType.TEXT),
                Pair(R.id.radio_answertype_bool, AnswerType.BOOLEAN),
                Pair(R.id.radio_answertype_number, AnswerType.NUMBER),
                Pair(R.id.radio_answertype_time, AnswerType.TIME),
                Pair(R.id.radio_answertype_duration, AnswerType.DURATION),
                Pair(R.id.radio_answertype_mc, AnswerType.MC)
        )

        private val answerTypes = mapOf(
                Pair(AnswerType.TEXT, R.id.radio_answertype_text),
                Pair(AnswerType.BOOLEAN, R.id.radio_answertype_bool),
                Pair(AnswerType.NUMBER, R.id.radio_answertype_number),
                Pair(AnswerType.TIME, R.id.radio_answertype_time),
                Pair(AnswerType.DURATION, R.id.radio_answertype_duration),
                Pair(AnswerType.MC, R.id.radio_answertype_mc)
        )

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
        answerTypes[act.question.answerType]?.let {
            radio_answertype.check(it)
        }
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
        root.findViewById<RadioGroup>(R.id.radio_answertype).setOnCheckedChangeListener { radioGroup, i ->
            radioIds[i]?.let {
                (activity as CreateQuestionActivity).question.answerType = it
                (activity as CreateQuestionActivity).questionData.clear()
                changed()
            }
        }
    }

}
