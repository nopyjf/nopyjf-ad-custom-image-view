package com.example.nopyjfadcustomimageview

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class AdsCustomImageView(context: Context, attr: AttributeSet) : AppCompatImageView(context, attr) {

    private var listener: AdListener? = null

    private var adImageUrl: String = ""

    private var adImpressed: Boolean = false

    private var stillCountDownAdImpressed = false

    private val impressionCountDown = object : CountDownTimer(1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            // do nothing
        }

        override fun onFinish() {
            stillCountDownAdImpressed = false
            onAdsImpress(
                {
                    if (!adImpressed) {
                        listener?.onAdImpression(adImageUrl)
                        adImpressed = true
                    }
                },
                {
                    // do nothing
                }
            )
        }
    }

    fun setAdListener(listener: AdListener) {
        this.listener = listener
    }

    fun setAdImageUrl(adImageUrl: String) {
        this.adImageUrl = adImageUrl
    }

    fun loadingAdImageUrl() {
        Glide.with(context)
            .load(adImageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    listener?.onAdLoadFailed(adImageUrl)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    listener?.onAdLoaded(adImageUrl)
                    return false
                }

            })
            .into(this)
    }

    fun onAdAppearOnScreen() {
        onAdsImpress(
            {
                if (!adImpressed && !stillCountDownAdImpressed) {
                    impressionCountDown.start()
                    stillCountDownAdImpressed = true
                }
            },
            {
                onAdDisappearOnScreen()
            }
        )
    }

    private fun onAdDisappearOnScreen() {
        stillCountDownAdImpressed = false
        impressionCountDown.cancel()
    }

    private fun onAdsImpress(
        onAdAppear: () -> Unit,
        onAdDisappear: () -> Unit
    ) {
        val itemRect = Rect()
        val isParentViewVisible = this.getLocalVisibleRect(itemRect)
        if (!isParentViewVisible) {
            onAdDisappear()
            return
        }

        val visibleWidth = itemRect.width().toDouble()
        val visibleHeight = itemRect.height().toDouble()

        val width = this.measuredWidth
        val height = this.measuredHeight

        if (width == 0 || height == 0) {
            onAdDisappear()
            return
        }

        val viewVisibleWidthPercentage = visibleWidth / width * 100
        val viewVisibleHeightPercentage = visibleHeight / height * 100

        Log.d(
            "MINT2",
            "${width}, ${height}, ${viewVisibleWidthPercentage}, ${viewVisibleHeightPercentage}"
        )

        if (viewVisibleWidthPercentage >= 50 && viewVisibleHeightPercentage >= 50) {
            onAdAppear()
        } else {
            onAdDisappear()
        }
    }

    override fun hasOnClickListeners(): Boolean {
        listener?.onAdClick(adImageUrl)
        return super.hasOnClickListeners()
    }
}

interface AdListener {
    fun onAdLoaded(adImageUrl: String)
    fun onAdLoadFailed(adImageUrl: String)
    fun onAdClick(adImageUrl: String)
    fun onAdImpression(adImageUrl: String)
}