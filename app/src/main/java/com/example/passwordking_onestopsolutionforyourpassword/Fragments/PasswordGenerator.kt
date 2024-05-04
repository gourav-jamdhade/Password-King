package com.example.passwordking_onestopsolutionforyourpassword.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.passwordking_onestopsolutionforyourpassword.DAO.Password
import com.example.passwordking_onestopsolutionforyourpassword.DAO.PasswordDatabase
import com.example.passwordking_onestopsolutionforyourpassword.R
import com.example.passwordking_onestopsolutionforyourpassword.databinding.DialogTitleSaveBinding
import com.example.passwordking_onestopsolutionforyourpassword.databinding.FragmentPasswordGeneratorBinding
import kotlinx.coroutines.launch
import kotlin.random.Random


class PasswordGenerator : Fragment() {

    private var progressDialog: ProgressDialog? = null
    private val char = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val sChar = "abcdefghijklmnopqrstuvwxyz"
    private val digits = "1234567890"
    private val special = "!@#$%^&*()_=+|~?><"

    private var inputText = ""
    private var globalPassword = ""
    private val binding by lazy {
        FragmentPasswordGeneratorBinding.inflate(layoutInflater)
    }

    private val dialogBinding by lazy {
        DialogTitleSaveBinding.inflate(layoutInflater)
    }

    val passwordDb by lazy {
        PasswordDatabase.getInstance(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        copyBtnClicked()
        binding.btnSave.setOnClickListener {
            showStringInputDialog()
        }



        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_passwordGenerator_to_homeFragment)
        }
        val options = arrayOf("4", "8", ">8")

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, options)
        binding.autoComplete.setAdapter(adapter)
        progressDialog = ProgressDialog(requireContext())

        binding.btnGenerate.setOnClickListener {

            if (binding.autoComplete.text.toString() == "") {
                Toast.makeText(
                    requireContext(),
                    "Please select the password length",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                progressDialog?.setTitle("Password Generator")
                progressDialog?.setMessage("Generating.Please Wait") // Set a message to be displayed
                progressDialog?.setCancelable(false)

                progressDialog!!.show()

                val length = binding.autoComplete.text

                Log.d("lnrght", length.toString())
                generatePassword(length.toString())

                android.os.Handler().postDelayed({
                    // Dismiss the ProgressDialog when the task is done
                    progressDialog?.dismiss()
                    binding.tvPassword.visibility = View.VISIBLE
                    binding.btnCopy.visibility = View.VISIBLE
                    binding.btnSave.visibility = View.VISIBLE
                }, 3000)
            }


        }
        return binding.root
    }

    private fun showStringInputDialog() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setView(dialogBinding.root).setTitle("Title").setMessage("Make sure the title is unique.").setCancelable(false)
            .setPositiveButton("Save") { dialog, _ ->

                inputText = dialogBinding.etTitle.text.toString()
                if (inputText.isNotEmpty()) {
                    Log.d("String", inputText)


                    //Insert the password

                    // Insert the password in a background thread using coroutines
                    lifecycleScope.launch {
                        passwordDb.passwordDao().insertPassword(
                            Password(title = inputText, password = globalPassword)
                        )

                        Toast.makeText(requireContext(), "Password Saved!", Toast.LENGTH_SHORT)
                            .show()
                    }


                    inputText = ""
                    findNavController().navigate(R.id.action_passwordGenerator_to_passwordGenerator)
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), "Please Enter a title", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }


        val dialog = builder.create()
// Remove the view from its parent before adding it to the dialog
        val parent = dialogBinding.root.parent as? ViewGroup
        parent?.removeView(dialogBinding.root)


        dialog.show()

    }


    private fun copyBtnClicked() {
        binding.btnCopy.setOnClickListener {
            copyToClipboard(globalPassword)
        }
    }

    private fun generatePassword(length: String?) {

        if (length.equals("4")) {
            password(4)
        } else if (length.equals("8")) {
            password(8)
        } else if (length.equals(">8")) {
            password(15)
        }
    }

    private fun password(i: Int) {
        val random = Random.Default
        val allChars = char + digits + special
        var password = ""
        password += char[random.nextInt(char.length)]
        password += sChar[random.nextInt(sChar.length)]
        password += digits[random.nextInt(digits.length)]
        password += special[random.nextInt(special.length)]

        repeat(i - 4) {
            password += allChars[random.nextInt(allChars.length)]
        }
        password = password.toList().shuffled().joinToString("")
        Log.d("Password", password)
        binding.tvPassword.text = password
        globalPassword = password


    }

    private fun copyToClipboard(password: String) {
        val clipBoardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Password", password)
        clipBoardManager.setPrimaryClip(clipData)

        Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

}