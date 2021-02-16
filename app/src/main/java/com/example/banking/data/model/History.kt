package com.example.banking.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "sender")
    var sender: String,
    @ColumnInfo(name = "receiver")
    var receiver: String,
    @ColumnInfo(name = "amount")
    var amount: Double,
    @ColumnInfo(name = "created_date")
    val createdTime : Long = System.currentTimeMillis()
) : Parcelable
