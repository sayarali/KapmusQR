package com.alisayar.kapmusqr.lesson.devamsizlik

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.adapter.DevamsizlikOgrencilerRecyclerAdapter
import com.alisayar.kapmusqr.adapter.StudentOnClickListener
import com.alisayar.kapmusqr.databinding.FragmentDevamDetayBinding


class DevamDetayFragment : Fragment() {

    private lateinit var binding: FragmentDevamDetayBinding
    private lateinit var viewModelFactory: DevamDetayViewModelFactory
    private lateinit var viewModel: DevamDetayViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_devam_detay, container, false)
        val argument = DevamDetayFragmentArgs.fromBundle(requireArguments())
        viewModelFactory = DevamDetayViewModelFactory(argument.hafta, argument.lessonModel)
        viewModel = ViewModelProvider(this, viewModelFactory)[DevamDetayViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.devamsizlikRecyclerRow.adapter = DevamsizlikOgrencilerRecyclerAdapter(
            StudentOnClickListener {
                val action = DevamDetayFragmentDirections.actionDevamDetayFragmentToStudentProfileFragment(it.id)
                findNavController().navigate(action)
            })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.haftaAdi.observe(viewLifecycleOwner, Observer {
            (activity as AppCompatActivity).supportActionBar?.title = it
        })



    }

}