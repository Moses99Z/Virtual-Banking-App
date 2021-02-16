package com.example.banking.data.database

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.banking.data.model.History
import com.example.banking.data.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [User::class, History::class], version = 2, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user_database"
                    )
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                GlobalScope.launch {
                                    getInstance(context).userDao().insertUsers(ListOfUsers.getUsers())
                                }

                            }
                        })
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }
}