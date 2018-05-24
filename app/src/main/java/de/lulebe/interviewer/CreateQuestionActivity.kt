package de.lulebe.interviewer

import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import de.lulebe.interviewer.data.AnswerType
import de.lulebe.interviewer.data.AppDatabase
import de.lulebe.interviewer.data.Question
import de.lulebe.interviewer.data.QuestionData
import de.lulebe.interviewer.ui.questionCreation.*
import kotlinx.android.synthetic.main.activity_create_question.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class CreateQuestionActivity : AppCompatActivity() {

    companion object {
        const val MAX_FRAGMENT_NUMBER = 2
    }

    private var mCurrentFragment = 0
    var question = Question(UUID.randomUUID(), UUID.randomUUID(),"", AnswerType.BOOLEAN, 0)
    val questionData = mutableListOf<QuestionData>()
    private var _canMoveOn = false
    var canMoveOn: Boolean
        get() = _canMoveOn
        set(value) {
            _canMoveOn = value
            if (value) {
                btn_next.setBackgroundResource(R.drawable.bg_buttoncreate_enabled)
                btn_next.foreground = foregroundBtnNext
                if (mCurrentFragment < MAX_FRAGMENT_NUMBER)
                    iv_nextbtn.setImageResource(R.drawable.ic_chevron_right_whitetransparent_24dp)
                else
                    iv_nextbtn.setImageResource(R.drawable.ic_check_whitetransparent_24dp)
            } else {
                btn_next.setBackgroundResource(R.drawable.bg_buttoncreate_disabled)
                btn_next.foreground = null
                iv_nextbtn.setImageResource(R.drawable.ic_warning_red_24dp)
            }
        }

    private var foregroundBtnNext : Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_question)
        initViews(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        supportActionBar?.elevation = 0F
        val out = TypedValue()
        theme.resolveAttribute(android.R.attr.selectableItemBackground, out, true)
        foregroundBtnNext = ResourcesCompat.getDrawable(resources, out.resourceId, theme)
        question.interviewId = UUID.fromString(intent.extras.getString("interviewId"))
    }

    private fun initViews(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container_create_pages, getFragmentNo(0))
                    .commit()
        btn_next.setOnClickListener {
            if (mCurrentFragment < MAX_FRAGMENT_NUMBER && canMoveOn) {
                mCurrentFragment++
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container_create_pages, getFragmentNo(mCurrentFragment))
                        .commit()
                steps.go(mCurrentFragment, true)
                setupViewsForPage(mCurrentFragment)
            } else if (mCurrentFragment == MAX_FRAGMENT_NUMBER && canMoveOn)
                saveQuestion()
        }
        btn_back.setOnClickListener {
            if (mCurrentFragment > 0) {
                mCurrentFragment--
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container_create_pages, getFragmentNo(mCurrentFragment))
                        .commit()
                steps.go(mCurrentFragment, true)
                setupViewsForPage(mCurrentFragment)
            }
        }
    }

    private fun getFragmentNo(number: Int) = when (number) {
        0 -> CreateQuestionQuestionFragment()
        1 -> {
            when (question.answerType) {
                AnswerType.TEXT -> CreateQuestionAnswersTextFragment()
                AnswerType.TIME -> CreateQuestionAnswersTimeFragment()
                AnswerType.DURATION -> CreateQuestionAnswersDurationFragment()
                AnswerType.NUMBER -> CreateQuestionAnswersNumberFragment()
                AnswerType.MC -> CreateQuestionAnswersMCFragment()
                AnswerType.BOOLEAN -> CreateQuestionAnswersBooleanFragment()
            }
        }
        else -> CreateQuestionReviewFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun setupViewsForPage (page: Int) {
        if (page == 0) {
            val animation = btn_back.animate().scaleX(0.6F).scaleY(0.6F).alpha(0F)
                    .setDuration(150).setInterpolator(AccelerateInterpolator())
            animation.withEndAction {
                btn_back.visibility = View.GONE
            }
            animation.start()
        }
        if (page > 0 && btn_back.visibility == View.GONE) {
            btn_back.visibility = View.VISIBLE
            btn_back.alpha = 0F
            btn_back.scaleX = 0.6F
            btn_back.scaleY = 0.6F
            btn_back.animate().scaleX(1F).scaleY(1F).alpha(1F).setDuration(150)
                    .setInterpolator(DecelerateInterpolator())
                    .start()
        }
        when (page) {
            0 -> tv_btn_next_caption.setText(R.string.createquestion_nextcaption_0)
            1 -> tv_btn_next_caption.setText(R.string.createquestion_nextcaption_1)
            2 -> tv_btn_next_caption.setText(R.string.createquestion_nextcaption_2)
        }
        if (page == MAX_FRAGMENT_NUMBER) {
            iv_nextbtn.setImageResource(R.drawable.ic_check_whitetransparent_24dp)
        } else {
            iv_nextbtn.setImageResource(R.drawable.ic_chevron_right_whitetransparent_24dp)
        }
    }

    private fun saveQuestion() {
        doAsync {
            val db = AppDatabase.getDatabase(applicationContext)
            db.questionDao().createQuestion(question)
            questionData.forEach {
                it.insertInto(db)
            }
            uiThread {
                supportFinishAfterTransition()
            }
        }
    }

}
