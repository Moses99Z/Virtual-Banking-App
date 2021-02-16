package com.example.banking.data.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users_table", indices = [Index(value = ["username"], unique = true)])
data class User(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userId")
    var userId: Int,

    @ColumnInfo(name = "username")
    var username: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "balance")
    var balance: Double = 2000.0,

    @ColumnInfo(name = "created_time")
    var createdTime: Long = System.currentTimeMillis()

) : Parcelable

