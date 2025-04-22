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

package com.iconsnotfound.ui.licenceslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.iconsnotfound.ui.R

class LicencesViewHolder(
    view: View,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val listener: LicencesAdapter.onItemClickListener
): RecyclerView.ViewHolder(view) {
    private var id: Int = -1
    private val title = view.findViewById<TextView>(R.id.licences_title)
    private val subTitle = view.findViewById<TextView>(R.id.licences_sub_title)
    private val cardView = view.findViewById<MaterialCardView>(R.id.licences_card_view)

    private val onClickListener = View.OnClickListener {
        if(bindingAdapterPosition != RecyclerView.NO_POSITION) {
            listener.onLicencesItemClick(it, bindingAdapterPosition)
        }
    }

    fun bind(id: Int, title: String, subTitle: String?=null) {
        this.id = id
        this.title.text = title
        this.subTitle.text = subTitle

        if(subTitle == null) this.subTitle.visibility = View.GONE

        init()
    }

    private fun init() {
        cardView.setOnClickListener(onClickListener)
    }

    companion object {
        fun create(parent: ViewGroup, lifecycleScope: LifecycleCoroutineScope, listener: LicencesAdapter.onItemClickListener): LicencesViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.licences_item, parent, false)
            return LicencesViewHolder(view, lifecycleScope, listener)
        }
    }
}