package com.example.userapplication.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapplication.data.remote.response.UserResponse
import com.example.userapplication.databinding.FragmentFollowersFollowingBinding
import com.example.userapplication.views.ListUserAdapter
import com.example.userapplication.utils.ViewModelFactory

class FollowersFollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowersFollowingBinding

    private lateinit var viewModel: FollowersFollowingViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowersFollowingBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val username = arguments?.getString(USERNAME)
        val factory = username?.let { ViewModelFactory(username = it) }
        viewModel = ViewModelProvider(this, factory!!).get(FollowersFollowingViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        if (index == 1) {
            viewModel.listFollowers.observe(viewLifecycleOwner) {users ->
                setListUserData(users)
            }
            viewModel.isLoadingFollowers.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        } else if (index == 2) {
            viewModel.listFollowing.observe(viewLifecycleOwner) {users ->
                setListUserData(users)
            }
            viewModel.isLoadingFollowing.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }
    }

    private fun setListUserData(users: List<UserResponse>) {
        val adapter = ListUserAdapter<UserResponse>()
        adapter.submitList(users)
        binding.recyclerView.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val USERNAME = "username"
    }

}
