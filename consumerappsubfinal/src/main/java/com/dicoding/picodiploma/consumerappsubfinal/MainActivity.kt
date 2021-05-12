package com.dicoding.picodiploma.consumerappsubfinal

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.consumerappsubfinal.adapter.FavoriteAdapter
import com.dicoding.picodiploma.consumerappsubfinal.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.consumerappsubfinal.databinding.ActivityMainBinding
import com.dicoding.picodiploma.consumerappsubfinal.helper.MappingHelper
import com.dicoding.picodiploma.consumerappsubfinal.model.FavItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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