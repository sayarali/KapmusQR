package com.alisayar.kapmusqr.lesson

import android.R.attr.button
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.DialogInterface.OnShowListener
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.databinding.FragmentLessonBinding
import com.google.android.material.snackbar.Snackbar


class LessonFragment : Fragment() {


    private lateinit var binding: FragmentLessonBinding
    private lateinit var viewModelFactory: LessonViewModelFactory
    private lateinit var viewModel: LessonViewModel
    private lateinit var argument: LessonFragmentArgs
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lesson, container, false)
        argument = LessonFragmentArgs.fromBundle(requireArguments())
        viewModelFactory = LessonViewModelFactory(argument.lessonModel)
        viewModel = ViewModelProvider(this, viewModelFactory)[LessonViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dersAdi.observe(viewLifecycleOwner, Observer {
            (activity as AppCompatActivity).supportActionBar?.title = it
        })

        viewModel.toastError.observe(viewLifecycleOwner, Observer {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        })
        viewModel.popUpEvent.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.completePopUp()
                findNavController().navigateUp()
            }
        })

        viewModel.studentNo.observe(viewLifecycleOwner, Observer {
            println(it)
        })

        binding.teacherButton.setOnClickListener{
            val action = viewModel.ogretimGorevlisi.value?.let { it1 ->
                LessonFragmentDirections.actionLessonFragmentToTeacherProfileFragment(
                    it1.id)
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }


        binding.haftaBir.setOnClickListener {
            val action = LessonFragmentDirections.actionLessonFragmentToDevamDetayFragment("1. Hafta", argument.lessonModel)
            findNavController().navigate(action)
        }
        binding.haftaIki.setOnClickListener{
            val action = LessonFragmentDirections.actionLessonFragmentToDevamDetayFragment("2. Hafta", argument.lessonModel)
            findNavController().navigate(action)
        }
        binding.haftaUc.setOnClickListener{
            val action = LessonFragmentDirections.actionLessonFragmentToDevamDetayFragment("3. Hafta", argument.lessonModel)
            findNavController().navigate(action)
        }
        binding.haftaDort.setOnClickListener{
            val action = LessonFragmentDirections.actionLessonFragmentToDevamDetayFragment("4. Hafta", argument.lessonModel)
            findNavController().navigate(action)
        }
        binding.haftaBes.setOnClickListener{
            val action = LessonFragmentDirections.actionLessonFragmentToDevamDetayFragment("5. Hafta", argument.lessonModel)
            findNavController().navigate(action)
        }
        binding.haftaAlti.setOnClickListener{
            val action = LessonFragmentDirections.actionLessonFragmentToDevamDetayFragment("6. Hafta", argument.lessonModel)
            findNavController().navigate(action)
        }
        binding.haftaYedi.setOnClickListener{
            val action = LessonFragmentDirections.actionLessonFragmentToDevamDetayFragment("7. Hafta", argument.lessonModel)
            findNavController().navigate(action)
        }
        binding.haftaSekiz.setOnClickListener{
            val action = LessonFragmentDirections.actionLessonFragmentToDevamDetayFragment("8. Hafta", argument.lessonModel)
            findNavController().navigate(action)
        }
        binding.haftaDokuz.setOnClickListener{
            val action = LessonFragmentDirections.actionLessonFragmentToDevamDetayFragment("9. Hafta", argument.lessonModel)
            findNavController().navigate(action)
        }
        binding.haftaOn.setOnClickListener{
            val action = LessonFragmentDirections.actionLessonFragmentToDevamDetayFragment("10. Hafta", argument.lessonModel)
            findNavController().navigate(action)
        }
        binding.haftaOnbir.setOnClickListener{
            val action = LessonFragmentDirections.actionLessonFragmentToDevamDetayFragment("11. Hafta", argument.lessonModel)
            findNavController().navigate(action)
        }
        binding.haftaOniki.setOnClickListener{
            val action = LessonFragmentDirections.actionLessonFragmentToDevamDetayFragment("12. Hafta", argument.lessonModel)
            findNavController().navigate(action)
        }
        binding.haftaOnuc.setOnClickListener{
            val action = LessonFragmentDirections.actionLessonFragmentToDevamDetayFragment("13. Hafta", argument.lessonModel)
            findNavController().navigate(action)
        }
        binding.haftaOndort.setOnClickListener{
            val action = LessonFragmentDirections.actionLessonFragmentToDevamDetayFragment("14. Hafta", argument.lessonModel)
            findNavController().navigate(action)
        }



        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.lessons_teacher_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_student -> {

                val inflater = layoutInflater
                val dialogLayout = inflater.inflate(R.layout.add_student_dialog, null)
                val editText  = dialogLayout.findViewById<EditText>(R.id.add_student_no_et)
                val builder = AlertDialog.Builder(requireContext())
                    .setTitle("Öğrenci Ekle")
                    .setView(dialogLayout)
                    .setPositiveButton("Ekle", null)
                    .setNegativeButton("Kapat", null)
                    .create()

                builder.setOnShowListener(OnShowListener{
                    val positiveButton = builder.getButton(AlertDialog.BUTTON_POSITIVE)
                    positiveButton.setOnClickListener(View.OnClickListener {
                        if(editText.text.toString().isNotEmpty()){
                            viewModel.addStudent(editText.text.toString())
                        }
                    })
                })


                builder.show()

            }
            R.id.edit_lesson -> {
                println("Ders Düzenleme")
            }
            R.id.delete_lesson -> {
                viewModel.deleteLesson()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}

