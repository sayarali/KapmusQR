package com.alisayar.kapmusqr.qr

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alisayar.kapmusqr.R
import com.alisayar.kapmusqr.databinding.FragmentCreateQRBinding
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.QRCodeDecoderMetaData
import com.google.zxing.qrcode.encoder.QRCode
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.security.cert.PKIXRevocationChecker.Option


class CreateQRFragment : Fragment() {


    private lateinit var binding: FragmentCreateQRBinding
    private lateinit var viewModelFactory: CreateQRViewModelFactory
    private lateinit var viewModel: CreateQRViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_q_r, container, false)
        val argument = CreateQRFragmentArgs.fromBundle(requireArguments())
        viewModelFactory = CreateQRViewModelFactory(argument.lessonModel)
        viewModel = ViewModelProvider(this, viewModelFactory)[CreateQRViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val haftalar = resources.getStringArray(R.array.haftalar)
        val arrayAdapter = ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, haftalar)
        binding.autoCompleteHaftalar.setAdapter(arrayAdapter)


        viewModel.haftaError.observe(viewLifecycleOwner, Observer {
            binding.haftaTil.error = it
        })

        binding.createButton.setOnClickListener {
            viewModel.create()
        }










    }


}