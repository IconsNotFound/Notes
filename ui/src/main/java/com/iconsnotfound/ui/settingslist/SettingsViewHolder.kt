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

package com.iconsnotfound.ui.settingslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.iconsnotfound.ui.R
import com.iconsnotfound.ui.utils.Util
import com.google.android.material.card.MaterialCardView

class SettingsViewHolder(
    view: View,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val listener: SettingsAdapter.OnItemClickListener
): RecyclerView.ViewHolder(view) {
    private var id: Int = -1
    private val title = view.findViewById<TextView>(R.id.settings_title)
    private val subTitle = view.findViewById<TextView>(R.id.settings_sub_title)
    private val icon = view.findViewById<ImageView>(R.id.settings_logo)
    private val endIcon = view.findViewById<ImageView>(R.id.settings_end_icon)
    private val cardView = view.findViewById<MaterialCardView>(R.id.settings_card_view)

    private val youtube = view.findViewById<Button>(R.id.youtube)
    private val github = view.findViewById<Button>(R.id.github)

    private val onClickListener = View.OnClickListener {
        if(bindingAdapterPosition != RecyclerView.NO_POSITION) {
            listener.onSettingsItemClick(it, bindingAdapterPosition)
        }
    }

    fun bind(id: Int, title: String, icon:Int, subTitle: String?=null, endIcon: Int? = null) {
        this.id = id
        this.title.text = title
        Util.startTextScroll(this.title, lifecycleScope)

        if(subTitle == null) this.subTitle.visibility = View.INVISIBLE

        this.subTitle.text = subTitle
        this.subTitle.isEnabled = true
        Util.startTextScroll(this.subTitle, lifecycleScope)

        this.icon.setImageResource(icon)
        if(endIcon == null) this.endIcon.visibility = View.GONE
        else {
            this.endIcon.visibility = View.VISIBLE
            this.endIcon.setImageResource(endIcon)
        }

        init()
    }

    fun bindFooter() {
        youtube.setOnClickListener(onClickListener)
        github.setOnClickListener(onClickListener)
    }

    private fun init() {
        cardView.setOnClickListener(onClickListener)

        if((this.id%4==0 || this.id%4 == 1)) {
            cardView.let {
                it.strokeWidth = 0
                it.strokeColor = ContextCompat.getColor(it.context, R.color.md_theme_surface)
                it.setCardBackgroundColor(ContextCompat.getColor(it.context, R.color.md_theme_secondaryContainer))
            }
        }
        else {
            cardView.let {
                it.strokeColor = ContextCompat.getColor(it.context, R.color.md_theme_outlineVariant)
                it.setCardBackgroundColor(ContextCompat.getColor(it.context, R.color.md_theme_surface))
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, lifecycleScope: LifecycleCoroutineScope, listener: SettingsAdapter.OnItemClickListener, viewType: Int): SettingsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(if(viewType==1)R.layout.settings_item else R.layout.settings_item_footer, parent, false)
            return SettingsViewHolder(view, lifecycleScope, listener)
        }
    }

}