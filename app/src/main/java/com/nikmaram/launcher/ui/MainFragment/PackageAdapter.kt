package com.nikmaram.launcher.ui.MainFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.nikmaram.launcher.databinding.PackageItemBinding
import com.nikmaram.launcher.model.PackageModel


class PackageAdapter(val data: List<PackageModel>,val callback: (String)->Unit) : RecyclerView.Adapter<PackageAdapter.viewHolder>() {


    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder =
            viewHolder(PackageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = data[position]
        holder.binding.data = item
        holder.itemView.setOnClickListener {
            callback(item.packageName)
        }
    }
    class viewHolder(val binding: PackageItemBinding) : RecyclerView.ViewHolder(binding.root)

}




