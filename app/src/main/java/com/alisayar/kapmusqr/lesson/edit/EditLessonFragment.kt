package com.alisayar.kapmusqr.lesson.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.databinding.FragmentEditLessonBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


class EditLessonFragment : Fragment() {
    private lateinit var binding: FragmentEditLessonBinding
    private lateinit var viewModelFactory: EditLessonViewModelFactory
    private lateinit var viewModel: EditLessonViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_lesson, container, false)
        val argument = EditLessonFragmentArgs.fromBundle(requireArguments())
        viewModelFactory = EditLessonViewModelFactory(argument.lessonModel)
        viewModel = ViewModelProvider(this, viewModelFactory)[EditLessonViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.lesson.observe(viewLifecycleOwner, Observer {
            binding.apply {
                lessonNameEt.setText(it.dersAdi)
                autoCompleteDonemler.setText(it.donem)
                val donemler = resources.getStringArray(R.array.donemler)
                val arrayAdapter = ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, donemler)
                autoCompleteDonemler.setAdapter(arrayAdapter)
                autoCompleteSinifi.setText(it.sinif)
                val siniflar = resources.getStringArray(R.array.sinif)
                val arrayAdapterS = ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, siniflar)
                autoCompleteSinifi.setAdapter(arrayAdapterS)
                autoCompleteDerslik.setText(it.derslik)
                val derslik = resources.getStringArray(R.array.derslik)
                val arrayAdapterD = ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, derslik)
                autoCompleteDerslik.setAdapter(arrayAdapterD)
                autoCompleteGun.setText(it.gun)
                val gunler = resources.getStringArray(R.array.gunler)
                val arrayAdapterG = ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, gunler)
                autoCompleteGun.setAdapter(arrayAdapterG)
                saatText.setText(it.saat)
                val picker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(9)
                    .setMinute(0)
                    .setTitleText("Ders Saati")
                    .build()
                binding.saatTil.setEndIconOnClickListener { picker.show(childFragmentManager, "SAAT") }
                binding.saatText.setOnClickListener { picker.show(childFragmentManager, "SAAT") }
                picker.addOnPositiveButtonClickListener {
                    binding.saatText.setText("${picker.hour}:${picker.minute}")
                }
            }
        })

        viewModel.saveEvent.observe(viewLifecycleOwner, Observer {
            if (it){
                findNavController().popBackStack()
            }
        })

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_lessons_save_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save -> {
                viewModel.save()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}