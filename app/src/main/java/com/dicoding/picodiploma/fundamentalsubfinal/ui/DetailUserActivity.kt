package com.dicoding.picodiploma.fundamentalsubfinal.ui

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.fundamentalsubfinal.R
import com.dicoding.picodiploma.fundamentalsubfinal.adapter.SectionsPagerAdapter
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.AVATAR_URL
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.COMPANY
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.FULLNAME
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.USERNAME
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.FOLLOWERS
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.FOLLOWING
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion.LOCATION
import com.dicoding.picodiploma.fundamentalsubfinal.database.DatabaseContract.UserColumns.Companion._ID
import com.dicoding.picodiploma.fundamentalsubfinal.databinding.ActivityDetailUserBinding
import com.dicoding.picodiploma.fundamentalsubfinal.helper.MappingHelper
import com.dicoding.picodiploma.fundamentalsubfinal.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var uriWithId: Uri
    private var isFavorite = false
    private var fullName: String? = null
    private var avatar: String? = null
    private var followers: Int? = 0
    private var following: Int? = 0
    private var company: String? = null
    private var location: String? = null

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ID = "extra_id"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(true)

        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.mf_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        val user = intent.getStringExtra(EXTRA_USER)
        val userid = intent.getIntExtra(EXTRA_ID, 0)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java
        )

        mainViewModel.setDetailUser(user.toString())

        mainViewModel.getDetailUser().observe(this, { userItems ->
            binding.apply {
                Glide.with(this@DetailUserActivity)
                    .load(userItems.avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(profileImg)
                tvProfileUsername.text = userItems.username
                tvProfileName.text = userItems.name
                tvProfileLocation.text = userItems.location
                tvProfileCompany.text = userItems.company
                tvFollowersCount.text = userItems.followers.toString()
                tvFollowingCount.text = userItems.following.toString()
                tvRepositoryCount.text = userItems.repository.toString()
            }
            showLoading(false)
            toolbar.title = userItems.username

            fullName = userItems.name.toString()
            avatar = userItems.avatar.toString()
            followers = userItems.followers
            following = userItems.following
            company = userItems.company.toString()
            location = userItems.location.toString()
        })

        //ViewPager2
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = user
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        //Database
        uriWithId = Uri.parse("$CONTENT_URI/$userid")
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        if (cursor != null) {
            val fav = MappingHelper.mapCursorToArrayList(cursor)
            for (data in fav) {
                if (userid == data.id) {
                    cursor.close()
                    isFavorite = true
                    setStatusFavorite(isFavorite)
                }
            }
        }

        //fav item
        val username = user.toString()

        setStatusFavorite(isFavorite)
        binding.fabFav.setOnClickListener {
            if (!isFavorite) {
                val values = ContentValues()
                values.put(_ID, userid)
                values.put(USERNAME, username)
                values.put(AVATAR_URL, avatar)
                values.put(FULLNAME, fullName)
                values.put(FOLLOWERS, followers)
                values.put(FOLLOWING, following)
                values.put(LOCATION, location)
                values.put(COMPANY, company)
                contentResolver.insert(CONTENT_URI, values)

                isFavorite = !isFavorite
                setStatusFavorite(isFavorite)
                Toast.makeText(this, getString(R.string.add_fav), Toast.LENGTH_SHORT).show()
            } else {
                uriWithId = Uri.parse("$CONTENT_URI/$userid")
                contentResolver.delete(uriWithId, null, null)

                isFavorite = !isFavorite
                setStatusFavorite(isFavorite)
                Toast.makeText(this, getString(R.string.remove_fav), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setStatusFavorite(state: Boolean) {
        if (state) {
            binding.fabFav.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.fabFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
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