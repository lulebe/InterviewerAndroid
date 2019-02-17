package de.lulebe.interviewer.ui.answerInput

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.lulebe.interviewer.R
import de.lulebe.interviewer.data.*
import kotlinx.android.synthetic.main.fragment_answerinput_boolean.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class NumberAnswerFragment : Fragment(), AnswerFragment {
    private var answer: Answer? = null
    private var answerData: AnswerDataBoolean? = null
    private var answerIsNew = false
    private var mQuestionId = UUID.randomUUID()
    private var mInterviewId = UUID.randomUUID()
    private var mTime = Calendar.getInstance()
    private var mQuestionData: QuestionDataBoolean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_answerinput_boolean, container, false)
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
        ansData.data = cb_answer.isChecked
        ans.success = cb_answer.isChecked == mQuestionData?.successValue
        doAsync {
            context?.applicationContext?.let { appCtx ->
                val db = AppDatabase.getDatabase(appCtx)
                if (ansIsNew) {
                    db.answerDao().createAnswer(ans)
                    db.answerDataBooleanDao().createAnswerDataBoolean(ansData)
                } else {
                    db.answerDao().updateAnswer(ans)
                    db.answerDataBooleanDao().updateAnswerDataBoolean(ansData)
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
            mQuestionData = db.questionDataBooleanDao().getQuestionDataForQuestion(mQuestionId)
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

    private fun getDataForAnswer(db: AppDatabase, answer: Answer) : AnswerDataBoolean {
        var answerData = db.answerDataBooleanDao().getDataForAnswer(answer.id)
        if (answerData == null)
            answerData = AnswerDataBoolean(
                    UUID.randomUUID(),
                    answer.id,
                    true
            )
        return answerData
    }

    private fun initViews() {
        cb_answer.isChecked = answerData?.data ?: false
    }
}