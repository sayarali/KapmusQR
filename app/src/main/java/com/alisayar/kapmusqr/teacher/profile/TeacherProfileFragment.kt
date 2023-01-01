package com.alisayar.kapmusqr.teacher.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.databinding.FragmentTeacherProfileBinding
import com.alisayar.kapmusqr.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth


class TeacherProfileFragment : Fragment() {


    private lateinit var binding: FragmentTeacherProfileBinding
    private lateinit var viewModelFactory: TeacherProfileViewModelFactory
    private lateinit var viewModel: TeacherProfileViewModel
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var teacherId = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_teacher_profile, container, false)
        teacherId = auth.currentUser!!.uid
        arguments?.let {
            if(it.getString("teacherId") != null){
                teacherId = it.getString("teacherId")!!
            }
        }
        viewModelFactory = TeacherProfileViewModelFactory(teacherId)
        viewModel = ViewModelProvider(this, viewModelFactory)[TeacherProfileViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(teacherId == auth.currentUser!!.uid){
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
                val action = TeacherProfileFragmentDirections.actionTeacherProfileFragmentToTeacherEditProfileFragment()
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