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
import com.alisayar.kapmusqr.databinding.FragmentSignInBinding
import com.google.android.material.snackbar.Snackbar


class SignInFragment : Fragment() {


    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginSuccessEvent.observe(viewLifecycleOwner, Observer {
            if(it){
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                viewModel.loginEventComplete()
                requireActivity().finish()
            }
        })

        viewModel.emailError.observe(viewLifecycleOwner, Observer {
            binding.siEmailTil.error = it
        })
        viewModel.passwordError.observe(viewLifecycleOwner, Observer {
            binding.siPasswordTil.error = it
        })
        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        })
    }


}