package de.lulebe.interviewer.ui.interviewCreation

import android.support.v4.app.Fragment
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import de.lulebe.interviewer.CreateInterviewActivity
import de.lulebe.interviewer.R

class CreateInterviewFirstFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_createinterview_first, container, false)
        rootView.findViewById<EditText>(R.id.et_name).addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                (activity as CreateInterviewActivity).interview.name = s.toString()
                changed()
            }
        })
        return rootView
    }

    override fun onStart() {
        super.onStart()
        changed()
        updateViews()
    }

    private fun changed() {
        val act = (activity as CreateInterviewActivity)
        act.canMoveOn = act.interview.name.isNotEmpty()
    }

    private fun updateViews() {
        val interview = (activity as CreateInterviewActivity).interview
        view?.findViewById<EditText>(R.id.et_name)?.setText(interview.name)
    }
}
