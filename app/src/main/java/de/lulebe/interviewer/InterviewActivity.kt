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

    private val fragments = WeakHashMap<Int, Fragment>()
    private val FRAGMENT_LIST = 1
    private val FRAGMENT_STATS = 2
    private val FRAGMENT_SETTINGS = 3

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val f = when (item.itemId) {
            R.id.navigation_list -> FRAGMENT_LIST
            R.id.navigation_settings -> FRAGMENT_SETTINGS
            else -> FRAGMENT_STATS
        }
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.mainfragments_in, R.animator.mainfragments_out)
                .replace(R.id.fragment_page, getFragment(f))
                .commit()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_page, StatsFragment())
                    .commit()
        else {
            navigation.selectedItemId = savedInstanceState.getInt("selectedBottomnavItem")
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.let {
            it.putInt("selectedBottomnavItem", navigation.selectedItemId)
        }
        super.onSaveInstanceState(outState)
    }

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
}
