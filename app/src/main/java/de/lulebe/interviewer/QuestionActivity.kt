package de.lulebe.interviewer

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import de.lulebe.interviewer.ui.question.QuestionAnswersFragment
import de.lulebe.interviewer.ui.question.QuestionSettingsFragment
import kotlinx.android.synthetic.main.activity_interview.*
import java.util.*

class QuestionActivity : AppCompatActivity() {
    companion object {
        const val FRAGMENT_ANSWERS = 1
        const val FRAGMENT_SETTINGS = 2
    }

    private val fragments = WeakHashMap<Int, Fragment>()
    private var mCurrentFragmentNo = 0

    private var questionId : UUID? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val f = when (item.itemId) {
            R.id.navigation_settings -> FRAGMENT_SETTINGS
            else -> FRAGMENT_ANSWERS
        }
        if (f == mCurrentFragmentNo) return@OnNavigationItemSelectedListener true
        val animIn = if (f > mCurrentFragmentNo) R.anim.mainfragments_in_from_right else R.anim.mainfragments_in_from_left
        val animOut = if (f > mCurrentFragmentNo) R.anim.mainfragments_out_to_left else R.anim.mainfragments_out_to_right
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(animIn, animOut)
                .replace(R.id.fragment_page, getFragment(f))
                .commit()
        mCurrentFragmentNo = f
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState == null) {
            val fragmentNo = intent.getIntExtra("page", FRAGMENT_ANSWERS)
            mCurrentFragmentNo = fragmentNo
            val newFragment = getFragment(fragmentNo)
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_page, newFragment)
                    .commit()
            navigation.selectedItemId = getBottomNavItemForFragment(fragmentNo)
        } else {
            mCurrentFragmentNo = savedInstanceState.getInt("currentFragmentNo")
            navigation.selectedItemId = savedInstanceState.getInt("selectedBottomnavItem")
        }
        questionId = UUID.fromString(intent.getStringExtra("questionId"))
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.let {
            it.putInt("selectedBottomnavItem", navigation.selectedItemId)
            it.putInt("currentFragmentNo", mCurrentFragmentNo)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    fun getQuestionId() = questionId

    private fun getFragment(fragment: Int) : Fragment {
        fragments[fragment]?.let {
            return it
        }
        val f = when (fragment) {
            FRAGMENT_SETTINGS -> QuestionSettingsFragment()
            else -> QuestionAnswersFragment()
        }
        fragments[fragment] = f
        return f
    }

    private fun getBottomNavItemForFragment(fragmentNo: Int) = when(fragmentNo) {
        FRAGMENT_SETTINGS -> R.id.navigation_settings
        else -> R.id.navigation_answers
    }

}