package de.lulebe.interviewer

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import de.lulebe.interviewer.ui.QuestionsFragment
import de.lulebe.interviewer.ui.SettingsFragment
import de.lulebe.interviewer.ui.StatsFragment
import kotlinx.android.synthetic.main.activity_interview.*
import java.util.*

class InterviewActivity : AppCompatActivity() {

    companion object {
        const val FRAGMENT_STATS = 1
        const val FRAGMENT_LIST = 2
        const val FRAGMENT_SETTINGS = 3
    }

    private val fragments = WeakHashMap<Int, Fragment>()
    private var mCurrentFragmentNo = 0

    private var interviewId : UUID? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val f = when (item.itemId) {
            R.id.navigation_list -> FRAGMENT_LIST
            R.id.navigation_settings -> FRAGMENT_SETTINGS
            else -> FRAGMENT_STATS
        }
        if (f == mCurrentFragmentNo) return@OnNavigationItemSelectedListener false
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
            val fragmentNo = intent.getIntExtra("page", FRAGMENT_LIST)
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
        interviewId = UUID.fromString(intent.getStringExtra("interviewId"))
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.let {
            it.putInt("selectedBottomnavItem", navigation.selectedItemId)
            it.putInt("currentFragmentNo", mCurrentFragmentNo)
        }
        super.onSaveInstanceState(outState)
    }

    fun getInterviewId() = interviewId

    private fun getFragment(fragment: Int) : Fragment {
        fragments[fragment]?.let {
            return it
        }
        val f = when (fragment) {
            FRAGMENT_LIST -> QuestionsFragment()
            FRAGMENT_SETTINGS -> SettingsFragment()
            else -> StatsFragment()
        }
        fragments[fragment] = f
        return f
    }

    private fun getBottomNavItemForFragment(fragmentNo: Int) = when(fragmentNo) {
        FRAGMENT_LIST -> R.id.navigation_list
        FRAGMENT_SETTINGS -> R.id.navigation_settings
        else -> R.id.navigation_stats
    }
}
