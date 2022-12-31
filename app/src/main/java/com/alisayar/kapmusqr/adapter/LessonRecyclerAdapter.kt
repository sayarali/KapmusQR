package com.alisayar.kapmusqr.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alisayar.kapmusqr.databinding.LessonRecyclerRowBinding
import com.alisayar.kapmusqr.model.LessonModel

class LessonRecyclerAdapter(private val onClickListener: OnClickListener): ListAdapter<LessonModel, LessonRecyclerAdapter.ViewHolder>(DiffUtilCallback) {
    class ViewHolder(val binding: LessonRecyclerRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(lessonModel: LessonModel?) {
            binding.lessonModel = lessonModel
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LessonRecyclerRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
        holder.bind(item)
    }
}

class OnClickListener(val clickListener: (lessonModel: LessonModel) -> Unit){
    fun onClick(lessonModel: LessonModel) = clickListener(lessonModel)
}