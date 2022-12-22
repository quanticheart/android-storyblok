package com.quanticheart.storyblok.ui.email

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.quanticheart.storyblok.conn.model.Links
import com.quanticheart.storyblok.databinding.ItemEmailBinding

//
// Created by Jonn Alves on 20/12/22.
//
class EmailAdapter(private val recyclerView: RecyclerView) :
    RecyclerView.Adapter<EmailAdapter.EmailHolder>() {
    private var glide: RequestManager = Glide.with(recyclerView)
    private var list: List<Links> = arrayListOf()
    private var callback: ((String) -> Unit)? = null

    init {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(recyclerView.context)
            adapter = this@EmailAdapter
        }
    }


    inner class EmailHolder(private val view: ItemEmailBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun showView(link: Links) {
            if (!link.img.isNullOrEmpty()) {
                glide.load(link.img).into(view.img)
                view.img.visibility = View.VISIBLE
            } else {
                view.img.visibility = View.GONE
            }
            view.text.text = link.title
            view.root.setOnClickListener {
                callback?.let { it1 -> it1(link.id) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EmailHolder(ItemEmailBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: EmailHolder, position: Int) =
        holder.showView(list.get(position))

    override fun getItemCount(): Int = list.size

    fun setData(list: List<Links>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setOnClickListener(callback: (String) -> Unit) {
        this.callback = callback
    }
}