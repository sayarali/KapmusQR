package com.alisayar.kapmusqr.adapter

import androidx.recyclerview.widget.DiffUtil
import com.alisayar.kapmusqr.model.LessonModel
import com.alisayar.kapmusqr.model.StudentDevamsizlikModel
import com.alisayar.kapmusqr.model.StudentModel

object DiffUtilCallback: DiffUtil.ItemCallback<LessonModel>() {
    override fun areItemsTheSame(oldItem: LessonModel, newItem: LessonModel): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: LessonModel, newItem: LessonModel): Boolean {
        return oldItem.dersKodu == newItem.dersKodu
    }

}
object DiffUtilCallbackA: DiffUtil.ItemCallback<StudentDevamsizlikModel>() {
    override fun areItemsTheSame(oldItem: StudentDevamsizlikModel, newItem: StudentDevamsizlikModel): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: StudentDevamsizlikModel, newItem: StudentDevamsizlikModel): Boolean {
        return oldItem.id == newItem.id
    }

}

