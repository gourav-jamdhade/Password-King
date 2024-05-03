package com.example.passwordking_onestopsolutionforyourpassword.EncrytionUtils

import androidx.lifecycle.ViewModel
import javax.crypto.SecretKey

class EncryptionViewModel :ViewModel(){

    var secretKey: SecretKey? = null
}