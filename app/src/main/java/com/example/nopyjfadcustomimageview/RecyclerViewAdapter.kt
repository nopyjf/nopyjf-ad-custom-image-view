package com.example.nopyjfadcustomimageview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

class RecyclerViewAdapter(
    private var listener: AdListener,
    private var data: List<String> = arrayListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recycler_view,
            parent,
            false
        )
        return PagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PagerViewHolder -> holder.bind(data)
        }
    }

    override fun getItemCount() = 10

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        when (holder) {
            is RecyclerViewAdapter.PagerViewHolder -> holder.bind(data)
        }
    }

    inner class PagerViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {

        private val customViewPager =
            itemView.findViewById<ViewPager>(R.id.custom_view_pager)

        private val viewPagerAdapter = CustomViewPagerAdapter(
            data,
            listener
        )

        fun onAdImpress() {
            val view = customViewPager.getChildAt(customViewPager.currentItem)
            val imageView =
                view.findViewById<AdsCustomImageView>(R.id.ads_custom_image_view)
            imageView.onAdAppearOnScreen()
        }

        fun bind(data: List<String>) {
            viewPagerAdapter.setData(data)
            customViewPager.apply {
                adapter = viewPagerAdapter
                offscreenPageLimit = 2
                clipToPadding = false
                setPadding(
                    resources.getDimensionPixelSize(R.dimen.view_pager_padding),
                    0,
                    resources.getDimensionPixelSize(R.dimen.view_pager_padding),
                    0
                )
                pageMargin = resources.getDimensionPixelSize(R.dimen.view_pager_page_margin)

                addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        Log.d("MINT3", "${position}, ${positionOffset}, ${positionOffsetPixels}")

                        val view = getChildAt(position)
                        val imageView =
                            view.findViewById<AdsCustomImageView>(R.id.ads_custom_image_view)
                        imageView.onAdAppearOnScreen()
                    }

                    override fun onPageSelected(position: Int) {
                        Log.d("MINT4", "${position}")
                        val view = getChildAt(position)
                        val imageView =
                            view.findViewById<AdsCustomImageView>(R.id.ads_custom_image_view)
                        imageView.onAdAppearOnScreen()
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        // do nothing
                    }
                })
            }
        }
    }
}