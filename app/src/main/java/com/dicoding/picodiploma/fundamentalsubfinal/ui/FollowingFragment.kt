package com.dicoding.picodiploma.fundamentalsubfinal.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.fundamentalsubfinal.adapter.FollowingAdapter
import com.dicoding.picodiploma.fundamentalsubfinal.databinding.FragmentFollowingBinding
import com.dicoding.picodiploma.fundamentalsubfinal.model.FollowItems
import com.dicoding.picodiploma.fundamentalsubfinal.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var adapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel
    private val list = ArrayList<FollowItems>()

    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowingFragment {
            val fragment = FollowingFragment()
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
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME)

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowingViewModel::class.java
        )

        showRecyclerView()

        username?.let { followingViewModel.setListFollowing(it) }
        followingViewModel.getListFollowing().observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.setData(it)
                showLoading(false)
                showEmpty(false)
                if (it.isEmpty()) showEmpty(true)
            }
        })
    }

    private fun showRecyclerView() {
        binding.rvFollowing.layoutManager = LinearLayoutManager(context)
        binding.rvFollowing.setHasFixedSize(true)
        adapter = FollowingAdapter(list)
        binding.rvFollowing.adapter = adapter
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