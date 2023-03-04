package com.example.vktest.views

import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable
import androidx.appcompat.widget.AppCompatImageButton
import com.example.vktest.R

class ToggleImageButton : AppCompatImageButton, Checkable {

    @FunctionalInterface
    interface OnCheckedChangeListener {
        fun onCheckedChanged(button: ToggleImageButton?, isChecked: Boolean)
    }

    private var mChecked = false
    var onCheckedChangeListener: OnCheckedChangeListener? = null

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?)
            : super(context!!, attrs)
    {
        setCheckedFrom(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int)
            : super(context!!, attrs, defStyle)
    {
        setCheckedFrom(attrs)
    }

    private fun setCheckedFrom(attrs: AttributeSet?) {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ToggleImageButton)
        isChecked = styledAttrs.getBoolean(R.styleable.ToggleImageButton_android_checked, false)
        styledAttrs.recycle()
    }


    override fun isChecked(): Boolean {
        return mChecked
    }

    override fun setChecked(checked: Boolean) {
        mChecked = checked
        onCheckedChangeListener?.onCheckedChanged(this, mChecked)
        invalidate()
        refreshDrawableState()
    }

    override fun toggle() {
        isChecked = !isChecked
    }

    override fun performClick(): Boolean {
        toggle()
        return super.performClick()
    }

    private val drawableStateChecked = intArrayOf(android.R.attr.state_checked)

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        return if (isChecked) {
            mergeDrawableStates(
                super.onCreateDrawableState(extraSpace + drawableStateChecked.size),
                drawableStateChecked
            )
        } else {
            super.onCreateDrawableState(extraSpace)
        }
    }
}