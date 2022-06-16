package com.genta.storyapp.View.Register

import android.content.ContentValues
import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.genta.storyapp.Data.API.ApiConfig
import com.genta.storyapp.Data.Response.RegisterResponse
import com.genta.storyapp.R
import retrofit2.Call
import retrofit2.Response


class RegisterViewModel: ViewModel() {
    private val _loading =  MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _APImessage = MutableLiveData<String>()
    val APImessage : LiveData<String> = _APImessage

    private val _nameEmpty = MutableLiveData<Boolean>()
    val nameEmpty : LiveData<Boolean> = _nameEmpty

    private val _emailEmpty = MutableLiveData<Boolean>()
    val emailEmpty : LiveData<Boolean>  = _nameEmpty

    private  val _passwordEmpty = MutableLiveData<Boolean>()
    val passwordEmpty : LiveData<Boolean> = _passwordEmpty

    private  val _emailValidasi = MutableLiveData<Boolean>()
    val emailValidasi : LiveData<Boolean> = _emailValidasi

    private val _passwordValidasi = MutableLiveData<Boolean>()
    val passwordValidasi : LiveData<Boolean> = _passwordValidasi

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

    private fun checkEmptyForm(name : String , email : String , password : String): Boolean{
        var notEmpty = true

        if ( name.isEmpty()){
            _nameEmpty.value = true
            notEmpty = false
        }
        else if ( email.isEmpty()){
            _emailEmpty.value = true
            notEmpty = false
        }
        else if (password.isEmpty()){
            _passwordEmpty.value = true
            notEmpty = false
        }
        return notEmpty
    }

    private fun formValidasi(name : String , email : String , password : String) : Boolean{
        var cekValid = true

        if ( checkEmptyForm(name,email, password)){
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
    fun Register(context: Context, name: String, email: String, password: String){
        if(formValidasi(name,email, password)){
            ApiConfig.getApiService()
                .postRegister(name, email, password)
                .enqueue(object : retrofit2.Callback<RegisterResponse>{
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        val _response = response.body()
                        if (response.isSuccessful && _response != null ){
                            Log.d("RegisterViewModel","sukses")
                            val error = _response.error
                            if (!error){
                                _loading.value = false
                                _APImessage.value = _response.message
                            }else{
                                _loading.value = true
                                _APImessage.value = "register gagal"

                            }

                        }else{
                            Log.d("RegisterViewModel","email telah di guanakan")
                            _loading.value = true
                            _APImessage.value = context.getString(R.string.fail)
                        }


                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        _loading.value=true
                        _APImessage.value=t.message
                        Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")

                    }
                })

        }
    }
}