package de.lulebe.interviewer.ui.interviewCreation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.lulebe.interviewer.CreateInterviewActivity
import de.lulebe.interviewer.R
import kotlinx.android.synthetic.main.fragment_createinterview_fourth.*

class CreateInterviewFourthFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_createinterview_fourth, container, false)
        initViews(rootView)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        updateViews()
        changed()
    }

    private fun changed() {
        (activity as CreateInterviewActivity).canMoveOn = true
    }

    private fun updateViews() {
        val act = activity as CreateInterviewActivity
        tv_name.text = act.interview.name
    }

    private fun initViews(root: View) {

    }
}
