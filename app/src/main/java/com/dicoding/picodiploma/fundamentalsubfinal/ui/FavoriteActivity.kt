package com.dicoding.picodiploma.fundamentalsubfinal.ui

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.fundamentalsubfinal.R
import com.dicoding.picodiploma.fundamentalsubfinal.adapter.FavoriteAdapter
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.fundamentalsubfinal.databinding.ActivityFavoriteBinding
import com.dicoding.picodiploma.fundamentalsubfinal.helper.MappingHelper
import com.dicoding.picodiploma.fundamentalsubfinal.model.FavItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.mf_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        showRecyclerList()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavsAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            // proses ambil data
            loadFavsAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavItems>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    private fun showRecyclerList() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        adapter = FavoriteAdapter(this)
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun loadFavsAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            showEmpty(false)
            val deferredFavs = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favs = deferredFavs.await()
            if (favs.size > 0) {
                adapter.listFavorite = favs
                showLoading(false)
            } else {
                adapter.listFavorite = ArrayList()
                showEmpty(true)
                showLoading(false)
            }
        }
    }

    private fun showEmpty(state: Boolean) {
        if (state) {
            binding.empty.layoutEmpty.visibility = View.VISIBLE
        } else {
            binding.empty.layoutEmpty.visibility = View.GONE
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}