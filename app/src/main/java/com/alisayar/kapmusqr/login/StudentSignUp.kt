package com.alisayar.kapmusqr.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alisayar.kapmusqr.MainActivity
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.databinding.FragmentStudentSignInBinding
import com.alisayar.kapmusqr.databinding.FragmentStudentSignUpBinding
import com.google.android.material.snackbar.Snackbar


class StudentSignUp : Fragment() {


    private lateinit var binding: FragmentStudentSignUpBinding
    private lateinit var viewModel: StudentSignUpViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_sign_up, container, false)
        viewModel = ViewModelProvider(this)[StudentSignUpViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ssuButton.setOnClickListener {
            viewModel.createStudent()
        }

        viewModel.studentNoError.observe(viewLifecycleOwner, Observer {
            binding.ssuStudentNoTil.error = it
        })
        viewModel.emailError.observe(viewLifecycleOwner, Observer {
            binding.ssuEmailTil.error = it
        })
        viewModel.nameError.observe(viewLifecycleOwner, Observer {
            binding.ssuNameTil.error = it
        })
        viewModel.surnameError.observe(viewLifecycleOwner, Observer {
            binding.ssuSurnameTil.error = it
        })
        viewModel.passwordError.observe(viewLifecycleOwner, Observer {
            binding.ssuPasswordTil.error = it
        })
        viewModel.passwordCheckError.observe(viewLifecycleOwner, Observer {
            binding.ssuPasswordCheckTil.error = it
        })

        viewModel.toastError.observe(viewLifecycleOwner, Observer {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        })

        viewModel.successRegisterEvent.observe(viewLifecycleOwner, Observer {
            if(it){
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                viewModel.registerEventComplete()
                requireActivity().finish()
            }
        })


    }

}