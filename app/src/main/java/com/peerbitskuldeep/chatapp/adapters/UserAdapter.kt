package com.peerbitskuldeep.chatapp.adapters

import android.content.Context

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.peerbitskuldeep.chatapp.R
import com.peerbitskuldeep.chatapp.model.User

class UserAdapter(databaseQuery: DatabaseReference, var context: Context)
    :FirebaseRecyclerAdapter<User, UserAdapter.ViewHolder>(
    User::class.java,
    R.layout.users_row,
    UserAdapter.ViewHolder::class.java,
    databaseQuery
    )
{


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: User) {

    }

}