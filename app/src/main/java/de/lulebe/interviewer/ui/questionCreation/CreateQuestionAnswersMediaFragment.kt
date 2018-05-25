package de.lulebe.interviewer.ui.questionCreation

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.lulebe.interviewer.CreateQuestionActivity
import de.lulebe.interviewer.R
import de.lulebe.interviewer.data.QuestionDataText
import java.util.*


class CreateQuestionAnswersMediaFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_createquestion_answers_media, container, false)
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
            act.questionData.add(QuestionDataText(
                    UUID.randomUUID(),
                    act.question.id
            ))
        act.canMoveOn = true
    }

    private fun updateViews() {

    }
}