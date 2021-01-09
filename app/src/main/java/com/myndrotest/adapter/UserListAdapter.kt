package com.myndrotest.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.myndrotest.R
import com.myndrotest.app.MyndroApp
import com.myndrotest.model.UserModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_progress_footer.view.*
import kotlinx.android.synthetic.main.list_item_user.view.*

class UserListAdapter(var context: Context, var userList: ArrayList<UserModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var showProgressView = true
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_ITEM) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false)
            return UserListHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_progress_footer, parent, false)
            ProgressViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserListHolder -> {
                holder.onBind(userList[position])
            }
            else -> {
                (holder as ProgressViewHolder).onBind()
            }
        }
    }

    fun hideProgressBar() {
        showProgressView = false
        notifyDataSetChanged()
    }

    fun showProgressView() {
        showProgressView = true
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < userList.size) VIEW_ITEM else VIEW_PROG
    }

    override fun getItemCount(): Int = userList.size

    inner class UserListHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(model: UserModel) {
            if (model.image.isNotEmpty()) {
                Picasso.get().load(model.image).into(itemView.imageViewUser, object : Callback {
                    override fun onSuccess() {

                    }

                    override fun onError(e: Exception?) {
                        itemView.imageViewUser.setImageResource(R.drawable.ic_user)
                    }
                })
            } else {
                itemView.imageViewUser.setImageResource(R.drawable.ic_user)
            }

            itemView.textViewUserName.text = "${model.name}"

            val layoutManager = GridLayoutManager(context, 2)
            itemView.recyclerViewImages.layoutManager = layoutManager

            val adapter = UserImageAdapter(model.items.toCollection(ArrayList()))
            itemView.recyclerViewImages.adapter = adapter

            if (model.items.size % 2 != 0) {
                layoutManager.spanSizeLookup = object : SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == 0) 2 else 1
                    }
                }
            }
        }
    }

    inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {
            if (!showProgressView)
                itemView.progressBar.visibility = View.GONE
            else
                itemView.progressBar.visibility = View.VISIBLE
        }
    }
}