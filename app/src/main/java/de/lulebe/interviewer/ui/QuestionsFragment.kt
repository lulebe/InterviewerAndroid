package de.lulebe.interviewer.ui


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import de.lulebe.interviewer.*

import de.lulebe.interviewer.data.AppDatabase
import de.lulebe.interviewer.data.Question
import de.lulebe.interviewer.ui.adapters.AnswerListAdapter
import de.lulebe.interviewer.ui.adapters.QuestionsAdapter
import de.lulebe.interviewer.ui.helpers.fadeIn
import kotlinx.android.synthetic.main.fragment_questions.*
import org.jetbrains.anko.doAsync


class QuestionsFragment : Fragment() {

    private val mQuestionsAdapter = QuestionsAdapter({
        answerQuestion(it)
    }, {
        showAnswers(it)
    }, {
        editQuestion(it)
    }, {
        deleteQuestion(it)
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_questions, container, false)
        initViews(rootView)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        loadQuestions()
    }

    private fun initViews(root: View) {
        root.findViewById<View>(R.id.fab_add_question).setOnClickListener {
            val createQuestionIntent = Intent(activity, CreateQuestionActivity::class.java)
            createQuestionIntent.putExtra("interviewId", (activity as InterviewActivity).getInterviewId().toString())
            startActivity(createQuestionIntent)
        }
        val rv = root.findViewById<RecyclerView>(R.id.rv_questions)
        rv.adapter = mQuestionsAdapter
        rv.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.overview_columns))
    }

    private fun loadQuestions() {
        context?.applicationContext?.let { appCtx ->
            (activity as InterviewActivity).getInterviewId()?.let { interviewId ->
                val questionDao = AppDatabase.getDatabase(appCtx).questionDao()
                val questions = questionDao.getAllQuestionsForInterview(interviewId)
                questions.observe(this, Observer<List<Question>> {
                    mQuestionsAdapter.setNewList(it)
                    if (it == null || it.isEmpty()) {
                        rv_questions.visibility = View.GONE
                        l_empty.fadeIn()
                    } else {
                        rv_questions.visibility = View.VISIBLE
                        l_empty.visibility = View.GONE
                    }
                })
            }
        }
    }

    private fun answerQuestion(question: Question) {
        val answerInputIntent = Intent(context, SingleAnswerActivity::class.java)
        answerInputIntent.putExtra("interviewId", (activity as InterviewActivity).getInterviewId().toString())
        answerInputIntent.putExtra("questionId", question.id.toString())
        startActivity(answerInputIntent)
    }

    private fun showAnswers(question: Question) {
        val answersIntent = Intent(context, QuestionActivity::class.java)
        answersIntent.putExtra("page", QuestionActivity.FRAGMENT_ANSWERS)
        answersIntent.putExtra("questionId", question.id.toString())
        startActivity(answersIntent)
    }

    private fun editQuestion(question: Question) {
        Toast.makeText(context, "edit question: ${question.question}", Toast.LENGTH_SHORT).show()
    }

    private fun deleteQuestion(question: Question) {
        doAsync {
            context?.applicationContext?.let { appCtx ->
                AppDatabase.getDatabase(appCtx).questionDao().deleteQuestion(question)
            }
        }
    }

}
