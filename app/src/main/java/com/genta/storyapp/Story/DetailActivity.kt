package com.genta.storyapp.Story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.genta.storyapp.Data.Response.ResponseGetStory
import com.genta.storyapp.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    companion object{
        const val datas = "get data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Detail Story"

        val getData = intent.getParcelableExtra<ResponseGetStory>(datas)
        Glide.with(this).load(getData!!.photoUrl).into(binding.imageView4)
        Log.d("cek_profil","ini adalah ${getData}")

        binding.apply {
            tvName.setText("Nama : "+getData.name)
            tvDeskripsi.setText("Deskripsi : "+getData.description)
            tvDate.setText("Created at : "+getData.createdAt)
        }

    }
}