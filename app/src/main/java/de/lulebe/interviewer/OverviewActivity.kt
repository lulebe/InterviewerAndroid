package de.lulebe.interviewer

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import de.lulebe.interviewer.data.AppDatabase
import de.lulebe.interviewer.data.Interview
import de.lulebe.interviewer.ui.adapters.InterviewsAdapter
import de.lulebe.interviewer.ui.helpers.fadeIn
import kotlinx.android.synthetic.main.activity_overview.*
import org.jetbrains.anko.doAsync

class OverviewActivity : AppCompatActivity() {

    private val clickCallback = { interview: Interview ->
        val interviewIntent = Intent(this, InterviewActivity::class.java)
        interviewIntent.putExtra("interviewId", interview.id)
        startActivity(interviewIntent)
    }

    private val editCallback = { interview: Interview ->
        Toast.makeText(this, "edit: ${interview.name}", Toast.LENGTH_SHORT).show()
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
            startActivity(Intent(this, CreateInterviewActivity::class.java))
        }
        rv_interviews.adapter = mInterviewsAdapter
        rv_interviews.layoutManager = GridLayoutManager(this, resources.getInteger(R.integer.overview_columns))
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
}
