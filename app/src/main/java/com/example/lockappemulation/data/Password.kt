package com.example.lockappemulation.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_password")
data class Password(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    @ColumnInfo(name = "password_user")
    var password: String
)
