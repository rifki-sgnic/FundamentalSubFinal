package com.dicoding.picodiploma.fundamentalsubfinal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.fundamentalsubfinal.adapter.FollowerAdapter
import com.dicoding.picodiploma.fundamentalsubfinal.databinding.FragmentFollowersBinding
import com.dicoding.picodiploma.fundamentalsubfinal.model.FollowItems
import com.dicoding.picodiploma.fundamentalsubfinal.viewmodel.FollowerViewModel

class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    private lateinit var adapter: FollowerAdapter
    private lateinit var followerViewModel: FollowerViewModel
    private val list = ArrayList<FollowItems>()

    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME)

        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowerViewModel::class.java
        )

        showRecyclerView()
        username?.let { followerViewModel.setListFollower(it) }
        followerViewModel.getListFollower().observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.setData(it)
                showLoading(false)
                showEmpty(false)
                if (it.isEmpty()) showEmpty(true)
            }
        })
    }

    private fun showRecyclerView() {
        binding.rvFollowers.layoutManager = LinearLayoutManager(context)
        binding.rvFollowers.setHasFixedSize(true)
        adapter = FollowerAdapter(list)
        binding.rvFollowers.adapter = adapter
        adapter.notifyDataSetChanged()

        showLoading(true)
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

