package com.alisayar.kapmusqr.teacher.home

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
import com.alisayar.kapmusqr.databinding.FragmentTeacherHomeBinding
import com.alisayar.kapmusqr.login.TeacherSignInDirections


class TeacherHomeFragment : Fragment() {

    private lateinit var binding: FragmentTeacherHomeBinding
    private lateinit var viewModel: TeacherHomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_teacher_home, container, false)
        viewModel = ViewModelProvider(this)[TeacherHomeViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeFab.setOnClickListener {
            val action = TeacherHomeFragmentDirections.actionTeacherHomeFragmentToAddLessonsFragment()
            findNavController().navigate(action)
        }

        binding.teacherHomeRecyclerView.adapter = LessonRecyclerAdapter(OnClickListener {
            val action = TeacherHomeFragmentDirections.actionTeacherHomeFragmentToLessonFragment(it)
            findNavController().navigate(action)
        })

        binding.teacherHomeSwipeRefresh.setOnRefreshListener {
            viewModel.getLessonsData()
        }

        viewModel.isRefreshing.observe(viewLifecycleOwner, Observer {
            binding.teacherHomeSwipeRefresh.isRefreshing = it
        })


    }

    override fun onResume() {
        super.onResume()
        viewModel.getLessonsData()
    }


}