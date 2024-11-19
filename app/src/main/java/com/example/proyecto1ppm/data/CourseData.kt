// In com.example.proyecto1ppm.data package, create CourseData.kt

package com.example.proyecto1ppm.data

import androidx.annotation.DrawableRes


data class Course(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = ""
)
