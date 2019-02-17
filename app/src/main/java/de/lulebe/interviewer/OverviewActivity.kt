package de.lulebe.interviewer

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import de.lulebe.interviewer.data.AppDatabase
import de.lulebe.interviewer.data.Interview
import de.lulebe.interviewer.data.media.getTotalStorage
import de.lulebe.interviewer.data.media.getUsedStorage
import de.lulebe.interviewer.ui.adapters.InterviewsAdapter
import de.lulebe.interviewer.ui.helpers.fadeIn
import kotlinx.android.synthetic.main.activity_overview.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.doAsync

class OverviewActivity : AppCompatActivity() {

    private val clickCallback = { interview: Interview ->
        val interviewIntent = Intent(this, InterviewActivity::class.java)
        interviewIntent.putExtra("interviewId", interview.id.toString())
        startActivity(interviewIntent)
    }

    private val editCallback = { interview: Interview ->
        val interviewIntent = Intent(this, InterviewActivity::class.java)
        interviewIntent.putExtra("interviewId", interview.id.toString())
        interviewIntent.putExtra("page", InterviewActivity.FRAGMENT_SETTINGS)
        startActivity(interviewIntent)
    }

    private val deleteCallback = { interview: Interview ->
        deleteInterview(interview)
    }

    private val mInterviewsAdapter = InterviewsAdapter (clickCallback, editCallback, deleteCallback)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        supportActionBar?.elevation = 0F
        setupViews()
    }

    override fun onStart() {
        super.onStart()
        resetFab()
        loadInterviews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_overview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menuitem_settings -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setupViews() {
        fab.setOnClickListener {
            val createInterviewIntent = Intent(this, CreateInterviewActivity::class.java)
            iv_fab.animate().setDuration(70).alpha(0F).start()
            fab.animate().setDuration(70).rotation(0F).start()
            val scaleFactor = contentView!!.width.toFloat() / fab.width.toFloat()
            val translationY = contentView!!.height.toFloat() - (fab.layoutParams as FrameLayout.LayoutParams).topMargin - fab.height
            val translationX = (contentView!!.width.toFloat() - fab.width.toFloat()) / 2F - (fab.layoutParams as FrameLayout.LayoutParams).marginEnd
            fab.animate()
                    .setDuration(200).setStartDelay(70)
                    .scaleX(scaleFactor).scaleY(1.2F).translationY(translationY).translationX(-translationX)
                    .withEndAction({
                        startActivity(createInterviewIntent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    })
                    .start()
        }
        rv_interviews.adapter = mInterviewsAdapter
        rv_interviews.layoutManager = GridLayoutManager(this, resources.getInteger(R.integer.overview_columns))
        val usedStorage = getUsedStorage(this)
        val totalStorage = getTotalStorage()
        storage_bar.progress = (usedStorage.toFloat() / totalStorage.toFloat() * 100.0).toInt()
        tv_storage.text = "Used ${usedStorage / 1000000} MB of ${totalStorage / 1000000} MB."
    }

    private fun loadInterviews() {
        val interviewDao = AppDatabase.getDatabase(applicationContext).interviewDao()
        val interviews = interviewDao.getAllInterviews()
        interviews.observe(this, Observer<List<Interview>> {
            mInterviewsAdapter.setNewList(it)
            if (it == null || it.isEmpty()) {
                rv_interviews.visibility = View.GONE
                l_empty.fadeIn()
            } else {
                rv_interviews.visibility = View.VISIBLE
                l_empty.visibility = View.GONE
            }
        })
    }

    private fun deleteInterview(interview: Interview) {
        doAsync {
            AppDatabase.getDatabase(applicationContext).interviewDao().deleteInterview(interview)
        }
    }

    private fun resetFab() {
        iv_fab.alpha = 1F
        fab.translationX = 0F
        fab.translationY = 0F
        fab.rotation = 45F
        fab.scaleX = 1F
        fab.scaleY = 1F
    }
}
