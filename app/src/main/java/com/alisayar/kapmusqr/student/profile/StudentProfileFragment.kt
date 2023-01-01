package com.alisayar.kapmusqr.student.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.databinding.FragmentStudentProfileBinding
import com.alisayar.kapmusqr.login.LoginActivity
import com.alisayar.kapmusqr.teacher.profile.TeacherProfileFragmentDirections
import com.google.firebase.auth.FirebaseAuth


class StudentProfileFragment : Fragment() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: FragmentStudentProfileBinding
    private lateinit var viewModelFactory: StudentProfileViewModelFactory
    private lateinit var viewModel: StudentProfileViewModel
    var studentId = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_profile, container, false)
        studentId = auth.currentUser!!.uid
        arguments?.let {
            if(it.getString("studentId") != null){
                studentId = it.getString("studentId")!!
            }
        }
        viewModelFactory = StudentProfileViewModelFactory(studentId)
        viewModel = ViewModelProvider(this, viewModelFactory)[StudentProfileViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(studentId == auth.currentUser!!.uid){
            setHasOptionsMenu(true)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.teacher_profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit_profile_item -> {
                val action = StudentProfileFragmentDirections.actionStudentProfileFragmentToStudentEditProfileFragment()
                findNavController().navigate(action)
            }
            R.id.sign_out_item -> {
                auth.signOut()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}