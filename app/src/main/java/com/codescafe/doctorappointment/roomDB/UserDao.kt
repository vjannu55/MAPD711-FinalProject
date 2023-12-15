package com.codescafe.doctorappointment.roomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codescafe.doctorappointment.models.UserModel

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserModel)

    @Query("SELECT * FROM user_table LIMIT 1")
    fun getUser(): UserModel?

    @Query("DELETE FROM user_table")
    fun deleteUser()
}