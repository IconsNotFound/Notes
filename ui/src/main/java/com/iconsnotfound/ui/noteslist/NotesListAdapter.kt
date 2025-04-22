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

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iconsnotfound.db.entities.Notes

class NotesListAdapter(
    private val listener: OnItemClickListener
): PagingDataAdapter<Notes, NotesListViewHolder>(NotesListComparator()) {
    private val removedItems = mutableListOf<Notes>()

    interface OnItemClickListener {
        fun onNoteItemClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesListViewHolder {
        return NotesListViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: NotesListViewHolder, position: Int) {
        val item = getItem(position)
        if(item != null && removedItems.contains(item)) {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0,0)
        }
        else {
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        getItem(position)?.let {
            val current: Notes = it
            holder.bind(current.text, current.id, current.lastModifiedTime)
        }
    }

    fun getItemAt(position: Int) = getItem(position)

    fun removeItem(item: Notes) {
        removedItems.add(item)
        val position = snapshot().indexOf(item)
        if(position >= 0){
            notifyItemChanged(position)
        }
    }

    fun clearCache() {
        removedItems.clear()
    }

}