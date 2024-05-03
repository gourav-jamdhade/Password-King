package com.example.passwordking_onestopsolutionforyourpassword.Fragments

import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
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
import javax.crypto.SecretKey


class PasswordEncryption : Fragment() {

    private lateinit var viewModel: EncryptionViewModel
    private var progressDialog: ProgressDialog? = null
    private val binding by lazy {
        FragmentPasswordEncryptionBinding.inflate(layoutInflater)
    }

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
            copyToClipboard(binding.tVEncryptedPwd.text.toString())
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

                    val secretKey :SecretKey = AES.generateAESKey()

                    cipherText = AES.encrypt(plainText.toString(), secretKey)


                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                }

                progressDialog!!.show()

                android.os.Handler().postDelayed({
                    // Dismiss the ProgressDialog when the task is done
                    progressDialog?.dismiss()
                    binding.tVEncryptedPwd.text = cipherText
                }, 3000)


            }
        }
    }

    private fun copyToClipboard(password: String) {
        val clipBoardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Password", password)
        clipBoardManager.setPrimaryClip(clipData)

        Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }


}