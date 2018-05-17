package de.lulebe.interviewer.ui.questionCreation

import android.support.v4.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.lulebe.interviewer.CreateQuestionActivity
import de.lulebe.interviewer.R


class CreateQuestionReviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_questions, container, false)
        return rootView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        changed()
    }

    private fun changed() {
        val act = (activity as CreateQuestionActivity)
        act.canMoveOn = act.question.question.isNotEmpty()
    }
}