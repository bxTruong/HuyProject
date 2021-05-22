package com.bxtruong.huyproject

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bxtruong.huyproject.databinding.ItemExaminationRoomBinding
import com.bxtruong.huyproject.model.ExaminationRoom

class AD(
        var examinationRooms: ArrayList<ExaminationRoom>,
        var clickExamination: ClickExamination,
        var teacher:Boolean
) : RecyclerView.Adapter<AD.ExaminationRoomVH>() {

    inner class ExaminationRoomVH(private val binding: ItemExaminationRoomBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bin(item: ExaminationRoom) {
            binding.examinationRoom = item
            binding.click = clickExamination
            binding.isTeacher=teacher
            Log.e("isTeacher","$teacher")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExaminationRoomVH {
        return ExaminationRoomVH(
                ItemExaminationRoomBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )
    }

    override fun getItemCount() = examinationRooms.size

    override fun onBindViewHolder(holder: ExaminationRoomVH, position: Int) {
        holder.bin(examinationRooms[position])
    }
}