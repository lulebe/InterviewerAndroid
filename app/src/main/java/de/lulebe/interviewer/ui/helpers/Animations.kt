package de.lulebe.interviewer.ui.helpers

import android.animation.Animator
import android.view.View

fun View.fadeIn() {
    this.alpha = 0F
    this.visibility = View.VISIBLE
    this.animate()
            .alpha(1F)
            .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong()).start()
}

fun View.fadeOut() {
    this.alpha = 1F
    this.visibility = View.VISIBLE
    this.animate()
            .alpha(0F)
            .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
            .setListener(object: Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    this@fadeOut.visibility = View.GONE
                }
            })
}