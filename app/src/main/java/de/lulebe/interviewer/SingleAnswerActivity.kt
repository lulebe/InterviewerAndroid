package de.lulebe.interviewer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import de.lulebe.interviewer.data.AnswerType
import de.lulebe.interviewer.data.AppDatabase
import de.lulebe.interviewer.ui.answerInput.AnswerFragment
import de.lulebe.interviewer.ui.answerInput.TextAnswerFragment
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class SingleAnswerActivity : AppCompatActivity() {

    var shownFragment: AnswerFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_answer)
        initFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_singleanswer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        R.id.menuitem_save -> {
            shownFragment?.submit()
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun initFragment() {
        val interviewId = UUID.fromString(intent.getStringExtra("interviewId"))
        val questionId = UUID.fromString(intent.getStringExtra("questionId"))
        doAsync {
            val db = AppDatabase.getDatabase(applicationContext)
            val question = db.questionDao().getQuestionById(questionId)
            uiThread {
                val fragment = getFragmentForType(question.answerType)
                shownFragment = fragment
                supportFragmentManager.beginTransaction()
                        .add(R.id.container, fragment)
                        .commit()
                fragment.setArgs(questionId, interviewId, null)
            }
        }
    }

    private fun getFragmentForType(answerType: AnswerType) = when (answerType) {
        AnswerType.TEXT -> TextAnswerFragment()
        else -> TextAnswerFragment()
    }
}
