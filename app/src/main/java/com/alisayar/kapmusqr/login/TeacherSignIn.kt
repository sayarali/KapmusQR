package com.alisayar.kapmusqr.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alisayar.kapmusqr.MainActivity
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.databinding.FragmentTeacherSignInBinding
import com.google.android.material.snackbar.Snackbar


class TeacherSignIn : Fragment() {

    private lateinit var binding: FragmentTeacherSignInBinding
    private lateinit var viewModel: TeacherSignInViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_teacher_sign_in, container, false)
        viewModel = ViewModelProvider(this)[TeacherSignInViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tsiSignupButton.setOnClickListener {
            val action = TeacherSignInDirections.actionTeacherSignInToTeacherSignUp()
            findNavController().navigate(action)
        }

        binding.tsiButton.setOnClickListener {
            viewModel.signIn()
        }

        viewModel.emailError.observe(viewLifecycleOwner, Observer {
            binding.tsiEmailTil.error = it
        })

        viewModel.passwordError.observe(viewLifecycleOwner, Observer {
            binding.tsiPasswordTil.error = it
        })

        viewModel.loginSuccessEvent.observe(viewLifecycleOwner, Observer {
            if(it){
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                viewModel.loginEventComplete()
                requireActivity().finish()

            }
        })

        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        })

    }

}