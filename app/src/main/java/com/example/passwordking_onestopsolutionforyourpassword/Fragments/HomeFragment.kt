package com.example.passwordking_onestopsolutionforyourpassword.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.passwordking_onestopsolutionforyourpassword.R
import com.example.passwordking_onestopsolutionforyourpassword.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {


    private val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.cvPasswordGenerator.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_passwordGenerator)
        }

        binding.cvPasswordList.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_yourPasswordList)
        }

        binding.cvPasswordEncryption.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_passwordEncryption)
        }

        binding.cvPasswordDecryption.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_passwordDecryption)
        }




        return binding.root
    }


}