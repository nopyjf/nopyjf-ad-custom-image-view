package com.example.nopyjfadcustomimageview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity(), AdListener {

    private lateinit var adsCustomImageView: AdsCustomImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adsCustomImageView = findViewById<AdsCustomImageView>(R.id.adsCustomImageView)
        adsCustomImageView.apply {
            setAdListener(this@MainActivity)
            setAdImageUrl("https://picsum.photos/id/1/360/180")
            loadingAdImageUrl()
        }
    }

    override fun onAdClick() {
        Toast.makeText(this, "ADS CLICKED", Toast.LENGTH_LONG).show()
    }

    override fun onAdImpression() {
        Toast.makeText(this, "ADS IMPRESSED", Toast.LENGTH_LONG).show()
    }

    override fun onAdLoadFailed() {
        // do nothing
    }

    override fun onAdLoaded() {
        adsCustomImageView.onAdAppearOnScreen()
    }
}