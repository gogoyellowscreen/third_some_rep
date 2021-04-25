package com.example.textnotesapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
data class Note (
    @PrimaryKey val id: Int,
    val title: String,
    val category: String,
    val body: String)
