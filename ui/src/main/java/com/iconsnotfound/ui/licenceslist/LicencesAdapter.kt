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

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.PagingDataAdapter
import com.iconsnotfound.data.utils.LicencesItem

class LicencesAdapter(
    private val lifecycleScope: LifecycleCoroutineScope,
    private val listener: onItemClickListener
): PagingDataAdapter<LicencesItem, LicencesViewHolder>(LicencesComparator()) {
    interface onItemClickListener {
        fun onLicencesItemClick(view: View, position: Int)
    }

    override fun onBindViewHolder(holder: LicencesViewHolder, position: Int) {
        getItem(position)?.let {
            val current: LicencesItem = it
            holder.bind(current.id, current.title, current.subTitle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LicencesViewHolder {
        return LicencesViewHolder.create(parent, lifecycleScope, listener)
    }

    fun getItemAt(position: Int) = getItem(position)
}