package com.example.passwordking_onestopsolutionforyourpassword.EncrytionUtils

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

import kotlin.jvm.Throws

object AES {


    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(Exception::class)
    fun encrypt(plainText:String, key:SecretKey):String{
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE,key)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(Exception::class)
    fun decrypt(cipherText : String, key : SecretKey):String{
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE,key)
        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText))
        return String(decryptedBytes)
    }


    @Throws(Exception::class)
    fun generateAESKey():SecretKey{
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        return keyGenerator.generateKey()
    }

}