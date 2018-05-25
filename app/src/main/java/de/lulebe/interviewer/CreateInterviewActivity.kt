package de.lulebe.interviewer

import android.support.v4.app.Fragment
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.text.format.DateUtils
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import de.lulebe.interviewer.data.*
import de.lulebe.interviewer.helpers.bundleToList
import de.lulebe.interviewer.helpers.listToBundle
import de.lulebe.interviewer.ui.interviewCreation.CreateInterviewFirstFragment
import de.lulebe.interviewer.ui.interviewCreation.CreateInterviewFourthFragment
import de.lulebe.interviewer.ui.interviewCreation.CreateInterviewSecondFragment
import de.lulebe.interviewer.ui.interviewCreation.CreateInterviewThirdFragment

import kotlinx.android.synthetic.main.activity_create_interview.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.time.Instant
import java.util.*

class CreateInterviewActivity : AppCompatActivity() {
    companion object {
        const val MAX_FRAGMENT_NUMBER = 3
    }

    private var mCurrentFragment = 0
    var interview = Interview(UUID.randomUUID(), "", ColorScheme.BUSINESS)
    var schedule = Schedule(UUID.randomUUID(), interview.id, IntervalType.DAY)
    var notification = Notification(UUID.randomUUID(), interview.id, 0, 20, 0)
    private var _canMoveOn = false
    var canMoveOn: Boolean
        get() = _canMoveOn
        set(value) {
            _canMoveOn = value
            if (value) {
                //btn_next.setBackgroundResource(R.drawable.bg_buttoncreate_enabled)
                btn_next.foreground = foregroundBtnNext
                if (mCurrentFragment < MAX_FRAGMENT_NUMBER)
                    iv_nextbtn.setImageResource(R.drawable.ic_chevron_right_whitetransparent_24dp)
                else
                    iv_nextbtn.setImageResource(R.drawable.ic_check_whitetransparent_24dp)
            } else {
                //btn_next.setBackgroundResource(R.drawable.bg_buttoncreate_disabled)
                btn_next.foreground = null
                iv_nextbtn.setImageResource(R.drawable.ic_warning_red_24dp)
            }
        }

    private var foregroundBtnNext : Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_interview)
        initViews(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        supportActionBar?.elevation = 0F
        val out = TypedValue()
        theme.resolveAttribute(android.R.attr.selectableItemBackground, out, true)
        foregroundBtnNext = ResourcesCompat.getDrawable(resources, out.resourceId, theme)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.let {
            it.putInt("currentFragment", mCurrentFragment)
            it.putBundle("interview", interview.toBundle())
            it.putBundle("schedule", schedule.toBundle())
            it.putBundle("notification", notification.toBundle())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (mCurrentFragment > 0)
            goBack()
        else
            super.onBackPressed()
    }

    private fun initViews(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container_create_pages, getFragmentNo(0))
                    .commit()
            tv_btn_next_caption.setText(R.string.createinterview_nextcaption_0)
            btn_back.visibility = View.GONE
        } else {
            interview = Interview.fromBundle(savedInstanceState.getBundle("interview"))
            schedule = Schedule.fromBundle(savedInstanceState.getBundle("schedule"))
            notification = Notification.fromBundle(savedInstanceState.getBundle("notification"))
            mCurrentFragment = savedInstanceState.getInt("currentFragment")
            steps.go(mCurrentFragment, false)
            setupViewsForPage(mCurrentFragment)
        }
        btn_next.setOnClickListener {
            if (mCurrentFragment < MAX_FRAGMENT_NUMBER && canMoveOn) {
                mCurrentFragment++
                supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.mainfragments_in_from_right, R.anim.mainfragments_out_to_left)
                        .replace(R.id.container_create_pages, getFragmentNo(mCurrentFragment))
                        .commit()
                steps.go(mCurrentFragment, true)
                setupViewsForPage(mCurrentFragment)
            } else if (mCurrentFragment == MAX_FRAGMENT_NUMBER && canMoveOn) {
                saveInterview()
            }
        }
        btn_back.setOnClickListener {
            if (mCurrentFragment > 0) {
                goBack()
            }
        }
    }

    private fun goBack() {
        mCurrentFragment--
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.mainfragments_in_from_left, R.anim.mainfragments_out_to_right)
                .replace(R.id.container_create_pages, getFragmentNo(mCurrentFragment))
                .commit()
        steps.go(mCurrentFragment, true)
        setupViewsForPage(mCurrentFragment)
    }

    private fun getFragmentNo(number: Int) : Fragment = when (number) {
        0 -> CreateInterviewFirstFragment()
        1 -> CreateInterviewSecondFragment()
        2 -> CreateInterviewThirdFragment()
        else -> CreateInterviewFourthFragment()
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
            0 -> tv_btn_next_caption.setText(R.string.createinterview_nextcaption_0)
            1 -> tv_btn_next_caption.setText(R.string.createinterview_nextcaption_1)
            2 -> tv_btn_next_caption.setText(R.string.createinterview_nextcaption_2)
            3 -> tv_btn_next_caption.setText(R.string.createinterview_nextcaption_3)
        }
        if (page == MAX_FRAGMENT_NUMBER) {
            iv_nextbtn.setImageResource(R.drawable.ic_check_whitetransparent_24dp)
        } else {
            iv_nextbtn.setImageResource(R.drawable.ic_chevron_right_whitetransparent_24dp)
        }
    }

    private fun saveInterview() {
        doAsync {
            val user = User.loadUser(applicationContext)
            val db = AppDatabase.getDatabase(applicationContext)
            db.interviewDao().createInterview(interview)
            db.interviewUserDao().createInterviewUser(InterviewUser(
                    UUID.randomUUID(),
                    user.id,
                    user.name,
                    interview.id,
                    true
            ))
            db.scheduleDao().createSchedule(schedule)
            db.notificationDao().createNotification(notification)
            uiThread {
                val interviewIntent = Intent(this@CreateInterviewActivity, InterviewActivity::class.java)
                interviewIntent.putExtra("interviewId", interview.id.toString())
                startActivity(interviewIntent)
                finish()
            }
        }
    }
}
