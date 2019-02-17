package de.lulebe.interviewer.ui.question


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.lulebe.interviewer.*
import de.lulebe.interviewer.data.AnswerWithData
import de.lulebe.interviewer.data.AppDatabase
import de.lulebe.interviewer.data.Question
import de.lulebe.interviewer.ui.adapters.AnswerListAdapter
import de.lulebe.interviewer.ui.helpers.fadeIn
import kotlinx.android.synthetic.main.fragment_question_answers.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class QuestionAnswersFragment : Fragment() {

    private val mAnswersAdapter = AnswerListAdapter()
    private var mQuestion: Question? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_question_answers, container, false)
        initViews(rootView)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun initViews(root: View) {
        val rv = root.findViewById<RecyclerView>(R.id.rv_answers)
        rv.adapter = mAnswersAdapter
        rv.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.overview_columns))
    }

    private fun loadData() {
        doAsync {
            context?.applicationContext?.let { appCtx ->
                (activity as QuestionActivity).getQuestionId()?.let { questionId ->
                    val questionDao = AppDatabase.getDatabase(appCtx).questionDao()
                    mQuestion = questionDao.getQuestionById(questionId)
                    context?.applicationContext?.let { appCtx ->
                        mQuestion?.let { question ->
                            val answers = question.getAnswersWithData(AppDatabase.getDatabase(appCtx))
                            uiThread {
                                mAnswersAdapter.setAnswerType(question.answerType)
                                mAnswersAdapter.setAnswerList(answers)
                                if (answers.isEmpty()) {
                                    rv_answers.visibility = View.GONE
                                    l_empty.fadeIn()
                                } else {
                                    rv_answers.visibility = View.VISIBLE
                                    l_empty.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
