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

package com.iconsnotfound.ui.utils

import android.content.Context
import com.iconsnotfound.ui.R

class AlertDialogTexts(private val context: Context, val template: Template? = null)  {
    enum class Template {Save, Update}
    lateinit var adTitle: String
    lateinit var adMessage: String
    lateinit var adYesButton: String
    lateinit var adNoButton: String
    lateinit var adNeutral: String

    init {
        template?.let {
            adTitle = getTitle()
            adMessage = getMessage()
            adYesButton = getYesButtonText()
            adNoButton = getNoButtonText()
            adNeutral = getNeutralButtonText()
        }
    }

    private fun getTitle(): String {
        return when(template) {
            Template.Save -> context.resources.getString(R.string.unsaved_changes_title)
            Template.Update -> context.resources.getString(R.string.update_changes_title)
            else -> ""
        }
    }

    private fun getMessage(): String {
        return when(template) {
            Template.Save -> context.resources.getString(R.string.unsaved_changes_message)
            Template.Update -> context.resources.getString(R.string.update_changes_message)
            else -> ""
        }
    }

    private fun getYesButtonText(): String {
        return when(template) {
            Template.Save -> context.resources.getString(R.string.save)
            Template.Update -> context.resources.getString(R.string.update)
            else -> ""
        }
    }

    private fun getNoButtonText(): String {
        return when(template) {
            Template.Save -> context.resources.getString(R.string.do_not_save)
            Template.Update -> context.resources.getString(R.string.do_not_update)
            else -> ""
        }
    }

    private fun getNeutralButtonText(): String {
        return when(template) {
            Template.Save, Template.Update -> context.resources.getString(R.string.cancel)
            else -> ""
        }
    }


}