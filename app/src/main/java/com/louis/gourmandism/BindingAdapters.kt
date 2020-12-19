package com.louis.gourmandism

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {

    val imgUri = imgUrl?.toUri()?.buildUpon()?.build()
    Glide.with(imgView.context)
        .load(imgUri)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.home)
                .error(R.drawable.no_picture)
        )
        .into(imgView)
}

@BindingAdapter("imageLocal")
fun bindLocalImage(imgView: ImageView, imgUrl: String?) {

    Glide.with(imgView.context)
        .load(imgUrl)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.home)
                .error(R.drawable.no_picture)
        )
        .into(imgView)
}

@BindingAdapter("userImage")
fun bindUserImage(imgView: ImageView, imgUrl: String?) {

    Glide.with(imgView.context)
        .load(imgUrl)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
        )
        .into(imgView)
}

@BindingAdapter("textVisible")
fun bindTextVisible(textView: TextView, data: String?) {
    if(data.isNullOrBlank()){
        textView.visibility = View.GONE
    }else{
        textView.visibility = View.VISIBLE
    }
}
