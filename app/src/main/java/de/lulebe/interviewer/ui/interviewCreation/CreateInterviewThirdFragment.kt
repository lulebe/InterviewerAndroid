package de.lulebe.interviewer.ui.interviewCreation

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.lulebe.interviewer.CreateInterviewActivity
import de.lulebe.interviewer.R
import kotlinx.android.synthetic.main.fragment_createinterview_third.*

class CreateInterviewThirdFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_createinterview_third, container, false)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        changed()
        updateViews()
    }

    private fun changed() {
        (activity as CreateInterviewActivity).canMoveOn = true
    }

    private fun updateViews() {
        tv_name.text = (activity as CreateInterviewActivity).interview.name
    }
}
