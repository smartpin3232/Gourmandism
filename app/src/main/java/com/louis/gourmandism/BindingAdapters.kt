package com.louis.gourmandism

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.model.LatLng
import com.louis.gourmandism.data.Location
import com.louis.gourmandism.data.OpenTime

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {

    val imgUri = imgUrl?.toUri()?.buildUpon()?.build()
    Glide.with(imgView.context)
        .load(imgUri)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.tableware)
                .error(R.drawable.tableware)
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
                .error(R.drawable.home)
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


@SuppressLint("SetTextI18n")
@BindingAdapter("textBusinessTime")
fun bindBusinessTime(textView: TextView, time: OpenTime) {
    val day = when(time.day){
        "1" -> "星期一"
        "2" -> "星期二"
        "3" -> "星期三"
        "4" -> "星期四"
        "5" -> "星期五"
        "6" -> "星期六"
        "7" -> "星期日"
        else -> "星期日"
    }
    textView.text = day+ "  " + time.startTime + " ‒ " + time.endTime
}
