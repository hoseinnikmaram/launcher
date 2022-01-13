package com.nikmaram.launcher.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.nikmaram.launcher.R

@BindingAdapter("imageUrl")
fun imageView(view: ImageView, url: Any?) {
    Glide.with(view)
        .load(url)
        .centerCrop()
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.placeholder)
        .into(view)
}

@BindingAdapter("isVisible")
fun View.isVisible(isShow: Boolean) {
    if (isShow) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}


