package com.codescafe.doctorappointment.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codescafe.doctorappointment.models.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Database(entities = [UserModel::class, BooleanData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun booleanDataDao(): BooleanDataDao
}

class UserDataManager(private var context: Context) {
    private val db: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "user-database"
    ).build()

    private val userDao = db.userDao()
    private val booleanDataDao = db.booleanDataDao()


    suspend fun setBooleanData(key: String, value: Boolean) {
        withContext(Dispatchers.IO) {
            val booleanData = BooleanData(key, value)
            booleanDataDao.insertBooleanData(booleanData)
        }
    }

    suspend fun getBooleanData(key: String): Boolean {
        return withContext(Dispatchers.IO) {
            val booleanData = booleanDataDao.getBooleanData(key)
            booleanData?.value ?: false
        }
    }

    suspend fun setUserDetails(user: UserModel) {
        withContext(Dispatchers.IO) {
            userDao.insertUser(user)
        }
    }

    suspend fun getUserLoginDetails(): UserModel? {
        return withContext(Dispatchers.IO) {
            userDao.getUser()
        }
    }

    suspend fun logoutUser() {
        withContext(Dispatchers.IO) {
            userDao.deleteUser()
            booleanDataDao.deleteBooleanData("login")
        }
    }
}