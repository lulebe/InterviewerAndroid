package de.lulebe.interviewer.ui.questionCreation

import android.support.v4.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.lulebe.interviewer.CreateQuestionActivity
import de.lulebe.interviewer.R


class CreateQuestionAnswersTimeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_questions, container, false)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        changed()
        updateViews()
    }

    private fun changed() {
        val act = (activity as CreateQuestionActivity)
        act.canMoveOn = true
    }

    private fun updateViews() {

    }
}