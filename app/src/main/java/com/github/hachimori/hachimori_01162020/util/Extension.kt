package com.github.hachimori.hachimori_01162020.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.github.hachimori.hachimori_01162020.R

/**
 * Load image with Glide.
 */
@BindingAdapter("bind:imageUrl")
fun ImageView.loadImage(url: String) {
    GlideApp
        .with(context)
        .load(url)
        .transition(withCrossFade())
        .placeholder(R.drawable.ic_empty)
        .into(this)
}
