package com.example.passwordking_onestopsolutionforyourpassword.Fragments

import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.passwordking_onestopsolutionforyourpassword.EncrytionUtils.AES
import com.example.passwordking_onestopsolutionforyourpassword.R
import com.example.passwordking_onestopsolutionforyourpassword.databinding.FragmentPasswordDecryptionBinding
import java.util.Base64
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


class PasswordDecryption : Fragment() {

    private var progressDialog: ProgressDialog? = null

    private val binding by lazy { FragmentPasswordDecryptionBinding.inflate(layoutInflater) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        progressDialog = ProgressDialog(requireContext())
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_passwordDecryption_to_homeFragment)
        }

        decryptPassword()
        binding.btnCopyPwd.setOnClickListener {
            copyToClipboard(binding.tVDecryptedPassword.text.toString(), "Decrypted Password")
        }
        return binding.root


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun decryptPassword() {
        binding.btnDecrypt.setOnClickListener {
            progressDialog?.setTitle("Encrypting Password")
            progressDialog?.setMessage("Encrypting.Please Wait") // Set a message to be displayed
            progressDialog?.setCancelable(false)

            val password = binding.etEncrytpedPasswordTyped.text.toString()
            val secretKeyString = binding.etSecretKeyTyped.text.toString()

            val secretKey = decodeSecretKey(secretKeyString)

            var decryptedPassword: String? = ""

            if (password.isEmpty() || secretKeyString!!.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter your password and secret key",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                try {
                    decryptedPassword = AES.decrypt(password, secretKey)
                    Log.d("Decrypted Password", decryptedPassword)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                progressDialog!!.show()

                android.os.Handler().postDelayed({
                    // Dismiss the ProgressDialog when the task is done
                    progressDialog?.dismiss()
                    binding.tVDecryptedPassword.text = decryptedPassword
                }, 3000)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun decodeSecretKey(encodedKey: String): SecretKey {
        val decodedKey = Base64.getDecoder().decode(encodedKey)
        return SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
    }

    private fun copyToClipboard(password: String, label: String) {
        val clipBoardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Password", password)
        clipBoardManager.setPrimaryClip(clipData)

        Toast.makeText(requireContext(), "$label Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

}