package de.lulebe.interviewer.ui.questionCreation

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.lulebe.interviewer.CreateQuestionActivity
import de.lulebe.interviewer.R
import de.lulebe.interviewer.data.QuestionDataBoolean
import de.lulebe.interviewer.ui.views.ChipMultiPickerView
import kotlinx.android.synthetic.main.fragment_createquestion_answers_boolean.*
import java.util.*


class CreateQuestionAnswersBooleanFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_createquestion_answers_boolean, container, false)
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
            act.questionData.add(QuestionDataBoolean(
                    UUID.randomUUID(),
                    act.question.id,
                    true
            ))
        act.canMoveOn = true
    }

    private fun updateViews() {
        val data = (activity as CreateQuestionActivity).questionData[0] as QuestionDataBoolean
        chips_successvalue.setSelectedItems(listOf(if (data.successValue) 0 else 1))
    }

    private fun initViews(root: View) {
        val chipsSuccessvalue = root.findViewById<ChipMultiPickerView>(R.id.chips_successvalue)
        chipsSuccessvalue.items = listOf("Yes", "No")
        chipsSuccessvalue.selectionChangedListener = {
            val data = (activity as CreateQuestionActivity).questionData[0] as QuestionDataBoolean
            data.successValue = it.first() == 0
            changed()
        }
    }
}