package com.example.nopyjfadcustomimageview

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager


class CustomViewPager(context: Context, attr: AttributeSet) : ViewPager(context, attr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desireWidth = 320.toDouble()
        val desireHeight = 320.toDouble()
        val scale = desireWidth / desireHeight

        val height = MeasureSpec.getSize(widthMeasureSpec) / scale
        val heightWithMeasureSpec = MeasureSpec.makeMeasureSpec(height.toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightWithMeasureSpec)
    }
}