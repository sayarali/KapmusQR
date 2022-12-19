package com.alisayar.kapmusqr.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.studentSignInButton.setOnClickListener{
            val action = LoginFragmentDirections.actionLoginFragmentToStudentSignIn()
            findNavController().navigate(action)
        }
        binding.teacherSignInButton.setOnClickListener{
            val action = LoginFragmentDirections.actionLoginFragmentToTeacherSignIn()
            findNavController().navigate(action)
        }

    }


}