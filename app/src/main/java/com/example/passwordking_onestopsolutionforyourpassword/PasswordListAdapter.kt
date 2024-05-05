package com.example.passwordking_onestopsolutionforyourpassword

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordking_onestopsolutionforyourpassword.DAO.Password
import com.example.passwordking_onestopsolutionforyourpassword.DAO.PasswordDatabase
import com.example.passwordking_onestopsolutionforyourpassword.databinding.RvItemViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PasswordListAdapter(private val passwordList: List<Password>) :
    RecyclerView.Adapter<PasswordListAdapter.PasswordItemViewHolder>() {


    class PasswordItemViewHolder(private val binding: RvItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {


        init {
            // Set click listeners for copy and edit buttons
            binding.ivCopy.setOnClickListener {
                // Copy password to clipboard

                val password =
                    "Title = ${binding.tvPwdTitle.text.toString()}, Password = ${binding.tvPwd.text.toString()}"
                copyToClipboard(password)
            }

            binding.ivEdit.setOnClickListener {
                val context = itemView.context
                val password = binding.tvPwd.text.toString()
                val title = binding.tvPwdTitle.text.toString()
                showEditDialog(context, title, password)
            }

            binding.ivDelete.setOnClickListener {
                val context = itemView.context
                val password = binding.tvPwd.text.toString()
                val title = binding.tvPwdTitle.text.toString()

                val alertDialog = AlertDialog.Builder(context)
                    .setTitle("Delete Password")
                    .setMessage("Are you sure you want to delete this password?")
                    .setPositiveButton("Delete") { _, _ ->
                        deletePassword(title, password)
                        Toast.makeText(itemView.context, "Please Refresh", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .setNegativeButton("Cancel", null)
                    .create()

                alertDialog.show()

            }

            binding.ivShare.setOnClickListener {
                val context = itemView.context
                val title = binding.tvPwdTitle.text.toString()
                val password = binding.tvPwd.text.toString()

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Title: $title\nPassword: $password")
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share Password"))
            }


        }

        private fun deletePassword(title: String, password: String) {
            val passwordDAO = PasswordDatabase.getInstance(itemView.context).passwordDao()
            CoroutineScope(Dispatchers.IO).launch {
                val passwordEntity = passwordDAO.getPasswordByNameAndPassword(title, password)
                passwordEntity?.let {
                    passwordDAO.deletePassword(passwordEntity)

                }


            }
        }

        private fun showEditDialog(context: Context, title: String, password: String) {

            val passwordDAO = PasswordDatabase.getInstance(context).passwordDao()
            var passwordEntity: Password? = null
            CoroutineScope(Dispatchers.Main).launch {
                passwordEntity = withContext(Dispatchers.IO) {
                    passwordDAO.getPasswordByPassword(password)
                }
            }

            val dialogView = LayoutInflater.from(context).inflate(R.layout.edit_dialog, null)
            val builder = AlertDialog.Builder(context)
            builder.setView(dialogView)
            val dialog = builder.create()


            val etNewTitle = dialogView.findViewById<EditText>(R.id.etNewTitle)
            val etNewPassword = dialogView.findViewById<EditText>(R.id.etNewPassword)
            val btnSave = dialogView.findViewById<Button>(R.id.btnSave)
            val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

            etNewTitle.setText(title)
            etNewPassword.setText(password)

            btnSave.setOnClickListener {
                val newTitle = etNewTitle.text.toString()
                val newPassword = etNewPassword.text.toString()

                if (newTitle.isNotEmpty() && newPassword.isNotEmpty()) {
                    passwordEntity.let {
                        updatePassword(it!!.id, newTitle, newPassword)
                    }

                    dialog.dismiss()

                    Toast.makeText(context, "Please Refresh Once", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(context, "Title or password cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        private fun updatePassword(passwordId: Long, newTitle: String, newPassword: String) {

            val passwordDao = PasswordDatabase.getInstance(itemView.context).passwordDao()

            CoroutineScope(Dispatchers.IO).launch {
                val password: Password = passwordDao.getPasswordById(passwordId)!!

                password.let {
                    val updatedPassword = Password(passwordId, newTitle, newPassword)
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.d("Update", "${password.password}||${password.title}||${password.id}")
                        passwordDao.updatePassword(updatedPassword)
                    }
                }
            }


        }


        fun bind(title: String, password: String) {

            binding.tvPwdTitle.text = title
            binding.tvPwd.text = password
        }

        fun copyToClipboard(text: String) {
            val clipboardManager =
                itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Password", text)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(itemView.context, "Password copied to clipboard", Toast.LENGTH_SHORT)
                .show()
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PasswordListAdapter.PasswordItemViewHolder {
        val binding = RvItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PasswordItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PasswordListAdapter.PasswordItemViewHolder,
        position: Int
    ) {
        val password = passwordList[position]
        holder.bind(password.title, password.password)
    }

    override fun getItemCount(): Int {
        return passwordList.size
    }


}