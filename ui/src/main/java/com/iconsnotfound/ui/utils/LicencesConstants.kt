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
import com.iconsnotfound.data.utils.LicencesItem
import com.iconsnotfound.ui.R

object LicencesConstants {
    const val NOTES = 1
    const val ANDROIDX = 2
    const val MATERIAL_DESIGN = 3
    const val INTUIT_SSP = 4
    const val INTUIT_SDP = 5
    fun getList(context: Context): List<LicencesItem> {
        return listOf(
            LicencesItem(
                NOTES,
                context.resources.getString(R.string.play_store_app_name),
                context.resources.getString(R.string.notes_licence_sub_title)
            ),
            LicencesItem(
                ANDROIDX,
                context.resources.getString(R.string.androidx_libraries),
                context.resources.getString(R.string.apache_2_licence)
            ),
            LicencesItem(
                MATERIAL_DESIGN,
                context.resources.getString(R.string.material_design),
                context.resources.getString(R.string.apache_2_licence)
            ),
            LicencesItem(
                INTUIT_SDP,
                context.resources.getString(R.string.intuit_sdp),
                context.resources.getString(R.string.mit_licence)
            ),
            LicencesItem(
                INTUIT_SSP,
                context.resources.getString(R.string.intuit_ssp),
                context.resources.getString(R.string.mit_licence)
            ),
        )
    }
}