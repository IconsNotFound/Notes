/*
 * Notes - Fast &amp; Simple
 * Copyright (C) 2025 IconsNotFound
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.iconsnotfound.ui.noteslist

import android.graphics.text.LineBreaker
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iconsnotfound.lib.Util
import com.iconsnotfound.ui.R
import com.google.android.material.card.MaterialCardView

class NotesListViewHolder(
    view: View,
    private val listener: NotesListAdapter.OnItemClickListener
): RecyclerView.ViewHolder(view) {
    private val text = view.findViewById<TextView>(R.id.note_text)
    private var id: Int = -1
    private val dateTime = view.findViewById<TextView>(R.id.date_time)

    fun bind(text: String?, id: Int, timestamp: Long?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.text.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }
        this.text.text = text
        this.id = id
        if(timestamp != null) {
            dateTime.text = Util.convertToDateTime(timestamp)
        }
        else dateTime.text = "n/a"

        init()
    }

    private fun init() {
        fun initButtons(view: View) {
            if(bindingAdapterPosition != RecyclerView.NO_POSITION) listener.onNoteItemClick(view, bindingAdapterPosition)
        }

        val array = arrayOf(R.id.delete_note, R.id.share_note, R.id.copy_note)
        array.forEach { it2 ->
            itemView.findViewById<Button>(it2).setOnClickListener{ initButtons(it) }
        }

        itemView.findViewById<MaterialCardView>(R.id.note_card_view).setOnClickListener{ initButtons(it) }
    }

    companion object {
        fun create(parent: ViewGroup, listener: NotesListAdapter.OnItemClickListener): NotesListViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.notelist_item, parent, false)
            return NotesListViewHolder(view, listener)
        }
    }
}