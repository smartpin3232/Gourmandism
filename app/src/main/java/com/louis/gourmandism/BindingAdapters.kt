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

@SuppressLint("SimpleDateFormat", "SetTextI18n")
@BindingAdapter("textTime")
fun bindTextTime(textView: TextView, date: Long) {
    val timeDiff = System.currentTimeMillis() - date
    val day = (1000 * 60 * 60 * 24)
    val hour = (1000 * 60 * 60)
    val minute = (1000 * 60)
    when {
        timeDiff > day -> {
            textView.text = "${(timeDiff/day)}天前" }
        timeDiff in (hour + 1) until day -> {
            textView.text = "${(timeDiff/hour)}小時前"}
        else -> {
            textView.text = "${(timeDiff/minute)}分鐘前" }
    }
}
