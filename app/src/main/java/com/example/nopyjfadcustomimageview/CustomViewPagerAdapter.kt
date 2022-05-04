package com.example.nopyjfadcustomimageview

import android.database.DataSetObserver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide

class CustomViewPagerAdapter(
    private val listener: AdListener,
    private var items: List<String> = listOf()
) : PagerAdapter() {

    private var ivList: HashMap<Int, AdsCustomImageView> = hashMapOf()

    fun setData(data: List<String>) {
        items = data
        notifyDataSetChanged()
    }

    fun getAdImageView(position: Int): AdsCustomImageView? {
        if (position >= ivList.size) return null
        return ivList[position]
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
            ivList[position] = this
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