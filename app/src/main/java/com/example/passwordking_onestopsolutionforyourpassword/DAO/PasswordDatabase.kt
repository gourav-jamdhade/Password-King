package com.example.passwordking_onestopsolutionforyourpassword.DAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Password::class], version = 1)
abstract class PasswordDatabase : RoomDatabase() {

    abstract fun passwordDao():PasswordDAO

    companion object{
        private var INSTANCE:PasswordDatabase?= null

        fun getInstance(context: Context):PasswordDatabase{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    PasswordDatabase::class.java,
                    "passwords.db"
                ).build()
            }

            return INSTANCE!!
        }


    }
}