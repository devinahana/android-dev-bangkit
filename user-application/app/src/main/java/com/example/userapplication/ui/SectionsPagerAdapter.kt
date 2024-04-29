package com.example.userapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var userName: String = ""

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowersFollowingFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowersFollowingFragment.ARG_SECTION_NUMBER, position + 1)
            putString(FollowersFollowingFragment.USERNAME, userName)
        }
        return fragment
    }
}