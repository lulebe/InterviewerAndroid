package de.lulebe.interviewer

import android.support.v4.app.Fragment
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import de.lulebe.interviewer.background.NotificationWorker
import de.lulebe.interviewer.data.*
import de.lulebe.interviewer.helpers.bundleToList
import de.lulebe.interviewer.helpers.listToBundle
import de.lulebe.interviewer.ui.interviewCreation.CreateInterviewFirstFragment
import de.lulebe.interviewer.ui.interviewCreation.CreateInterviewSecondFragment
import de.lulebe.interviewer.ui.interviewCreation.CreateInterviewThirdFragment

import kotlinx.android.synthetic.main.activity_create_interview.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class CreateInterviewActivity : AppCompatActivity() {
    companion object {
        const val MAX_FRAGMENT_NUMBER = 2
    }

    private var mCurrentFragment = 0
    var interview = Interview(UUID.randomUUID(), "", ColorScheme.BUSINESS)
    var notificationsList = listOf(
            Notifications(UUID.randomUUID(), interview.id, emptyList(), emptyList(), emptyList(), emptyList(), emptyList())
    )
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
        setContentView(R.layout.activity_create_interview)
        initViews(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        supportActionBar?.elevation = 0F
        val out = TypedValue()
        theme.resolveAttribute(android.R.attr.selectableItemBackground, out, true)
        foregroundBtnNext = ResourcesCompat.getDrawable(resources, out.resourceId, theme)
        window.setBackgroundDrawable(ColorDrawable(Color.parseColor("#444444")))
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.let {
            it.putInt("currentFragment", mCurrentFragment)
            it.putBundle("interview", interview.toBundle())
            it.putBundle("notificationsList", listToBundle(notificationsList.map { it.toBundle() }))
        }
        super.onSaveInstanceState(outState)
    }

    private fun initViews(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container_create_pages, getFragmentNo(0))
                    .commit()
            tv_btn_next_caption.setText(R.string.createquestion_nextcaption_0)
            btn_back.visibility = View.GONE
        } else {
            interview = Interview.fromBundle(savedInstanceState.getBundle("interview"))
            notificationsList = bundleToList(savedInstanceState.getBundle("notificationsList"))
                    .map {Notifications.fromBundle(it) }
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
                mCurrentFragment--
                supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.mainfragments_in_from_left, R.anim.mainfragments_out_to_right)
                        .replace(R.id.container_create_pages, getFragmentNo(mCurrentFragment))
                        .commit()
                steps.go(mCurrentFragment, true)
                setupViewsForPage(mCurrentFragment)
            }
        }
    }

    private fun getFragmentNo(number: Int) : Fragment = when (number) {
        0 -> CreateInterviewFirstFragment()
        1 -> CreateInterviewSecondFragment()
        else -> CreateInterviewThirdFragment()
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
            notificationsList.forEach {
                db.notificationsDao().createNotifications(it)
            }
            NotificationWorker.enqueueNextNotification(applicationContext)
            uiThread {
                val interviewIntent = Intent(this@CreateInterviewActivity, InterviewActivity::class.java)
                interviewIntent.putExtra("interviewId", interview.id)
                startActivity(interviewIntent)
                finish()
            }
        }
    }
}
