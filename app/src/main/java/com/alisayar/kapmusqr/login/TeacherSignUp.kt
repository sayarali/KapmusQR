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
import com.alisayar.kapmusqr.databinding.FragmentTeacherSignUpBinding
import com.google.android.material.snackbar.Snackbar

class TeacherSignUp : Fragment() {


    private lateinit var viewModel: TeacherSignUpViewModel
    private lateinit var binding: FragmentTeacherSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_teacher_sign_up, container, false)
        viewModel = ViewModelProvider(this)[TeacherSignUpViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tsuButton.setOnClickListener {
            viewModel.createTeacher()
        }

        viewModel.personalNoError.observe(viewLifecycleOwner, Observer {
            binding.tsuPersonalNoTil.error = it
        })
        viewModel.emailError.observe(viewLifecycleOwner, Observer {
            binding.tsuEmailTil.error = it
        })
        viewModel.nameError.observe(viewLifecycleOwner, Observer {
            binding.tsuNameTil.error = it
        })
        viewModel.surnameError.observe(viewLifecycleOwner, Observer {
            binding.tsuSurnameTil.error = it
        })
        viewModel.passwordError.observe(viewLifecycleOwner, Observer {
            binding.tsuPasswordTil.error = it
        })
        viewModel.passwordCheckError.observe(viewLifecycleOwner, Observer {
            binding.tsuPasswordCheckTil.error = it
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