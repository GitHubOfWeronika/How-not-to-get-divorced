package com.example.how_not_to_get_divorced.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.how_not_to_get_divorced.R
import java.lang.Math.random
import kotlin.math.*


class Histogram(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    var color: Int = Color.CYAN
        set(color) {
            borderPaint = Paint()
            borderPaint.color = color
            borderPaint.strokeWidth = 12F
            fPaint = Paint()
            fPaint.color = color
            fPaint.strokeWidth = 20F
            histPaint = Paint()
            histPaint.color = color
            histPaint.alpha = 64
            field = color
        }
    private var isFSet = false
    var f : ((Float) -> Float) = {
        //(1.0/sqrt(2*PI)* exp(-it*it/2.0)).toFloat()
        1.0F
    }
        set(f: ((Float) -> Float)){
            field = f
            isFSet = true
            adjustF()
        }

    private var popupTextPaint: Paint = Paint()
    private var popupBackgroundPaint: Paint = Paint()
    private var bottomPaint: Paint = Paint()
    private lateinit var borderPaint: Paint
    private lateinit var fPaint: Paint
    private lateinit var histPaint: Paint
    var displayRangeNumber : (Float) -> String = {
        "%.1f".format(it)
    }
    var displayRange : (Float, Float) -> String = { r1, r2 ->
        displayRangeNumber(r1) + " - " + displayRangeNumber(r2)
    }
    val topMarginPopup = 20F
    val paddingPopup = 50F
    var histogramStep = 0.5F
        set(v){
            field = v
            manualStep = true
            adjustHistogram()
            invalidate()
        }
    private var manualStep = false
    private var pixelsForHist = 600F/8F
    private var hist = arrayOf(0)
    var histogramData : Array<Float> = Array(1000) {random().toFloat()}
        set(v){
            field = v
            adjustHistogram()
            invalidate()
        }
    private var histScale : Float = 1.0F / (hist.sum() * 0.5F)
    private val labelX = 12
    private val labelY = 60
    var minX = -2F
        set(v){
            field = v
            manualX = true
            adjustHistogram()
            if (isFSet) adjustF()
            invalidate()
        }
    var maxX = 2F
        set(v){
            field = v
            manualX = true
            adjustHistogram()
            if (isFSet) adjustF()
            invalidate()
        }
    private var manualX = false
    private val minY = 0F
    private var maxY = 4F
    private var a = 1F
    private var b = 0F
    private var pixelsOfGraph = 600
    private var graphPoints : Array<Float> = Array(pixelsOfGraph) {
        minX + (maxX - minX) * it.toFloat() / pixelsOfGraph.toFloat()
    }
    private var fPoints = graphPoints.map{ x -> f(x) }
    private var popupText : String? = null
    private var popupPath : Path? = null
    private var popupX : Float? = null
    private var popupRanges : String? = null
    private var popupBottomX : Float? = null

    init {
        val attr: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.Histogram)
        try {
            val c = attr.getColor(R.styleable.Histogram_color, Color.CYAN)
            color = c
            if (attr.hasValue(R.styleable.Histogram_histogramStep)){
                histogramStep = attr.getFloat(R.styleable.Histogram_histogramStep, 1.0F)
            }
            if (attr.hasValue(R.styleable.Histogram_maxX)){
                maxX =  attr.getFloat(R.styleable.Histogram_maxX, 1.0F)
            }
            if (attr.hasValue(R.styleable.Histogram_minX)){
                minX =  attr.getFloat(R.styleable.Histogram_minX, 1.0F)
            }
        } finally {
            attr.recycle()
        }
        popupTextPaint.textSize = 80F
        popupTextPaint.color = Color.WHITE
        popupTextPaint.textAlign
        popupBackgroundPaint.color = Color.DKGRAY
        bottomPaint.color = Color.DKGRAY
        bottomPaint.textSize = 60F
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            for (i in hist.indices) {
                drawRect(
                    i.toFloat() * pixelsForHist + labelX, a * hist[i] * histScale + b,
                    (i + 1).toFloat() * pixelsForHist + labelX, height.toFloat() - labelY, histPaint
                )
            }
            if (isFSet) {
                for (i in (1 until pixelsOfGraph)) {
                    this.drawLine(
                        (i - 1).toFloat() + labelX, a * (fPoints[i - 1]) + b,
                        i.toFloat() + labelX, a * (fPoints[i]) + b, fPaint
                    )
                }
            }
            this.drawLine(
                labelX.toFloat(), 0F, labelX.toFloat(),
                height.toFloat() - labelY, borderPaint
            )
            this.drawLine(
                width.toFloat(), height.toFloat() - labelY,
                labelX.toFloat(), height.toFloat() - labelY, borderPaint
            )
            val text = popupText
            val path = popupPath
            val posX = popupX
            val rangesText = popupRanges
            val bottomX = popupBottomX
            if (text != null && path != null && posX != null && rangesText != null && bottomX != null){
                drawPath(path, popupBackgroundPaint)
                drawText(
                    text,
                    posX,
                    popupTextPaint.textSize + paddingPopup + topMarginPopup - 10F,
                    popupTextPaint
                )
                drawText(rangesText, bottomX, height.toFloat(), bottomPaint)
            } else {
                drawText(displayRangeNumber(minX), 0F, height.toFloat(), bottomPaint)
                val end = displayRangeNumber(maxX)
                drawText(
                    end,
                    width.toFloat() - bottomPaint.measureText(end),
                    height.toFloat(),
                    bottomPaint
                )
            }
        }
    }

    fun countRanges(numOfRanges: Int, data: Array<Float>, selector: (Float) -> Int): Array<Int> {
        val result = Array(numOfRanges) {0}
        for (num in data){
            val index = selector(num)
            if (index in 0 until numOfRanges) {
                result[index] += 1
            }
        }
        return result
    }

    private fun adjustF(){
        pixelsOfGraph = width - labelX
        if (pixelsOfGraph > 0) {
            graphPoints = Array(pixelsOfGraph) {
                minX + (maxX - minX) * it.toFloat() / pixelsOfGraph.toFloat()
            }
            fPoints = graphPoints.map { x -> f(x) }
        }
    }

    private fun adjustHistogram(){
        val space = width - labelX
        if (space <= 0) return
        if (histogramData.isEmpty()) {
            hist = arrayOf(0)
            if (!manualX) {
                minX = 0.00001F
                maxX = 1F
            }
            if (!manualStep) {
                histogramStep = 1F
            }
            histScale = 0F
            pixelsForHist = space.toFloat()
            maxY = 1F
        } else {
            val max = histogramData.maxOrNull()!!
            val min = histogramData.minOrNull()!!
            if (!manualX) {
                if (max != min) {
                    minX = min - 0.00001F
                    maxX = max + 0.00001F
                } else {
                    minX = 0.00001F
                    maxX = max + 0.00001F
                }
            }
            val range = maxX - minX
            if (!manualStep) {
                histogramStep = range / (space / 50)
            }
            val steps = round(range / histogramStep).toInt()
            hist = countRanges(steps, histogramData) {
                floor((it - minX) / (histogramStep)).toInt()
            }
            histScale = 1.0F / (histogramData.size * histogramStep)
            maxY = hist.maxOrNull()!! * histScale * 1.1F
            pixelsForHist = space.toFloat() / steps.toFloat()
        }
    }

    private fun setPopup(text: String, bottomText: String, x: Float){
        val path = Path()
        val padding = paddingPopup
        val corners = floatArrayOf(
            80f, 80f,   // Top left radius in px
            80f, 80f,   // Top right radius in px
            80f, 80f,     // Bottom right radius in px
            80f, 80f      // Bottom left radius in px
        )
        val size = popupTextPaint.measureText(text) + padding * 2
        val pos = when {
            x < size/2F + 20F -> 20F
            x + size/2F > width - 20F -> width - size - 20F
            else -> x - size/2F
        }
        path.addRoundRect(
            RectF(
                pos, topMarginPopup, pos + size,
                popupTextPaint.textSize + topMarginPopup + padding * 2
            ), corners,
            Path.Direction.CW
        )
        popupPath = path
        popupText = text
        popupX = pos + padding
        popupRanges = bottomText
        val bottomSize = bottomPaint.measureText(bottomText)
        popupBottomX = when {
            x < bottomSize/2F -> 0F
            x + bottomSize/2F > width -> width - bottomSize
            else -> x - bottomSize/2F
        }
    }

    private fun unsetPopup(){
        popupText = null
    }

    private fun getHistogram(x: Int): Int? {
        val pos = floor((x - labelX) / pixelsForHist).toInt()
        return if (pos in hist.indices) {
            pos
        } else {
            null
        }
    }

    private fun getHistogramRanges(pos: Int): Pair<Float, Float>? {
        return if (pos in hist.indices) {
            Pair(minX + pos * histogramStep, minX + (pos + 1) * histogramStep)
        } else {
            null
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pos = getHistogram(event.x.toInt())
        if (pos == null) {
            unsetPopup()
            invalidate()
            return true
        }
        val (r1, r2) = getHistogramRanges(pos)!!
        if (event.action == MotionEvent.ACTION_DOWN) {
            setPopup(hist[pos].toString(), displayRange(r1, r2), event.x)
        } else if (event.action == MotionEvent.ACTION_MOVE) {
            unsetPopup()
            setPopup(hist[pos].toString(), displayRange(r1, r2), event.x)
        } else if (event.action == MotionEvent.ACTION_UP) {
            unsetPopup()
            performClick()
        }
        invalidate()
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        adjustHistogram()
        if (isFSet) adjustF()
        a = (height-labelY-4)/(minY - maxY)
        b = -a * maxY
    }

}
