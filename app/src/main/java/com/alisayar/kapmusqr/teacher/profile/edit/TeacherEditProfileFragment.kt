package com.alisayar.kapmusqr.teacher.profile.edit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.databinding.FragmentTeacherEditProfileBinding
import com.alisayar.kapmusqr.login.LoginActivity
import com.alisayar.kapmusqr.teacher.profile.TeacherProfileFragmentDirections


class TeacherEditProfileFragment : Fragment() {


    private lateinit var binding: FragmentTeacherEditProfileBinding
    private lateinit var viewModel: TeacherEditProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_teacher_edit_profile, container, false)
        viewModel = ViewModelProvider(this)[TeacherEditProfileViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.ogretimGorevlisi.observe(viewLifecycleOwner, Observer {
            binding.apply {
                tePersonalNoEt.setText(it.personalNo)
                teNameEt.setText(it.name)
                teSurnameEt.setText(it.surname)
                teRoomNumberEt.setText(it.roomNumber)
                tePhoneNumberEt.setText(it.phoneNumber)
            }
        })

        binding.fotografiDegistir.setOnClickListener {
            requestPermission()
        }

        viewModel.updatedProfileEvent.observe(viewLifecycleOwner, Observer {
            if(it){
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
               viewModel.saveButton()
           }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun requestPermission(){
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 2)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 2)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 2){
            if(resultCode == Activity.RESULT_OK && data != null){

                val chosenUri = data.data
                var chosenBitmap: Bitmap
                chosenUri?.let {

                    if(Build.VERSION.SDK_INT >= 28){
                        val source = ImageDecoder.createSource(requireContext().contentResolver, chosenUri)
                        chosenBitmap = ImageDecoder.decodeBitmap(source)
                        binding.profilePicture.setImageBitmap(chosenBitmap)
                        viewModel.chosenImage(chosenBitmap)
                    } else {
                        chosenBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, chosenUri)
                        binding.profilePicture.setImageBitmap(chosenBitmap)
                        viewModel.chosenImage(chosenBitmap)
                    }

                }

            }
        }
    }

}