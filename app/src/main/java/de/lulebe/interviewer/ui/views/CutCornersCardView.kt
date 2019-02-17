package de.lulebe.interviewer.ui.views

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.MotionEventCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import de.lulebe.interviewer.R

class CutCornersCardView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private val bgPaint = Paint()
    private var bgPath = Path()
    private val densityFactor = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1F, context.resources.displayMetrics)
    private val animator = ValueAnimator.ofFloat(16 * densityFactor, 32 * densityFactor)
    private val defaultColor = ResourcesCompat.getColor(resources, R.color.cardBackground, null)
    private val menuColor = ResourcesCompat.getColor(resources, R.color.cardBackgroundMenu, null)

    private var menuVisible = false
    fun isMenuVisible() = menuVisible

    init {
        bgPaint.color = defaultColor
        bgPaint.style = Paint.Style.FILL

        animator.duration = 70
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener {
            setCornerCuts(it.animatedValue as Float)
        }
        setOnLongClickListener {
            if (findViewWithTag<View>("menu") != null && findViewWithTag<View>("content") != null) {
                performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                toggleMenu()
                return@setOnLongClickListener true
            }
            false
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(bgPath, bgPaint)
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        setCornerCuts(16 * densityFactor)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun setCornerCuts(cutSize: Float) {
        bgPath = Path()
        bgPath.moveTo(0F, 0F)
        bgPath.lineTo(width - cutSize, 0F)
        bgPath.lineTo(width.toFloat(), cutSize)
        bgPath.lineTo(width.toFloat(), height.toFloat())
        bgPath.lineTo(cutSize, height.toFloat())
        bgPath.lineTo(0F, height - cutSize)
        bgPath.close()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            if (it.action == MotionEvent.ACTION_DOWN) {
                animateDown()
            } else if (it.action == MotionEvent.ACTION_CANCEL || it.action == MotionEvent.ACTION_UP) {
                animateUp()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun animateDown() {
        animator.start()
    }

    private fun animateUp() {
        animator.reverse()
    }

    fun toggleMenu() {
        if (menuVisible)
            hideMenu()
        else
            showMenu()
    }

    fun hideMenu() {
        if (findViewWithTag<View>("menu") == null || findViewWithTag<View>("content") == null)
            return
        menuVisible = false
        val menu = findViewWithTag<View>("menu")
        val content = findViewWithTag<View>("content")
        content.visibility = View.VISIBLE
        content.animate().setDuration(100).alpha(1F).setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                menu.visibility = View.GONE
            }
        }).start()
        menu.alpha = 1F
        menu.animate().setDuration(100).alpha(0F).start()
        val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), menuColor, defaultColor)
        colorAnimator.duration = 100
        colorAnimator.addUpdateListener {
            bgPaint.color = it.animatedValue as Int
            invalidate()
        }
        colorAnimator.start()
    }

    fun showMenu() {
        if (findViewWithTag<View>("menu") == null || findViewWithTag<View>("content") == null)
            return
        menuVisible = true
        val menu = findViewWithTag<View>("menu")
        val content = findViewWithTag<View>("content")
        content.animate().setDuration(100).alpha(0F).setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                content.visibility = View.GONE
                menu.visibility = View.VISIBLE
            }
        }).start()
        menu.alpha = 0F
        menu.animate().setDuration(100).alpha(1F).start()
        val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), defaultColor, menuColor)
        colorAnimator.duration = 100
        colorAnimator.addUpdateListener {
            bgPaint.color = it.animatedValue as Int
            invalidate()
        }
        colorAnimator.start()
    }
}