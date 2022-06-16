package com.genta.storyapp.View.Main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.LogPrinter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.genta.storyapp.Model.UserPreference
import com.genta.storyapp.R
import com.genta.storyapp.Story.ListStoryAdapter
import com.genta.storyapp.Story.LoadAdapter
import com.genta.storyapp.Story.addActivity
import com.genta.storyapp.View.Login.LoginActivity
import com.genta.storyapp.View.Maps.MapsActivity
import com.genta.storyapp.ViewModelFactoryMain
import com.genta.storyapp.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var mainActivity: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    companion object {
        var Token = "token"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivity.root)
        setupView()
    }



    private fun setupView() {
        mainViewModel = ViewModelProvider(this,
            ViewModelFactoryMain(this,UserPreference.getInstance(dataStore))
        ).get(MainViewModel::class.java)

        mainViewModel.Story.observe(this,{
            setUpAdapter()
        })
        mainViewModel.getUser().observe(this, { user ->
            if (user.isLogin){
               supportActionBar?.title = "User : "+ user.name
                user.token.let { mainViewModel.getStoryuser(user.token) }

            }
        })
        mainViewModel.Loading.observe(this,{
            setLoading(it)
        })
        mainViewModel.APIresult.observe(this,{
            Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
        })

        mainActivity.apply {
            mainActivity.floatingBtn.setOnClickListener{
                mainViewModel.getUser().observe(this@MainActivity,{user ->
                    Intent(this@MainActivity,addActivity::class.java).let {
                        it.putExtra(addActivity.EXTRA_TOKEN,user.token)
                        startActivity(it)

                    }
                })

            }


        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.nav_bar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout->{
                mainViewModel.exit()
                val i = Intent(this@MainActivity,LoginActivity::class.java)
                startActivity(i)
                finish()
                Toast.makeText(this,"Logout Berhasil", Toast.LENGTH_SHORT).show()
            }
            R.id.map->{
                val i = Intent(this@MainActivity,MapsActivity::class.java)
                startActivity(i)


            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun setUpAdapter(){
        val _adapter = ListStoryAdapter()
        lifecycleScope.launch {
            _adapter.loadStateFlow.collect  {
                setLoading(it.refresh is LoadState.Loading)
            }
        }

        mainActivity.apply {
            rvStoryList.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStoryList.setHasFixedSize(true)
            rvStoryList.adapter = _adapter.withLoadStateFooter(
                footer = LoadAdapter{
                    _adapter.retry()
                }
            )
        }

        mainViewModel.StoryUser.observe(this, {
            Log.d("cek ", "list data $it ")
            _adapter.submitData(lifecycle, it)
        })

    }
    private fun setLoading(value:Boolean){
        if (value){
            mainActivity.progressBar.visibility = View.VISIBLE
            mainActivity.rvStoryList.visibility = View.GONE
        }else{
            mainActivity.progressBar.visibility = View.GONE
            mainActivity.rvStoryList.visibility = View.VISIBLE
        }
    }
    




}