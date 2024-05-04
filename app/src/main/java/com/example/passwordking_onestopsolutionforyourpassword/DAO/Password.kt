package com.example.passwordking_onestopsolutionforyourpassword.DAO

import androidx.room.*

@Entity(tableName="passwords")
data class Password(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title:String,
    val password:String
)
