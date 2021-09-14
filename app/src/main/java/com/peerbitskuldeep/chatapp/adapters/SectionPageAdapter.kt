package com.peerbitskuldeep.chatapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.peerbitskuldeep.chatapp.fragments.ChatsFragment
import com.peerbitskuldeep.chatapp.fragments.UsersFragment

class SectionPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position)
        {
            0 -> return UsersFragment()
            1 -> return ChatsFragment()
        }
        return null!!
    }

    override fun getPageTitle(position: Int): CharSequence? {

        when(position)
        {
            0 -> return "Users"
            1 -> return "Chats"
        }

        return null!!
    }
}