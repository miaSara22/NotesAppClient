package com.miaekebom.mynotesapp.view.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miaekebom.mynotesapp.R
import com.miaekebom.mynotesapp.model.data.Note
import kotlinx.android.synthetic.main.row_note_item.view.*

class NoteAdapter(private val dataList: MutableList<Note>,
                  private val onNoteTitleClick: (Note) -> Unit,
                  private val onNoteRemoveClick: (Note) -> Unit,
                  private val onNoteEditNameClick: (Note) -> Unit

                   ):
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

        class NoteViewHolder(view: View): RecyclerView.ViewHolder(view) {

            val noteTitle: TextView
            val noteDelB: ImageButton
            val noteEditB: ImageButton

            init {

                noteTitle = view.TV_note_name
                noteDelB = view.IB_note_del
                noteEditB = view.IB_edit_note_name
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = View.inflate(parent.context, R.layout.row_note_item, null)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val note = dataList[position]

        holder.noteTitle.text = note.title
        holder.noteTitle.setOnClickListener {
            onNoteTitleClick.invoke(note)
        }

        holder.noteDelB.setOnClickListener {
            onNoteRemoveClick(note)
        }

        holder.noteEditB.setOnClickListener {
            onNoteEditNameClick(note)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}