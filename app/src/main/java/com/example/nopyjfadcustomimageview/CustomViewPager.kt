package com.example.nopyjfadcustomimageview

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.viewpager.widget.ViewPager


class CustomViewPager(context: Context, attr: AttributeSet) : ViewPager(context, attr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desireWidth = 160.toDouble()
        val desireHeight = 100.toDouble()
        val desireDotSize = 5.toDouble()

        val scale = desireWidth / (desireHeight - desireDotSize)

        val itemWidth = widthMeasureSpec - marginStart - marginEnd - paddingStart - paddingEnd
        val width = getChildWidthMeasureSpec(itemWidth)
        val height = getHeightFromScale(width, scale)

        setChildHeightMeasureSpec(width)
        setMeasuredDimension(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }

    private fun getChildWidthMeasureSpec(widthMeasureSpec: Int): Int {
        return MeasureSpec.makeMeasureSpec(
            0.coerceAtLeast(MeasureSpec.getSize(widthMeasureSpec)),
            MeasureSpec.getMode(widthMeasureSpec)
        )
    }

    private fun setChildHeightMeasureSpec(childWidthMeasureSpec: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(childWidthMeasureSpec, MeasureSpec.UNSPECIFIED)
        }
    }

    private fun getHeightFromScale(width: Int, scale: Double): Int {
        return (width.toDouble() / scale).toInt()
    }
}