package de.lulebe.interviewer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        val gsclient = GoogleSignIn.getClient(this, gso)
        btn_gsignin.setOnClickListener {
            startActivityForResult(gsclient.signInIntent, 19)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 19) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignIn(task)
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleSignIn(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            val name = account.displayName ?: return
            tv_name.text = name
        } catch (e: ApiException) {
            Log.w("SIGNIN", e.localizedMessage)
        }
    }
}
