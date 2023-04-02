package com.miaekebom.mynotesapp.view.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miaekebom.mynotesapp.R
import com.miaekebom.mynotesapp.model.data.List
import kotlinx.android.synthetic.main.row_list_item.view.*

class ListAdapter(private var dataList: MutableList<com.miaekebom.mynotesapp.model.data.List>,
                  private val onListTitleClick: (com.miaekebom.mynotesapp.model.data.List) -> Unit,
                  private val onListRemoveClick: (com.miaekebom.mynotesapp.model.data.List) -> Unit,
                  private val onListEditNameClick: (List) -> Unit
                   ):
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

        class ListViewHolder(view: View): RecyclerView.ViewHolder(view) {

            val listTitle: TextView
            val listDelB: ImageButton
            val listEditB: ImageButton

            init {

                listTitle = view.TV_list_name
                listDelB = view.IB_list_del
                listEditB = view.IB_edit_list_name

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = View.inflate(parent.context, R.layout.row_list_item, null)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val list = dataList[position]

        holder.listTitle.text = list.listName
        holder.listTitle.setOnClickListener {
            onListTitleClick.invoke(list)
        }

        holder.listDelB.setOnClickListener {
            onListRemoveClick(list)
        }

        holder.listEditB.setOnClickListener {
            onListEditNameClick(list)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchItem(result: ArrayList<com.miaekebom.mynotesapp.model.data.List>){
        dataList = result
        notifyDataSetChanged()
    }

//    fun updateItem(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, item: List){
//        adapter.notifyItemChanged(item)
//
//    }
//
//    fun deleteItem(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, item: List){
//        adapter.notifyItemRemoved(item.listId)
//    }
//
//    fun addItem(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, item: List){
//        adapter.notifyItemInserted(item.listId)
//    }
}