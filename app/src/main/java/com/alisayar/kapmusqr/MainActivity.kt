package com.alisayar.kapmusqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alisayar.kapmusqr.databinding.ActivityMainBinding
import com.alisayar.kapmusqr.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)


        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)


    }

}