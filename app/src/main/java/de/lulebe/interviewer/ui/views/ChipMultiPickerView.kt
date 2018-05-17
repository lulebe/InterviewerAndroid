package de.lulebe.interviewer.ui.views

import android.content.Context
import android.gesture.Gesture
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.content.res.ResourcesCompat
import android.text.DynamicLayout
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import de.lulebe.interviewer.R
import org.jetbrains.anko.collections.forEachWithIndex

class ChipMultiPickerView : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private val selectedItemIndices = mutableListOf<Int>()
    var selectionChangedListener: ((List<Int>) -> Unit)? = null

    private var _items = emptyList<String>()
    var items: List<String>
        get() = _items
        set(value) {
            _items = value
            requestLayout()
        }
    private var itemLayouts = emptyList<Pair<StaticLayout, StaticLayout>>()

    private val COLOR_UI = ResourcesCompat.getColor(resources, R.color.colorAccent, null)
    private val COLOR_TEXT = Color.parseColor("#333333")
    private val BLOCK_PADDING = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F, resources.displayMetrics)
    private val CORNERCUT_SIZE = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6F, resources.displayMetrics)
    private val CORNERCUT_SIZE_SELECTED = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12F, resources.displayMetrics)
    private val BLOCK_MARGIN = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F, resources.displayMetrics)
    private val TEXT_Y_ADJUSTMENT = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2F, resources.displayMetrics)
    private var mTextWidth = 0F
    private var mTextHeight = 0F
    private var mBlockWidth = 0F
    private var mBlockHeight = 0F
    private var mElementsPerRow = 0
    private var mStartPadding = 0

    private val mBlockPaint = Paint()
    private val mBlockSelectedPaint = Paint()
    private val mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val mTextSelectedPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val mBlockPath = Path()
    private val mBlockSelectedPath = Path()

    init {
        mBlockPaint.color = COLOR_UI
        mBlockPaint.style = Paint.Style.STROKE
        mBlockPaint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2F, resources.displayMetrics)
        mBlockSelectedPaint.color = COLOR_UI
        mBlockSelectedPaint.style = Paint.Style.FILL_AND_STROKE
        mBlockSelectedPaint.strokeWidth = mBlockPaint.strokeWidth
        mTextPaint.color = COLOR_TEXT
        mTextPaint.typeface = ResourcesCompat.getFont(context, R.font.nunito)
        mTextPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16F, resources.displayMetrics)
        mTextSelectedPaint.color = Color.WHITE
        mTextSelectedPaint.typeface = ResourcesCompat.getFont(context, R.font.nunito)
        mTextSelectedPaint.textSize = mTextPaint.textSize
    }

    fun setSelectedItems (selected: List<Int>) {
        selectedItemIndices.clear()
        selectedItemIndices.addAll(selected)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMeasure = MeasureSpec.getSize(widthMeasureSpec)
        calculateTextDimensions()
        mBlockWidth = mTextWidth + (2 * mBlockPaint.strokeWidth) + (2 * BLOCK_PADDING)
        mBlockHeight = mTextHeight + (2 * mBlockPaint.strokeWidth) + (2 * BLOCK_PADDING)
        mElementsPerRow = Math.floor(widthMeasure.toDouble() / (mBlockWidth + (2 * BLOCK_MARGIN))).toInt()
        mStartPadding = ((widthMeasure - (mElementsPerRow * (mBlockWidth + (2 * BLOCK_MARGIN)))) / 2).toInt()
        val rowCount = Math.ceil(items.size / mElementsPerRow.toDouble()).toInt()
        val heightMeasure = (rowCount * (mBlockHeight + (2 * BLOCK_MARGIN))).toInt()
        generateItemLayouts()
        setMeasuredDimension(widthMeasure, heightMeasure)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBlockPath.apply {
            reset()
            moveTo(0F, 0F)
            lineTo(mBlockWidth - CORNERCUT_SIZE, 0F)
            lineTo(mBlockWidth, CORNERCUT_SIZE)
            lineTo(mBlockWidth, mBlockHeight)
            lineTo(CORNERCUT_SIZE, mBlockHeight)
            lineTo(0F, mBlockHeight - CORNERCUT_SIZE)
            close()
        }
        mBlockSelectedPath.apply {
            reset()
            moveTo(0F, 0F)
            lineTo(mBlockWidth - CORNERCUT_SIZE_SELECTED, 0F)
            lineTo(mBlockWidth, CORNERCUT_SIZE_SELECTED)
            lineTo(mBlockWidth, mBlockHeight)
            lineTo(CORNERCUT_SIZE_SELECTED, mBlockHeight)
            lineTo(0F, mBlockHeight - CORNERCUT_SIZE_SELECTED)
            close()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            items.forEachWithIndex { i, _ ->
                val column = i % mElementsPerRow
                val row = i / mElementsPerRow
                save()
                translate(
                        column * (mBlockWidth + (2 * BLOCK_MARGIN)) + BLOCK_MARGIN + mStartPadding,
                        row * (mBlockHeight + (2 * BLOCK_MARGIN)) + BLOCK_MARGIN
                )
                if (selectedItemIndices.contains(i)) {
                    drawPath(mBlockSelectedPath, mBlockSelectedPaint)
                    translate(0F, BLOCK_PADDING + TEXT_Y_ADJUSTMENT)
                    itemLayouts[i].second.draw(this)
                } else {
                    drawPath(mBlockPath, mBlockPaint)
                    translate(0F, BLOCK_PADDING + TEXT_Y_ADJUSTMENT)
                    itemLayouts[i].first.draw(this)
                }
                restore()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null && event.action == MotionEvent.ACTION_DOWN)
            return true
        if (event != null && event.action == MotionEvent.ACTION_UP) {
            val clickedIndex = getClickedChipIndex(event.x, event.y) ?: return true
            clickedIndex(clickedIndex)
        }
        return super.onTouchEvent(event)
    }

    private fun calculateTextDimensions() {
        val textLayout = StaticLayout(items[0], mTextPaint, 1000, Layout.Alignment.ALIGN_NORMAL, 1F, 0F, true)
        mTextWidth = textLayout.getLineWidth(0)
        mTextHeight = textLayout.height.toFloat()
    }

    private fun generateItemLayouts() {
        itemLayouts = items.map {
            Pair(
                    StaticLayout(
                            it,
                            mTextPaint,
                            mBlockWidth.toInt(),
                            Layout.Alignment.ALIGN_CENTER,
                            1F,
                            0F,
                            false
                    ),
                    StaticLayout(
                            it,
                            mTextSelectedPaint,
                            mBlockWidth.toInt(),
                            Layout.Alignment.ALIGN_CENTER,
                            1F,
                            0F,
                            false
                    )
            )
        }
    }

    private fun getClickedChipIndex(x: Float, y: Float) : Int? {
        val depaddedX = x - mStartPadding
        val wholeElementWidth = mBlockWidth + (2 * BLOCK_MARGIN)
        val wholeElementHeight = mBlockHeight + (2 * BLOCK_MARGIN)
        val column = Math.floor(depaddedX / wholeElementWidth.toDouble()).toInt()
        if (column < 0)
            return null
        val row = Math.floor(y / wholeElementHeight.toDouble()).toInt()
        return column + row * mElementsPerRow
    }

    private fun clickedIndex (index: Int) {
        if (selectedItemIndices.contains(index))
            selectedItemIndices.remove(index)
        else
            selectedItemIndices.add(index)
        selectionChangedListener?.invoke(selectedItemIndices.toList())
        invalidate()
    }

}