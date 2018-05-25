package de.lulebe.interviewer.ui.answerInput

import android.arch.lifecycle.Lifecycle
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.lulebe.interviewer.R
import de.lulebe.interviewer.data.*
import kotlinx.android.synthetic.main.fragment_answerinput_text.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class TextAnswerFragment : Fragment(), AnswerFragment {

    private var answer: Answer? = null
    private var answerData: AnswerDataText? = null
    private var answerIsNew = false
    private var mQuestionId = UUID.randomUUID()
    private var mInterviewId = UUID.randomUUID()
    private var mTime = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_answerinput_text, container, false)
    }

    override fun onStart() {
        super.onStart()
        loadAnswer()
    }

    override fun setArgs(questionId: UUID, interviewId: UUID, time: Calendar?) {
        mQuestionId = questionId
        mInterviewId = interviewId
        time?.let { mTime = it }
    }

    override fun submit() {
        val ans = answer!!
        val ansIsNew = answerIsNew
        val ansData = answerData!!
        ansData.data = et_answer.text.toString()
        ans.success = cb_isSuccess.isChecked
        doAsync {
            context?.applicationContext?.let { appCtx ->
                val db = AppDatabase.getDatabase(appCtx)
                if (ansIsNew) {
                    db.answerDao().createAnswer(ans)
                    db.anwserDataTextDao().createAnswerDataText(ansData)
                } else {
                    db.answerDao().updateAnswer(ans)
                    db.anwserDataTextDao().updateAnswerDataText(ansData)
                }
            }
        }
    }

    private fun loadAnswer() {
        val appCtx = context?.applicationContext ?: return
        doAsync {
            val db = AppDatabase.getDatabase(appCtx)
            val question = db.questionDao().getQuestionById(mQuestionId)
            val schedule = db.scheduleDao().getScheduleForInterview(mInterviewId)
            answer = db.answerDao().getAnswerForQuestionAtTime(question.id, mTime) ?: generateAnswer(question, schedule)
            answerData = getDataForAnswer(db, answer!!)
            uiThread {
                initViews()
            }
        }
    }

    private fun generateAnswer(question: Question, schedule: Schedule) : Answer {
        answerIsNew = true
        val now = Calendar.getInstance()
        val interval = schedule.getIntervalForTime(now)
        return Answer(
                UUID.randomUUID(),
                question.id,
                now,
                interval.first,
                interval.second,
                false
        )
    }

    private fun getDataForAnswer(db: AppDatabase, answer: Answer) : AnswerDataText {
        var answerData = db.anwserDataTextDao().getDataForAnswer(answer.id)
        if (answerData == null)
            answerData = AnswerDataText(
                    UUID.randomUUID(),
                    answer.id,
                    ""
            )
        return answerData
    }

    private fun initViews() {
        et_answer.setText(answerData!!.data)
        cb_isSuccess.isChecked = answer!!.success ?: false
    }

}