package com.genta.storyapp.View.Login

import android.content.ContentValues
import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.*
import com.genta.storyapp.Data.API.*
import com.genta.storyapp.Data.Response.ResponseLogin
import com.genta.storyapp.Model.UserModel
import com.genta.storyapp.Model.UserPreference
import com.genta.storyapp.R
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class LoginViewModel(private var preference: UserPreference) : ViewModel() {

    private val _APImessage = MutableLiveData<String>()
    val APImessage : LiveData<String> = _APImessage
    private val _emailEmpty = MutableLiveData<Boolean>()
    val emailEmpty : LiveData<Boolean>  = _emailEmpty
    private  val _passwordEmpty = MutableLiveData<Boolean>()
    val passwordEmpty : LiveData<Boolean> = _passwordEmpty
    private  val _emailValidasi = MutableLiveData<Boolean>()
    val emailValidasi : LiveData<Boolean> = _emailValidasi
    private val _passwordValidasi = MutableLiveData<Boolean>()
    val passwordValidasi : LiveData<Boolean> = _passwordValidasi

    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

    fun login(users: UserModel) {
        viewModelScope.launch {
            preference.login(users)
        }
    }
    private fun checkEmptyForm(email : String , password : String): Boolean{
        var notEmpty = true

        if ( email.length == 0){
            _emailEmpty.value = true
            notEmpty = false
        }
       else if (password.length == 0){
            _passwordEmpty.value = true
            notEmpty = false
        }
        return notEmpty
    }

    private fun formValidasi( email : String , password : String) : Boolean{
        var cekValid = true

        if ( checkEmptyForm(email, password )){
            if (!emailValidasi(email)){
                _emailValidasi.value = false
                cekValid = false
            }
            else if(!passwordValidasi(password)){
                _passwordValidasi.value = false
                cekValid = false
            }

        }
        else cekValid = false
        return cekValid
    }
    fun getLogin(context: Context,email: String, password: String) {
        if (formValidasi(email,password )){
            ApiConfig.getApiService()

                .postLogin(email, password)
                .enqueue(object : retrofit2.Callback<ResponseLogin> {
                    override fun onResponse(
                        call: Call<ResponseLogin>,
                        response: Response<ResponseLogin>
                    ) {
                        val _response = response.body()
                        if (response.isSuccessful && _response != null) {
                            Log.d("LoginViewModel", "Login Berhasil")
                            val users = UserModel(
                                _response.loginResult.userId,
                                _response.loginResult.name,
                                true,
                                _response.loginResult.token
                            )
                            login(users)

                        } else {
                            Log.d("LoginViewModel", "Akun salah")
                            _APImessage.value = context.getString(R.string.fail_login)
                        }


                    }

                    override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                        _APImessage.value = t.message
                        Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")

                    }
                })

        }
    }

    private fun emailValidasi(email : String) : Boolean{
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }
    private fun passwordValidasi(password : String) : Boolean {
        return password.length  >= 6

    }

}