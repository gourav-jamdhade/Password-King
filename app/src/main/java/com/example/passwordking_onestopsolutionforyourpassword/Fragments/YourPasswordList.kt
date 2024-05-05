package com.example.passwordking_onestopsolutionforyourpassword.Fragments

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.passwordking_onestopsolutionforyourpassword.DAO.PasswordDatabase
import com.example.passwordking_onestopsolutionforyourpassword.PasswordListAdapter
import com.example.passwordking_onestopsolutionforyourpassword.PasswordListViewModel
import com.example.passwordking_onestopsolutionforyourpassword.R
import com.example.passwordking_onestopsolutionforyourpassword.databinding.FragmentYourPasswordListBinding
import kotlinx.coroutines.launch


class YourPasswordList : Fragment() {

    private lateinit var viewModel: PasswordListViewModel
    private lateinit var passwordListAdapter: PasswordListAdapter
    private val binding by lazy {
        FragmentYourPasswordListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_yourPasswordList_to_homeFragment)
        }
        val database = PasswordDatabase.getInstance(requireContext())
        viewModel = PasswordListViewModel(database.passwordDao())
        lifecycleScope.launch {
            passwordListAdapter = PasswordListAdapter(viewModel.getPasswordList())
            binding.rvPasswords.adapter = passwordListAdapter

        }


        binding.iconRefresh.setOnClickListener {
            findNavController().navigate(R.id.action_yourPasswordList_to_yourPasswordList)
        }

        return binding.root
    }






}