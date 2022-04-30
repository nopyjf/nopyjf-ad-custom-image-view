package com.example.nopyjfadcustomimageview

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlin.math.max
import kotlin.math.min

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
            onAdsImpressFollowHeight(
                {
                    listener?.onAdImpression()
                    adImpressed = true
                },
                {
                    adImpressed = false
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
                    listener?.onAdLoadFailed()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    listener?.onAdLoaded()
                    return false
                }

            })
            .into(this)
    }

    fun onAdAppearOnScreen() {
        onAdsImpressFollowHeight(
            {
                if (!adImpressed) {
                    stillCountDownAdImpressed = true
                    impressionCountDown.start()
                }
            },
            {
                onAdDisappearOnScreen()
            }
        )
    }

    private fun onAdDisappearOnScreen() {
        impressionCountDown.cancel()
    }

    private fun onAdsImpressFollowHeight(
        onAdAppear: () -> Unit,
        onAdDisappear: () -> Unit
    ) {
//        val itemRect = Rect()
//        val itemView = this.getLocalVisibleRect(itemRect)
//
//        val yTopPosItem = itemRect.top
//        val yBottomPosItem = itemRect.bottom
//        val heightItem = itemRect.height()
//
//        val yTopPosScreen = 0
//        val yBottomPosScreen = context.resources.displayMetrics.heightPixels
//
//        val maxTopPos = max(0, max(yTopPosScreen, yTopPosItem))
//        val minBottomPos = max(0, min(yBottomPosScreen, yBottomPosItem))
//
//        val itemHeightPercentage = (minBottomPos - maxTopPos) / heightItem * 100
//
//        if (itemView && itemHeightPercentage >= 50) {
//            onAdAppear()
//        } else {
//            onAdDisappear()
//        }

        val itemRect = Rect()
        val isParentViewEmpty = this.getLocalVisibleRect(itemRect)
        val visibleWidth = itemRect.width().toDouble()
        val visibleHeight = itemRect.height().toDouble()
        val width = this.measuredWidth
        val height = this.measuredHeight
        val viewVisibleWidthPercentage = visibleWidth / width * 100
        val viewVisibleHeightPercentage = visibleHeight / height * 100

        if (isParentViewEmpty && viewVisibleWidthPercentage >= 50 && viewVisibleHeightPercentage >= 50) {
            onAdAppear()
        } else {
            onAdDisappear()
        }
    }

    override fun hasOnClickListeners(): Boolean {
        listener?.onAdClick()
        return super.hasOnClickListeners()
    }
}

interface AdListener {
    fun onAdLoaded()
    fun onAdLoadFailed()
    fun onAdClick()
    fun onAdImpression()
}