package com.alisayar.kapmusqr.lesson.addstudent

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.databinding.FragmentAddStudentBinding


class AddStudentFragment : Fragment() {


    private lateinit var binding: FragmentAddStudentBinding
    private lateinit var viewModelFactory: AddStudentViewModelFactory
    private lateinit var viewModel: AddStudentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_student, container, false)
        val argument = AddStudentFragmentArgs.fromBundle(requireArguments())
        viewModelFactory = AddStudentViewModelFactory(argument.lessonModel)
        viewModel = ViewModelProvider(this, viewModelFactory)[AddStudentViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}