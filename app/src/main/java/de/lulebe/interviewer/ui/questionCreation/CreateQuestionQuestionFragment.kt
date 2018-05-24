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
import de.lulebe.interviewer.CreateQuestionActivity
import de.lulebe.interviewer.R


class CreateQuestionQuestionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_createquestion_question, container, false)
        rootView.findViewById<EditText>(R.id.et_question).addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                (activity as CreateQuestionActivity).question.question = s.toString()
                changed()
            }
        })
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
