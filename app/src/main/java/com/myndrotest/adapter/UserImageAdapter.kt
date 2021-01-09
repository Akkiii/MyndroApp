package com.myndrotest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myndrotest.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_images.view.*
import java.lang.Exception

class UserImageAdapter(var imageItem: ArrayList<String>) :
    RecyclerView.Adapter<UserImageAdapter.UserImageHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserImageHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_images, parent, false)
        return UserImageHolder(view)
    }

    override fun onBindViewHolder(holder: UserImageHolder, position: Int) {
        val model = imageItem[position]
        if (imageItem.size % 2 != 0) {
            if (position == 0) {
                holder.itemView.imageOddItems.visibility = View.VISIBLE
                holder.itemView.imageItems.visibility = View.GONE
                Picasso.get().load(model).into(holder.itemView.imageOddItems, object : Callback {
                    override fun onSuccess() {

                    }

                    override fun onError(e: Exception?) {
                        holder.itemView.progressBar.visibility = View.GONE
                    }

                })
            } else {
                holder.itemView.imageOddItems.visibility = View.GONE
                holder.itemView.imageItems.visibility = View.VISIBLE

                Picasso.get().load(model).into(holder.itemView.imageItems, object : Callback {
                    override fun onSuccess() {

                    }

                    override fun onError(e: Exception?) {
                        holder.itemView.progressBar.visibility = View.GONE
                    }
                })
            }
        } else {
            holder.itemView.imageOddItems.visibility = View.GONE
            holder.itemView.imageItems.visibility = View.VISIBLE

            Picasso.get().load(model).into(holder.itemView.imageItems, object : Callback {
                override fun onSuccess() {

                }

                override fun onError(e: Exception?) {
                    holder.itemView.progressBar.visibility = View.GONE
                }
            })
        }
    }

    override fun getItemCount(): Int = imageItem.size

    inner class UserImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(model: String) {
            if (model.isNotEmpty()) {
                Picasso.get().load(model).into(itemView.imageItems, object : Callback {
                    override fun onSuccess() {

                    }

                    override fun onError(e: Exception?) {
                        itemView.progressBar.visibility = View.GONE
                    }

                })
            } else {
                itemView.progressBar.visibility = View.GONE
            }
        }
    }
}