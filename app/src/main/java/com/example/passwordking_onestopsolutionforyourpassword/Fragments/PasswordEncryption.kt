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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.passwordking_onestopsolutionforyourpassword.EncrytionUtils.AES
import com.example.passwordking_onestopsolutionforyourpassword.EncrytionUtils.EncryptionViewModel
import com.example.passwordking_onestopsolutionforyourpassword.R
import com.example.passwordking_onestopsolutionforyourpassword.databinding.FragmentPasswordEncryptionBinding
import java.util.Base64
import javax.crypto.SecretKey


class PasswordEncryption : Fragment() {

    private lateinit var viewModel: EncryptionViewModel
    private var progressDialog: ProgressDialog? = null
    private val binding by lazy {
        FragmentPasswordEncryptionBinding.inflate(layoutInflater)
    }
    private lateinit var secretKey: SecretKey

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(EncryptionViewModel::class.java)
        progressDialog = ProgressDialog(requireContext())
        encryptPassword()
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_passwordEncryption_to_homeFragment)
        }
        binding.btnCopyPwd.setOnClickListener {
            copyToClipboard(binding.tVEncryptedPwd.text.toString(),"Encrypted Password")
        }

        binding.btnCopySecretKey.setOnClickListener {
            copyToClipboard(Base64.getEncoder().encodeToString(secretKey.encoded),"Secret Key")
        }


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun encryptPassword() {
        binding.btnEncrypt.setOnClickListener {
            progressDialog?.setTitle("Encrypting Password")
            progressDialog?.setMessage("Encrypting.Please Wait") // Set a message to be displayed
            progressDialog?.setCancelable(false)
            val plainText = binding.etPasswordTyped.text
            var cipherText: String? = ""

            if (plainText!!.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your password", Toast.LENGTH_SHORT)
                    .show()
            } else {


                try {

                    secretKey = AES.generateAESKey()
                    Log.d("SecretKey", Base64.getEncoder().encodeToString(secretKey.encoded))

                    cipherText = AES.encrypt(plainText.toString(), secretKey)


                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                }

                progressDialog!!.show()

                android.os.Handler().postDelayed({
                    // Dismiss the ProgressDialog when the task is done
                    progressDialog?.dismiss()
                    binding.tVEncryptedPwd.text = cipherText
                    binding.tvSecretKey.text = Base64.getEncoder().encodeToString(secretKey.encoded)
                }, 3000)


            }
        }
    }

    private fun copyToClipboard(password: String,label: String) {
        val clipBoardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Password", password)
        clipBoardManager.setPrimaryClip(clipData)

        Toast.makeText(requireContext(), "$label Copied to clipboard", Toast.LENGTH_SHORT).show()
    }


}