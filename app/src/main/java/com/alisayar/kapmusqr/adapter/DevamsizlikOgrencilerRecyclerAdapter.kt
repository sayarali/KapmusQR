package com.alisayar.kapmusqr.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alisayar.kapmusqr.databinding.DevamsizlikOgrencilerRecyclerRowBinding
import com.alisayar.kapmusqr.databinding.LessonRecyclerRowBinding
import com.alisayar.kapmusqr.model.LessonModel
import com.alisayar.kapmusqr.model.StudentDevamsizlikModel

class DevamsizlikOgrencilerRecyclerAdapter(): ListAdapter<StudentDevamsizlikModel, DevamsizlikOgrencilerRecyclerAdapter.ViewHolder>(DiffUtilCallbackA) {
    class ViewHolder(val binding: DevamsizlikOgrencilerRecyclerRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(studentDevamsizlikModel: StudentDevamsizlikModel?) {
            binding.model = studentDevamsizlikModel
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DevamsizlikOgrencilerRecyclerRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

