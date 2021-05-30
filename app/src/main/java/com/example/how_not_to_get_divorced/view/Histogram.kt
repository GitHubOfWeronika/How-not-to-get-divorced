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
    var color: Int = Color.CYAN // Kolor naszego wyresu
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
    private var isFSet = false // czy mamy oczekiwany rozkład
    var f : ((Float) -> Float) = { 1.0F } //Funkcja oczekiwanego rozkąłd
        set(f: ((Float) -> Float)){
            field = f
            isFSet = true
            adjustF()
        }
    // Używane farby
    private var popupTextPaint: Paint = Paint()
    private var popupBackgroundPaint: Paint = Paint()
    private var bottomPaint: Paint = Paint()
    private lateinit var borderPaint: Paint
    private lateinit var fPaint: Paint
    private lateinit var histPaint: Paint
    var displayRangeNumber : (Float) -> String = { // Funkcja do ustawienia sposobu wyświetlania danych na osi X
        "%.1f".format(it)
    }
    var displayRange : (Float, Float) -> String = { r1, r2 -> // Funkcja do ustawienia sposobu wyświetlania danych przedziałów
        displayRangeNumber(r1) + " - " + displayRangeNumber(r2)
    }
    private val topMarginPopup = 20F // Odkegłość dymka od górnej granicy
    private val paddingPopup = 50F // Padding w dymku
    private var histogramStep = 0.5F // Określa jaki przedział obejmuje pojedyńczy słupek w histogramie
    private var manualStep = false // czy histogramStep oztsał ustwainy przez użytkowika, czy jest automatyczny
    private var pixelsForHist = 1F // Liczba pikseli na jeden słupek histogramu
    private var hist = arrayOf(0) // wyskokości w histogramie
    var histogramData : Array<Float> = Array(1000) {random().toFloat()} // dane do wyliczenia histogramu
        set(v){
            field = v
            adjustHistogram()
            invalidate()
        }
    private var histScale : Float = 1.0F / (hist.sum() * 0.5F) // dostosowuje wysokość wistogramu do wysokości oczekiwanego rozkłądu
    private val labelX = 12 // pixele na margines po lewej
    private val labelY = 60 // pixele na margines w dole
    private var minX = 0F // zakres wyświetlania wykresu na osi X
    private var maxX = 1F
    private var manualX = false // czy zakres x postał ustawiny ręcznie
    private val minY = 0F // zakres wyświetlania wykresu na osi Y
    private var maxY = 4F
    private var a = 1F // stała kierunkowa przeliczenia pozycji y na pixele
    private var b = 0F // wyraz wolny przeliczenia pozycji y na pixele
    private var pixelsOfGraph = 600 //pixele na wyres
    private var graphPoints : Array<Float> = Array(pixelsOfGraph) { // wartość x dla każdego pixela
        minX + (maxX - minX) * it.toFloat() / pixelsOfGraph.toFloat()
    }
    private var fPoints = graphPoints.map{ x -> f(x) } // wartość y dla każdego pixela
    private var popupText : String? = null // dane dotyczące dymku z szczegółowymi informacjami
    private var popupPath : Path? = null
    private var popupX : Float? = null
    private var popupRanges : String? = null
    private var popupBottomX : Float? = null

    init {
        val attr: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.Histogram) // usawianie parametrów na podsatwie attrybutów z XML'a
        try {
            val c = attr.getColor(R.styleable.Histogram_color, Color.CYAN)
            color = c
            if (attr.hasValue(R.styleable.Histogram_histogramStep)){
                histogramStep = attr.getFloat(R.styleable.Histogram_histogramStep, 1.0F)
                manualStep = true
            }
            if (attr.hasValue(R.styleable.Histogram_maxX)){
                maxX =  attr.getFloat(R.styleable.Histogram_maxX, 1.0F)
                manualX = true
            }
            if (attr.hasValue(R.styleable.Histogram_minX)){
                minX =  attr.getFloat(R.styleable.Histogram_minX, 1.0F)
                manualX = true
            }
        } finally {
            attr.recycle()
        }
        // przygotowanie paintów
        popupTextPaint.textSize = 80F
        popupTextPaint.color = Color.WHITE
        popupTextPaint.textAlign
        popupBackgroundPaint.color = Color.DKGRAY
        bottomPaint.color = Color.DKGRAY
        bottomPaint.textSize = 60F
    }

    fun setXAxis(max: Float, min: Float){
        maxX = max
        minX = min
        manualX = true
        adjustHistogram()
        if (isFSet) adjustF()
        invalidate()
    }

    fun setStep(step: Float){
        histogramStep = step
        manualStep = true
        adjustHistogram()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            // rysuje histogram
            for (i in hist.indices) {
                drawRect(
                    i.toFloat() * pixelsForHist + labelX, a * hist[i] * histScale + b,
                    (i + 1).toFloat() * pixelsForHist + labelX, height.toFloat() - labelY, histPaint
                )
            }
            // rysuje oczekiwany rozkład
            if (isFSet) {
                for (i in (1 until pixelsOfGraph)) {
                    this.drawLine(
                        (i - 1).toFloat() + labelX, a * (fPoints[i - 1]) + b,
                        i.toFloat() + labelX, a * (fPoints[i]) + b, fPaint
                    )
                }
            }
            // rysuje obramowanie
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
                // rysyuje dymek
                drawPath(path, popupBackgroundPaint)
                drawText(
                    text,
                    posX,
                    popupTextPaint.textSize + paddingPopup + topMarginPopup - 10F,
                    popupTextPaint
                )
                // rysuje zakres na osi X
                drawText(rangesText, bottomX, height.toFloat(), bottomPaint)
            } else {
                // rysuje zakres na osi X
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

    /**
     * Zlicza ile elementów znajduje się w poszczególnych przedziałach
     * numOfRanges - liczba predziałów
     * data - dane do zliczenia
     * selector - funkcja generująca indeks do zliczenia
     */

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

    /**
     * Przygotowuje dane do wyświetlenia oczekiwanego rozkładu
     */
    private fun adjustF(){
        pixelsOfGraph = width - labelX
        if (pixelsOfGraph > 0) {
            graphPoints = Array(pixelsOfGraph) {
                minX + (maxX - minX) * it.toFloat() / pixelsOfGraph.toFloat()
            }
            fPoints = graphPoints.map { x -> f(x) }
        }
    }

    /**
     * Przygotowuje dane do wyświetlenia histogramu
     */
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
                    //histogram ma wiecej niż 1 wartość
                    minX = min - 0.001F
                    maxX = max + 0.001F
                } else {
                    minX = 0.001F
                    maxX = max + 0.01F
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
            a = (height-labelY-4)/(minY - maxY)
            b = -a * maxY
        }
    }

    /**
     * Przygotowyje dane do wyścietlenia dymku
     */
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

    /**
     * Ustawia dane do ukrycia dymku
     */
    private fun unsetPopup(){
        popupText = null
    }

    /**
     * Na podstwaie pikela na osi X wyznacza odpowiadający mu kolumnę histogramu
     */
    private fun getHistogram(x: Int): Int? {
        val pos = floor((x - labelX) / pixelsForHist).toInt()
        return if (pos in hist.indices) {
            pos
        } else {
            null
        }
    }

    /**
     * Oblicza zakres wartosci dla wybranej kolomny histogramu
     */
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
