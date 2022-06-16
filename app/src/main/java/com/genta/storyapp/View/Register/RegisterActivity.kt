package com.genta.storyapp.View.Register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.genta.storyapp.R
import com.genta.storyapp.View.Login.LoginActivity
import com.genta.storyapp.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {
    private  lateinit var registerActivity: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerActivity = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerActivity.root)

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        setupView()
        setupAction()
        setupPlayAnimation()


        registerViewModel.loading.observe(this,{

            isLoadingIntent(it)
        })

        registerViewModel.APImessage.observe(this,{
            message -> Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        })

        registerViewModel.nameEmpty.observe(this,{
            if(it) registerActivity.nameEditTextLayout.setError(getString(R.string.name_error))
        })

        registerViewModel.passwordEmpty.observe(this,{
            if (it) registerActivity.passwordEditTextLayout.setError(getString(R.string.password_error))
        })

        registerViewModel.emailEmpty.observe(this,{
            if (it) registerActivity.emailEditTextLayout.setError(getString(R.string.mail_error))
        })
        registerViewModel.passwordValidasi.observe(this,{
            if(!it) registerActivity.passwordEditTextLayout.setError(getString(R.string.password_min))
        })



    }

    private fun setupPlayAnimation() {
        val title = ObjectAnimator.ofFloat(registerActivity.titleTextView, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(registerActivity.nameTextView, View.ALPHA, 1f).setDuration(500)
        val name_l = ObjectAnimator.ofFloat(registerActivity.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val name_text = ObjectAnimator.ofFloat(registerActivity.nameEditText, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(registerActivity.emailTextView, View.ALPHA, 1f).setDuration(500)
        val email_l = ObjectAnimator.ofFloat(registerActivity.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val email_text = ObjectAnimator.ofFloat(registerActivity.emailEditText, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(registerActivity.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val pass_text = ObjectAnimator.ofFloat(registerActivity.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val pass_l = ObjectAnimator.ofFloat(registerActivity.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(registerActivity.signupButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title,name,name_l,name_text,email,email_l,email_text,pass,pass_l,pass_text,signup)
            start()
        }
    }

    private fun setupView() {
        supportActionBar?.hide()
    }
    private fun setupAction(){
        registerActivity.signupButton.setOnClickListener{
            val name = registerActivity.nameEditText.text.toString()
            val email = registerActivity.emailEditText.text.toString()
            val pass = registerActivity.passwordEditText.text.toString()
            registerViewModel.Register(this@RegisterActivity,name,email,pass)
            Toast.makeText(this, "Tunggu Sebentar", Toast.LENGTH_SHORT).show()


        }
    }

    private fun isLoadingIntent(value: Boolean){
        if (value){return}
        else{
            Intent(this, LoginActivity::class.java).let {
                startActivity(it)
                finish()

            }
        }
    }

}