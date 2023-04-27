package com.miaekebom.mynotesapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.miaekebom.mynotesapp.databinding.RowNoteItemBinding
import com.miaekebom.mynotesapp.model.data.List
import com.miaekebom.mynotesapp.model.data.Note

class NoteAdapter(
    private var dataList: MutableList<Note>,
    private val onNoteTitleClick: (Note) -> Unit,
    private val onNoteRemoveClick: (Note) -> Unit,
    private val onNoteEditNameClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(private val binding: RowNoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.TVNoteName.setOnClickListener {
                onNoteTitleClick.invoke(dataList[adapterPosition])
            }
            binding.IBNoteDel.setOnClickListener {
                onNoteRemoveClick(dataList[adapterPosition])
            }
            binding.IBEditNoteName.setOnClickListener {
                onNoteEditNameClick(dataList[adapterPosition])
            }
        }

        fun bind(note: Note) {
            binding.TVNoteName.text = note.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = RowNoteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}