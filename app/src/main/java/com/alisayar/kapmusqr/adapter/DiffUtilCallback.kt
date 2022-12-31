package com.alisayar.kapmusqr.adapter

import androidx.recyclerview.widget.DiffUtil
import com.alisayar.kapmusqr.model.LessonModel
import com.alisayar.kapmusqr.model.StudentModel

object DiffUtilCallback: DiffUtil.ItemCallback<LessonModel>() {
    override fun areItemsTheSame(oldItem: LessonModel, newItem: LessonModel): Boolean {
        return oldItem.dersKodu == newItem.dersKodu
    }
    override fun areContentsTheSame(oldItem: LessonModel, newItem: LessonModel): Boolean {
        return oldItem == newItem
    }

}

