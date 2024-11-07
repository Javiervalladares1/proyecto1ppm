// In the same file or a new file CourseRepository.kt

package com.example.proyecto1ppm.data

import com.example.proyecto1ppm.R

object CourseRepository {
    val courses = listOf(
        Course(
            id = "course_calculus",
            title = "Cálculo",
            description = "Este curso cubre los conceptos fundamentales del cálculo diferencial e integral.",
            imageRes = R.drawable.house // Replace with your actual image resource
        ),
        Course(
            id = "course_physics",
            title = "Física",
            description = "Explora las leyes fundamentales que gobiernan el universo.",
            imageRes = R.drawable.house
        ),
        Course(
            id = "course_chemistry",
            title = "Química",
            description = "Aprende sobre la composición, estructura y propiedades de la materia.",
            imageRes = R.drawable.house
        )
        // Add more courses as needed
    )
}
