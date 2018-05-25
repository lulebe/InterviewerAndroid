package de.lulebe.interviewer.ui.questionCreation

import android.support.v4.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import de.lulebe.interviewer.CreateQuestionActivity
import de.lulebe.interviewer.R
import de.lulebe.interviewer.data.QuestionDataMC
import de.lulebe.interviewer.ui.adapters.CreateMCOptionsAdapter
import kotlinx.android.synthetic.main.fragment_createquestion_answers_mc.*
import java.util.*


class CreateQuestionAnswersMCFragment : Fragment() {

    private val mAdapter = CreateMCOptionsAdapter({
        deleteOption(it)
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_createquestion_answers_mc, container, false)
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
        act.canMoveOn = act.questionData.size >= 2
    }

    private fun updateViews() {
        val act = (activity as CreateQuestionActivity)
        mAdapter.notifyDataSetChanged()
        if (!act.questionData.isEmpty() && (act.questionData[0] as QuestionDataMC).allowMultiSelect)
            cb_multiselect.isChecked = true
    }

    private fun initViews(root: View) {
        val rv = root.findViewById<RecyclerView>(R.id.rv_options)
        val cbMultiSelect = root.findViewById<CheckBox>(R.id.cb_multiselect)
        val etNewoption = root.findViewById<EditText>(R.id.et_newoption)
        val cbIsSuccess = root.findViewById<CheckBox>(R.id.cb_newoption_success)
        val btnAddoption = root.findViewById<View>(R.id.btn_newoption_add)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = mAdapter
        mAdapter.items = (activity as CreateQuestionActivity).questionData
        cbMultiSelect.setOnCheckedChangeListener { _, checked ->
            (activity as CreateQuestionActivity).questionData.forEach {
                (it as QuestionDataMC).allowMultiSelect = checked
            }
            changed()
        }
        btnAddoption.setOnClickListener {
            val qd = (activity as CreateQuestionActivity).questionData
            qd.add(QuestionDataMC(
                    UUID.randomUUID(),
                    (activity as CreateQuestionActivity).question.id,
                    etNewoption.text.toString(),
                    cbIsSuccess.isChecked,
                    cbMultiSelect.isChecked
            ))
            mAdapter.notifyDataSetChanged()
            etNewoption.setText("")
            cbIsSuccess.isChecked = false
            changed()
        }
    }

    private fun deleteOption(questionDataMC: QuestionDataMC) {
        val qd = (activity as CreateQuestionActivity).questionData
        qd.remove(questionDataMC)
        mAdapter.notifyDataSetChanged()
        changed()
    }
}