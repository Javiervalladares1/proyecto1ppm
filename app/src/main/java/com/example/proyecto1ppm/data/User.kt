package com.example.proyecto1ppm.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id :Int = 0,
    @ColumnInfo(name = "user") val user: String,
    @ColumnInfo(name = "firstName") val firstName: String,
    @ColumnInfo(name = "lastName") val lastName: String,
    @ColumnInfo(name = "email") val email:String
    )

