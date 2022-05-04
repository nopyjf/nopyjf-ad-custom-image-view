package com.example.nopyjfadcustomimageview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

class RecyclerViewAdapter(
    private var listener: AdListener,
    private var data: List<List<String>> = arrayListOf()
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
            is PagerViewHolder -> holder.bind(data[position])
        }
    }

    override fun getItemCount() = data.size

    inner class PagerViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {

        private val customViewPager = itemView.findViewById<CustomViewPager>(R.id.custom_view_pager)

        private val viewPagerAdapter = CustomViewPagerAdapter(listener)

        private var swiperState: Int = 0
        private var currentSelectedItem: Int = 0
        private var nextSelectedItem: Int = 0

        fun onAdImpress() {
            viewPagerAdapter.getAdImageView(nextSelectedItem)?.onAdAppearOnScreen()
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

                    override fun onPageScrollStateChanged(state: Int) {
                        swiperState = state
                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        if (swiperState != ViewPager.SCROLL_STATE_SETTLING) {
                            if (position == currentSelectedItem) {
                                if (positionOffset >= 0.5) {
                                    if (position + 1 != nextSelectedItem) {
                                        nextSelectedItem = position + 1
                                        viewPagerAdapter
                                            .getAdImageView(nextSelectedItem)
                                            ?.onAdAppearOnScreen()
                                    }
                                } else {
                                    if (position != nextSelectedItem) {
                                        nextSelectedItem = position
                                        viewPagerAdapter
                                            .getAdImageView(nextSelectedItem)
                                            ?.onAdDisappearOnScreen()
                                    }
                                }
                            } else {
                                if (positionOffset >= 0.5) {
                                    if (position + 1 != nextSelectedItem) {
                                        nextSelectedItem = position + 1
                                        viewPagerAdapter
                                            .getAdImageView(nextSelectedItem)
                                            ?.onAdDisappearOnScreen()
                                    }
                                } else {
                                    if (position != nextSelectedItem) {
                                        nextSelectedItem = position
                                        viewPagerAdapter
                                            .getAdImageView(nextSelectedItem)
                                            ?.onAdAppearOnScreen()
                                    }
                                }
                            }
                        }
                    }

                    override fun onPageSelected(position: Int) {
                        currentSelectedItem = position
                        nextSelectedItem = position
                        viewPagerAdapter.getAdImageView(position)?.onAdDisappearOnScreen()
                        viewPagerAdapter.getAdImageView(position)?.onAdAppearOnScreen()
                    }
                })
            }
        }
    }
}