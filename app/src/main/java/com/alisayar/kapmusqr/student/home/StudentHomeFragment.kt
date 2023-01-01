package com.alisayar.kapmusqr.student.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.adapter.LessonRecyclerAdapter
import com.alisayar.kapmusqr.adapter.OnClickListener
import com.alisayar.kapmusqr.databinding.FragmentStudentHomeBinding
import com.alisayar.kapmusqr.teacher.home.TeacherHomeFragmentDirections


class StudentHomeFragment : Fragment() {


    private lateinit var binding: FragmentStudentHomeBinding
    private lateinit var viewModel: StudentHomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_home, container, false)
        viewModel = ViewModelProvider(this)[StudentHomeViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.studentHomeRecyclerView.adapter = LessonRecyclerAdapter(OnClickListener {
            val action = StudentHomeFragmentDirections.actionStudentHomeFragmentToLessonFragment(it)
            findNavController().navigate(action)
        })

        binding.studentHomeSwipeRefresh.setOnRefreshListener {
            viewModel.getLessonsData()
        }

        viewModel.isRefreshing.observe(viewLifecycleOwner, Observer {
            binding.studentHomeSwipeRefresh.isRefreshing = it
        })

    }


}