package com.udacity.asteroidradar.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

@BindingAdapter("statusImage")
fun asteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, strUrl: String?) {
    Picasso.with(imageView.context)
        .load(strUrl)
        .placeholder(R.drawable.placeholder_picture_of_day)
        .error(R.drawable.placeholder_picture_of_day)
        .into(imageView)
}