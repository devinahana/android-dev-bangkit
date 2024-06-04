package com.bangkit.userstory.utils

import com.bangkit.userstory.data.remote.response.Story

object DataDummy {
    fun generateDummyStory(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                id = i.toString(),
                photoUrl = "https://i.ibb.co.com/1z68FvJ/woman-2-1.png",
                name = "author $i",
                description = "description $i"
            )
            items.add(story)
        }
        return items
    }

    fun generateToken(): String {
        return "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVpSaXJaWlhMbEYtTS0zY1EiLCJpYXQiOjE3MTc1MjY1MTJ9.xNibBc8TDBZZIf9X3PT8ngS8lhUY3D7t4hx_2ryHte8"
    }
}