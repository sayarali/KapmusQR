package com.alisayar.kapmusqr.util

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.adapter.DevamsizlikOgrencilerRecyclerAdapter
import com.alisayar.kapmusqr.adapter.LessonRecyclerAdapter
import com.alisayar.kapmusqr.model.LessonModel
import com.alisayar.kapmusqr.model.StudentDevamsizlikModel
import com.alisayar.kapmusqr.model.StudentModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("lessonsList")
fun lessonBindAdapter(recyclerView: RecyclerView, lessonList: List<LessonModel>?){
    val adapter = recyclerView.adapter as LessonRecyclerAdapter
    adapter.submitList(lessonList)
}

@BindingAdapter("devamsizlikOgrenciList")
fun ogrenciBindAdapter(recyclerView: RecyclerView, ogrenciList: List<StudentDevamsizlikModel>?){
    val adapter = recyclerView.adapter as DevamsizlikOgrencilerRecyclerAdapter
    adapter.submitList(ogrenciList)
}

@BindingAdapter("circleImageUrl")
fun bindCircleImageUrl(imageView: CircleImageView, imgUrl: String?){
    if(imgUrl != null){
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imgUri)
            .into(imageView)
    }
}

@BindingAdapter("bitmap")
fun bindImageBitmap(imageView: ImageView, bitmap: Bitmap?){
    if(bitmap != null){
        imageView.setImageBitmap(bitmap)
    }
}

@BindingAdapter("imageUrl")
fun bindAnyImageUrl(imageView: ImageView, imgUrl: String?){
    if(imgUrl != null){
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imgUri)
            .placeholder(R.drawable.loading_animation)
            .into(imageView)

    }
}

@BindingAdapter("devamsizCheck")
fun bindCheckImageView(imageView: ImageView, check: Boolean?){
    if(check != null){
        if(check){
            imageView.setImageResource(R.drawable.ic_check_green)
        } else {
            imageView.setImageResource(R.drawable.ic_xmark)
        }

    }
}

