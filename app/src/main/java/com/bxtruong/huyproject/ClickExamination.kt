package com.bxtruong.huyproject

import com.bxtruong.huyproject.model.ExaminationRoom

interface ClickExamination {
    fun onClick(item: ExaminationRoom)
    fun update(item: ExaminationRoom)
    fun delete(item: ExaminationRoom)
}