package com.alisayar.kapmusqr.teacher.add

import android.annotation.SuppressLint
import android.content.res.Resources.Theme
import android.os.Bundle
import android.text.style.TtsSpan.DateBuilder
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alisayar.kapmusqr.DataBinderMapperImpl
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.databinding.FragmentAddLessonsBinding
import com.google.android.material.datepicker.DateSelector
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


class AddLessonsFragment : Fragment() {


    private lateinit var binding: FragmentAddLessonsBinding
    private lateinit var viewModel: AddLessonsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_lessons, container, false)
        viewModel = ViewModelProvider(this)[AddLessonsViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val donemler = resources.getStringArray(R.array.donemler)
        val arrayAdapter = ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, donemler)
        binding.autoCompleteDonemler.setAdapter(arrayAdapter)
        val siniflar = resources.getStringArray(R.array.sinif)
        val arrayAdapterS = ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, siniflar)
        binding.autoCompleteSinifi.setAdapter(arrayAdapterS)
        val derslik = resources.getStringArray(R.array.derslik)
        val arrayAdapterD = ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, derslik)
        binding.autoCompleteDerslik.setAdapter(arrayAdapterD)
        val gunler = resources.getStringArray(R.array.gunler)
        val arrayAdapterG = ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, gunler)
        binding.autoCompleteGun.setAdapter(arrayAdapterG)
        viewModel.donem.observe(viewLifecycleOwner, Observer {
            println(it)
        })
        setHasOptionsMenu(true)

        viewModel.dersKoduError.observe(viewLifecycleOwner, Observer {
            binding.dersKoduTil.error = it
        })
        viewModel.dersAdiError.observe(viewLifecycleOwner, Observer {
            binding.addLessonNameTil.error = it
        })
        viewModel.donemError.observe(viewLifecycleOwner, Observer {
            binding.donemTil.error = it
        })
        viewModel.sinifError.observe(viewLifecycleOwner, Observer {
            binding.sinifTil.error = it
        })
        viewModel.derslikError.observe(viewLifecycleOwner, Observer {
            binding.derslikTil.error = it
        })
        viewModel.gunError.observe(viewLifecycleOwner, Observer {
            binding.gunTil.error = it
        })
        viewModel.saatError.observe(viewLifecycleOwner, Observer {
            binding.saatTil.error = it
        })
        viewModel.toastError.observe(viewLifecycleOwner, Observer {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
            viewModel.clearToastError()
        })

        viewModel.saveEvent.observe(viewLifecycleOwner, Observer {
            findNavController().navigateUp()
        })



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