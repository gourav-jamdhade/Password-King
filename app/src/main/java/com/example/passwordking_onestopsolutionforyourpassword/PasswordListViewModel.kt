package com.example.passwordking_onestopsolutionforyourpassword

import com.example.passwordking_onestopsolutionforyourpassword.DAO.Password
import com.example.passwordking_onestopsolutionforyourpassword.DAO.PasswordDAO

class PasswordListViewModel(private val passwordDao:PasswordDAO) {

    suspend fun getPasswordList():List<Password>{
        return passwordDao.getAllPasswords()
    }
}

