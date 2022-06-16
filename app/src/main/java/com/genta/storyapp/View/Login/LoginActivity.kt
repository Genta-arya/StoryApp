package com.genta.storyapp.View.Login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.genta.storyapp.Model.UserModel
import com.genta.storyapp.Model.UserPreference
import com.genta.storyapp.R
import com.genta.storyapp.View.Main.MainActivity
import com.genta.storyapp.ViewModelFactory
import com.genta.storyapp.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private val Context.dataStores: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginActivity: ActivityLoginBinding
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginActivity.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        loginActivity.loginButton.setOnClickListener {
            val emailUsers = loginActivity.emailEditText.text.toString().trim()
            val passwordUsers = loginActivity.passwordEditText.text.toString().trim()
            loginViewModel.getLogin( this,emailUsers, passwordUsers)
            Toast.makeText(this, "Tunggu Sebentar", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStores))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this, { user ->
            this.user = user

            if (this.user.isLogin) {
                AlertDialog.Builder(this).apply {
                    setTitle("Login Sukses")
                    setMessage("Selamat Datang " + user.name + " di Aplikasi Story App")
                    setPositiveButton("Lanjut") { _, _ ->
                        Intent(context, MainActivity::class.java).let{
                            it.putExtra(MainActivity.Token,user.token)
                            Log.d("imgcek","link image${user.token}")
                            startActivity(it)
                            finish()
                        }
                    }
                    create()
                    show()
                }
            }

            loginViewModel.APImessage.observe(this, { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            })


            loginViewModel.passwordEmpty.observe(this, {
                if (it) loginActivity.passwordEditTextLayout.setError(getString(R.string.password_error))
            })

            loginViewModel.emailEmpty.observe(this, {
                if (it) loginActivity.emailEditTextLayout.setError(getString(R.string.mail_error))
            })
            loginViewModel.passwordValidasi.observe(this, {
                if (!it) loginActivity.passwordEditTextLayout.setError(getString(R.string.password_min))

            })
        })
    }


    private fun setupView() {
        supportActionBar?.hide()
    }
}
