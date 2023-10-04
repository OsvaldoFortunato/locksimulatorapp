package com.example.lockappemulation.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PasswordDao {

    @Insert
    suspend fun insert(password: Password)

    @Query("SELECT * FROM tb_password")
    suspend fun getAll(): List<Password>

    @Query("SELECT * FROM tb_password WHERE tb_password.id = :id_password")
    suspend fun getOne(id_password: Int):List<Password>

    @Query("SELECT COUNT(*) FROM tb_password")
    suspend fun countTotal():Long
}