package com.example.nopyjfadcustomimageview

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), AdListener {

    private val urlImpressedList = hashSetOf<String>()

    private val urls = listOf(
        "https://picsum.photos/id/1/360/180",
        "https://picsum.photos/id/2/360/180",
        "https://picsum.photos/id/3/360/180",
        "https://picsum.photos/id/4/360/180",
        "https://picsum.photos/id/5/360/180",
        "https://picsum.photos/id/6/360/180",
        "https://picsum.photos/id/7/360/180",
        "https://picsum.photos/id/8/360/180"
    )

    private val recyclerViewAdapter = RecyclerViewAdapter(this, urls)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rvAds = findViewById<RecyclerView>(R.id.rvAds)
        rvAds.apply {
            layoutManager = object : LinearLayoutManager(this@MainActivity) {
                override fun onLayoutCompleted(state: RecyclerView.State?) {
                    super.onLayoutCompleted(state)
                    getItemOnHolder(this@apply) { holder ->
                        holder.onAdImpress()
                    }
                }
            }


            adapter = recyclerViewAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    getItemOnHolder(this@apply) { holder ->
                        holder.onAdImpress()
                    }
                }
            })
        }
    }

    override fun onAdLoaded(adImageUrl: String) {
        // do nothing
    }

    override fun onAdLoadFailed(adImageUrl: String) {
        // do nothing
    }

    override fun onAdClick(adImageUrl: String) {
        // do nothing
    }

    override fun onAdImpression(adImageUrl: String) {
        if (!urlImpressedList.contains(adImageUrl)) {
            // call APIs
            Log.d("MINT1", adImageUrl)
            urlImpressedList.add(adImageUrl) // do on call impress api success
        }
    }

    private fun getItemOnHolder(
        recyclerView: RecyclerView,
        doSomething: (holder: RecyclerViewAdapter.PagerViewHolder) -> Unit
    ) {
        val lm = recyclerView.layoutManager as LinearLayoutManager
        val firstPosition = lm.findFirstVisibleItemPosition()
        val lastPosition = lm.findLastVisibleItemPosition()
        val globalVisibleRect = Rect()
        recyclerView.getGlobalVisibleRect(globalVisibleRect)
        for (pos in firstPosition..lastPosition) {
            recyclerView.findViewHolderForAdapterPosition(pos).let { holder ->
                when (holder) {
                    is RecyclerViewAdapter.PagerViewHolder -> doSomething(holder)
                }
            }
        }
    }
}