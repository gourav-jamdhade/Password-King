package com.example.passwordking_onestopsolutionforyourpassword.DAO

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PasswordDAO {



    @Query("Select * FROM passwords")
    suspend fun getAllPasswords(): List<Password>

    @Query("SELECT * FROM passwords where id= :id")
    suspend fun getPasswordById(id: Long): Password?

    @Query("SELECT * FROM passwords WHERE password = :password")
    suspend fun getPasswordByPassword(password: String): Password?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(password: Password)


    @Delete
    suspend fun deletePassword(password: Password)

    @Update
    suspend fun updatePassword(password: Password)
}