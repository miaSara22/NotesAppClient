package com.miaekebom.mynotesapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.miaekebom.mynotesapp.databinding.RowListItemBinding
import com.miaekebom.mynotesapp.model.data.List

class ListAdapter(
    private var dataList: MutableList<List>,
    private val onListTitleClick: (List) -> Unit,
    private val onListRemoveClick: (List) -> Unit,
    private val onListEditNameClick: (List) -> Unit) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: RowListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(list: List) {

            binding.TVListName.text = list.title
            binding.TVListName.setOnClickListener {
                onListTitleClick.invoke(list)
            }
            binding.IBListDel.setOnClickListener {
                onListRemoveClick(list)
            }
            binding.IBEditListName.setOnClickListener {
                onListEditNameClick(list)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            RowListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val list = dataList[position]
        holder.bind(list)
    }

    override fun getItemCount(): Int = dataList.size

    fun searchItem(result: ArrayList<List>) {
        dataList = result
        notifyDataSetChanged()
    }

    fun updateItem(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, item: List) {
        adapter.notifyItemChanged(item.id)
    }

    fun deleteItem(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, item: List) {
        adapter.notifyItemRemoved(item.id)
    }

    fun addItem(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, item: List) {
        adapter.notifyItemInserted(item.id)
    }
}