package com.example.nopyjfadcustomimageview

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    private var listener: AdListener,
    private var data: List<String> = arrayListOf()
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recycler_view,
            parent,
            false
        )
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        when (holder) {
            is RecyclerViewAdapter.ViewHolder -> holder.onAdImpress()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val lm = recyclerView.layoutManager as LinearLayoutManager
        val firstPosition = lm.findFirstVisibleItemPosition()
        val lastPosition = lm.findLastVisibleItemPosition()
        val globalVisibleRect = Rect()
        recyclerView.getGlobalVisibleRect(globalVisibleRect)
        for (pos in firstPosition..lastPosition) {
            recyclerView.findViewHolderForAdapterPosition(pos).let { holder ->
                when (holder) {
                    is RecyclerViewAdapter.ViewHolder -> holder.onAdImpress()
                }
            }
        }
    }

    inner class ViewHolder(
        view: View,
        private val listener: AdListener
    ) : RecyclerView.ViewHolder(view) {

        private val adsCustomImageView =
            itemView.findViewById<AdsCustomImageView>(R.id.adsCustomImageView)

        fun bind(url: String) {
            adsCustomImageView.apply {
                setAdListener(listener)
                setAdImageUrl(url)
                loadingAdImageUrl()
            }
        }

        fun onAdImpress() {
            adsCustomImageView.onAdAppearOnScreen()
        }
    }
}