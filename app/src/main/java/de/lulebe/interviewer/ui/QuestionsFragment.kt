package de.lulebe.interviewer.ui


import android.os.Bundle
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.lulebe.interviewer.CreateQuestionActivity

import de.lulebe.interviewer.R


class QuestionsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_questions, container, false)
        rootView.findViewById<View>(R.id.fab_add_question).setOnClickListener {
            startActivity(Intent(activity, CreateQuestionActivity::class.java))
        }
        return rootView
    }

}
