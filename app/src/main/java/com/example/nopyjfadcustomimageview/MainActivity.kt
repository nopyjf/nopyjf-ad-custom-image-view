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
        listOf(
            "https://picsum.photos/id/1/360/180",
            "https://picsum.photos/id/2/360/180",
            "https://picsum.photos/id/3/360/180",
            "https://picsum.photos/id/4/360/180",
            "https://picsum.photos/id/5/360/180",
            "https://picsum.photos/id/6/360/180",
            "https://picsum.photos/id/7/360/180",
            "https://picsum.photos/id/8/360/180"
        ),
        listOf(
            "https://picsum.photos/id/11/360/180",
            "https://picsum.photos/id/12/360/180",
            "https://picsum.photos/id/13/360/180",
            "https://picsum.photos/id/14/360/180",
            "https://picsum.photos/id/15/360/180",
            "https://picsum.photos/id/16/360/180",
            "https://picsum.photos/id/17/360/180",
            "https://picsum.photos/id/18/360/180"
        ),
        listOf(
            "https://picsum.photos/id/21/360/180",
            "https://picsum.photos/id/22/360/180",
            "https://picsum.photos/id/23/360/180",
            "https://picsum.photos/id/24/360/180",
            "https://picsum.photos/id/25/360/180",
            "https://picsum.photos/id/26/360/180",
            "https://picsum.photos/id/27/360/180",
            "https://picsum.photos/id/28/360/180"
        ),
        listOf(
            "https://picsum.photos/id/31/360/180",
            "https://picsum.photos/id/32/360/180",
            "https://picsum.photos/id/33/360/180",
            "https://picsum.photos/id/34/360/180",
            "https://picsum.photos/id/35/360/180",
            "https://picsum.photos/id/36/360/180",
            "https://picsum.photos/id/37/360/180",
            "https://picsum.photos/id/38/360/180"
        ),
        listOf(
            "https://picsum.photos/id/41/360/180",
            "https://picsum.photos/id/42/360/180",
            "https://picsum.photos/id/43/360/180",
            "https://picsum.photos/id/44/360/180",
            "https://picsum.photos/id/45/360/180",
            "https://picsum.photos/id/46/360/180",
            "https://picsum.photos/id/47/360/180",
            "https://picsum.photos/id/48/360/180"
        ),
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