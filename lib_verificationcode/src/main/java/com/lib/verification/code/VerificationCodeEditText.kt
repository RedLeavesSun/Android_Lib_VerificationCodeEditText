package com.lib.verification.code

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import java.util.*

/**
 * 验证码或者对话框输入控件
 *
 * @author: 笨小孩.
 * @date  : 2020/3/28.
 */
class VerificationCodeEditText : AppCompatEditText {

    companion object {
        const val TYPE_HOLLOW = 1 //空心
        const val TYPE_SOLID = 2 //实心
        const val TYPE_UNDERLINE = 3 //下划线
    }

    /**方块之间间隙*/
    var spacing = 0
        set(value) {
            field = value
            postInvalidate()
        }
    /**圆角*/
    var corner = 0
        set(value) {
            field = value
            postInvalidate()
        }
    /**最大位数*/
    var maxLength = 0
        set(value) {
            field = value
            postInvalidate()
        }
    /**边界粗细*/
    var borderWidth = 0
        set(value) {
            field = value
            postInvalidate()
        }
    /**是否是密码类型*/
    var password = false
        set(value) {
            field = value
            postInvalidate()
        }
    /**显示光标*/
    var showCursor = false
        set(value) {
            field = value
            postInvalidate()
        }
    /**光标闪动间隔*/
    var cursorDuration = 0
        set(value) {
            field = value
            postInvalidate()
        }
    /**光标宽度*/
    var cursorWidth = 0
        set(value) {
            field = value
            postInvalidate()
        }
    /**光标颜色*/
    var cursorColor = 0
        set(value) {
            field = value
            postInvalidate()
        }
    /**实心方式、空心方式*/
    var type = 0
        set(value) {
            field = value
            postInvalidate()
        }
    /**边框颜色颜色*/
    var borderColor = 0
        set(value) {
            field = value
            postInvalidate()
        }
    /**实心方块颜色*/
    var blockColor = 0
        set(value) {
            field = value
            postInvalidate()
        }

    //边界画笔
    private lateinit var borderPaint: Paint
    //实心块画笔
    private lateinit var blockPaint: Paint
    //内容画笔
    private lateinit var textPaint: Paint
    //光标画笔
    private lateinit var cursorPaint: Paint
    //正方形边界
    private lateinit var borderRectF: RectF
    //小方块、小矩形
    private lateinit var boxRectF: RectF
    //方块宽度
    private var blockWidth = 0
    //方块高度
    private var blockHeight = 0
    //是否显示光标
    private var isCursorShowing = false
    //内容
    private lateinit var contentText: CharSequence
    //内容变动监听器
    var textChangedListener: TextChangedListener? = null

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        isLongClickable = false
        setTextIsSelectable(false)
        customSelectionActionModeCallback = object : android.view.ActionMode.Callback {
            /**
             * Called to report a user click on an action button.
             *
             * @param mode The current ActionMode
             * @param item The item that was clicked
             * @return true if this callback handled the event, false if the standard MenuItem
             * invocation should continue.
             */
            override fun onActionItemClicked(
                mode: android.view.ActionMode?,
                item: MenuItem?
            ): Boolean {
                return false
            }

            /**
             * Called when action mode is first created. The menu supplied will be used to
             * generate action buttons for the action mode.
             *
             * @param mode ActionMode being created
             * @param menu Menu used to populate action buttons
             * @return true if the action mode should be created, false if entering this
             * mode should be aborted.
             */
            override fun onCreateActionMode(mode: android.view.ActionMode?, menu: Menu?): Boolean {
                return false
            }

            /**
             * Called to refresh an action mode's action menu whenever it is invalidated.
             *
             * @param mode ActionMode being prepared
             * @param menu Menu used to populate action buttons
             * @return true if the menu or action mode was updated, false otherwise.
             */
            override fun onPrepareActionMode(mode: android.view.ActionMode?, menu: Menu?): Boolean {
                return false
            }

            /**
             * Called when an action mode is about to be exited and destroyed.
             *
             * @param mode The current ActionMode being destroyed
             */
            override fun onDestroyActionMode(mode: android.view.ActionMode?) {
            }

        }

        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val ta = context.obtainStyledAttributes(
            attrs, R.styleable.VerificationCodeEditText, defStyle, 0
        )
        password = ta.getBoolean(R.styleable.VerificationCodeEditText_password, false)
        showCursor = ta.getBoolean(R.styleable.VerificationCodeEditText_showCursor, true)
        borderColor = ta.getColor(
            R.styleable.VerificationCodeEditText_borderColor,
            Color.GRAY
        )
        blockColor = ta.getColor(
            R.styleable.VerificationCodeEditText_blockColor,
            Color.WHITE
        )
        blockWidth = ta.getDimension(
            R.styleable.VerificationCodeEditText_blockWidth,
            0f
        ).toInt()
        blockHeight = ta.getDimension(
            R.styleable.VerificationCodeEditText_blockHeight,
            0f
        ).toInt()
        cursorColor = ta.getColor(
            R.styleable.VerificationCodeEditText_cursorColor,
            Color.GRAY
        )
        corner = ta.getDimension(R.styleable.VerificationCodeEditText_corner, 0f).toInt()
        spacing = ta.getDimension(R.styleable.VerificationCodeEditText_blockSpacing, 0f).toInt()
        type = ta.getInt(
            R.styleable.VerificationCodeEditText_separateType, VerificationCodeEditText.TYPE_HOLLOW
        )
        maxLength = ta.getInt(R.styleable.VerificationCodeEditText_maxLength, 6)
        cursorDuration = ta.getInt(R.styleable.VerificationCodeEditText_cursorDuration, 500)
        cursorWidth = ta.getDimension(R.styleable.VerificationCodeEditText_cursorWidth, 2f).toInt()
        borderWidth = ta.getDimension(R.styleable.VerificationCodeEditText_borderWidth, 5f).toInt()

        ta.recycle()

        background = null;

        this.init()
    }

    private fun init() {
        this.isFocusableInTouchMode = true
        this.isFocusable = true
        this.requestFocus()
        this.isCursorVisible = false
        this.filters = arrayOf<InputFilter>(LengthFilter(maxLength))

        Handler().postDelayed({
            val imm =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
        }, 1000)

        blockPaint = Paint()
        blockPaint.isAntiAlias = true
        blockPaint.color = blockColor
        blockPaint.style = Paint.Style.FILL
        blockPaint.strokeWidth = 1.toFloat()

        textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.color = currentTextColor
        textPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.strokeWidth = 1.toFloat()
        textPaint.textSize = textSize
        textPaint.typeface = typeface

        borderPaint = Paint()
        borderPaint.isAntiAlias = true
        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth.toFloat()

        cursorPaint = Paint()
        cursorPaint.isAntiAlias = true
        cursorPaint.color = cursorColor
        cursorPaint.style = Paint.Style.FILL_AND_STROKE
        cursorPaint.strokeWidth = cursorWidth.toFloat()

        borderRectF = RectF()
        boxRectF = RectF()

        if (type == TYPE_HOLLOW) spacing = 0

        timerTask = object : TimerTask() {
            override fun run() {
                isCursorShowing = !isCursorShowing
                postInvalidate()
            }
        }
        timer = Timer()
    }


    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
        this.drawRect(canvas)
        this.drawText(canvas, contentText)
        this.drawCursor(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var w = widthMeasureSpec
        var h = heightMeasureSpec

        if (blockWidth != 0) {
            w = blockWidth * maxLength + spacing * (maxLength + 1)
        }

        if (blockHeight != 0) {
            h = blockHeight
        }

        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        var width = w
        var height = h
        if (blockWidth == 0) {
            blockWidth = (width - spacing * (maxLength + 1)) / maxLength
        }
        if (blockHeight == 0) {
            blockHeight = height
        }
        borderRectF[0f, 0f, width.toFloat()] = height.toFloat()
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        contentText = text ?: ""
        invalidate()
        if (maxLength == text?.length) {
            textChangedListener?.textCompleted(text)
        } else {
            textChangedListener?.textChanged(text)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //cursorFlashTime为光标闪动的间隔时间
        timer?.scheduleAtFixedRate(timerTask, 0, cursorDuration.toLong())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        timer?.cancel()
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        return true
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        val text: CharSequence? = text
        if (text != null) {
            if (selStart != text.length || selEnd != text.length) {
                setSelection(text.length, text.length)
                return
            }
        }
        super.onSelectionChanged(selStart, selEnd)
    }

    fun clearText() {
        setText("")
    }

    /**
     * 绘制光标
     *
     * @param canvas
     */
    private fun drawCursor(canvas: Canvas) {
        if (!isCursorShowing && showCursor && contentText.length < maxLength && hasFocus()) {
            val cursorPosition = contentText.length + 1
            val startX =
                spacing * cursorPosition + blockWidth * (cursorPosition - 1) + blockWidth / 2
            val startY = blockHeight / 4
            val endY = blockHeight - blockHeight / 4
            canvas.drawLine(
                startX.toFloat(),
                startY.toFloat(),
                startX.toFloat(),
                endY.toFloat(),
                cursorPaint
            )
        }
    }

    /**
     * 绘制内容　
     * @param canvas
     * @param charSequence
     */
    private fun drawText(
        canvas: Canvas,
        charSequence: CharSequence
    ) {
        for (i in charSequence.indices) {
            val startX = spacing * (i + 1) + blockWidth * i
            val startY = 0
            val baseX =
                (startX + blockWidth / 2 - textPaint.measureText(charSequence[i].toString()) / 2).toInt()
            val baseY =
                (startY + blockHeight / 2 - (textPaint.descent() + textPaint.ascent()) / 2).toInt()
            val centerX = startX + blockWidth / 2
            val centerY = startY + blockHeight / 2
            //Math.min=coerceAtMost
            val radius = blockWidth.coerceAtMost(blockHeight) / 6
            if (password) canvas.drawCircle(
                centerX.toFloat(),
                centerY.toFloat(),
                radius.toFloat(),
                textPaint
            ) else canvas.drawText(
                charSequence[i].toString(),
                baseX.toFloat(),
                baseY.toFloat(),
                textPaint
            )
        }
    }


    /**
     * 画矩形框
     * @param canvas
     */
    private fun drawRect(canvas: Canvas) {
        for (i in 0 until maxLength) {
            boxRectF[spacing * (i + 1) + blockWidth * i.toFloat(), 0f, spacing * (i + 1) + blockWidth * i + blockWidth.toFloat()] =
                blockHeight.toFloat()
            if (type == TYPE_SOLID) {
                canvas.drawRoundRect(boxRectF, corner.toFloat(), corner.toFloat(), blockPaint)
            } else if (type == TYPE_UNDERLINE) {
                canvas.drawLine(
                    boxRectF.left,
                    boxRectF.bottom,
                    boxRectF.right,
                    boxRectF.bottom,
                    borderPaint
                )
            } else if (type == TYPE_HOLLOW) {
                if (i == 0 || i == maxLength) continue
                canvas.drawLine(
                    boxRectF.left,
                    boxRectF.top,
                    boxRectF.left,
                    boxRectF.bottom,
                    borderPaint
                )
            }
        }
        if (type == TYPE_HOLLOW) canvas.drawRoundRect(
            borderRectF,
            corner.toFloat(),
            corner.toFloat(),
            borderPaint
        )
    }


    /**
     * 内容监听者
     */
    interface TextChangedListener {
        /**
         * 输入/删除监听
         *
         * @param changeText 输入/删除的字符
         */
        fun textChanged(changeText: CharSequence?)

        /**
         * 输入完成
         * @param text 输入的字符
         */
        fun textCompleted(text: CharSequence?)
    }
}
