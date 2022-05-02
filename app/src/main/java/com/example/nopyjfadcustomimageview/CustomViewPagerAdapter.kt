package com.example.nopyjfadcustomimageview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class CustomViewPagerAdapter(
    private var items: List<String> = listOf(),
    private val listener: AdListener
) : PagerAdapter() {

    fun setData(data: List<String>) {
        items = data
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater
            .from(container.context)
            .inflate(R.layout.item_custom_view_pager, container, false)
        val adsCustomImageView = view.findViewById<AdsCustomImageView>(R.id.ads_custom_image_view)
        adsCustomImageView.apply {
            setAdListener(listener)
            setAdImageUrl(items[position])
            loadingAdImageUrl()
        }
        container.addView(view)
        return view
    }

    override fun getCount() = items.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}