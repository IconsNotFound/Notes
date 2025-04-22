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
import com.iconsnotfound.data.sharedpreferences.NotesSharedPreferences
import com.iconsnotfound.data.sharedpreferences.SharedPreferencesConstants
import com.iconsnotfound.data.sharedpreferences.SharedPreferencesConstants.AppTheme
import com.iconsnotfound.data.utils.SettingsItem
import com.iconsnotfound.ui.R

object SettingsConstants {
    const val FOOTER = -1
    const val ITEM_THEME = 1
    const val DELETE_ALL = 2
    const val INFO = 3
    const val PRIVACY_POLICY = 4
    const val LICENCE = 5
    const val DONATE = 6
    fun getSettingsList(context: Context, sharedPreferences: NotesSharedPreferences): List<SettingsItem> {
        val themeSubtitle = when(sharedPreferences.getEnum(SharedPreferencesConstants.THEME)) {
            AppTheme.THEME_SYSTEM_DEFAULT -> context.resources.getString(R.string.theme_default)
            AppTheme.THEME_LIGHT -> context.resources.getString(R.string.theme_light)
            else -> context.resources.getString(R.string.theme_dark)
        }
        return listOf(
            SettingsItem(
                ITEM_THEME,
                context.resources.getString(R.string.theme),
                themeSubtitle,
                R.drawable.icon_theme,
                null
            ),
            SettingsItem(
                DELETE_ALL,
                context.resources.getString(R.string.delete_all),
                context.resources.getString(R.string.delete_all_sub),
                R.drawable.icon_delete_all_notes,
                null
            ),
            SettingsItem(
                INFO,
                context.resources.getString(R.string.app_info),
                Util.getAppVersion(context),
                R.drawable.icon_info,
                null
            ),
            SettingsItem(
                PRIVACY_POLICY,
                context.resources.getString(R.string.privacy_policy),
                context.resources.getString(R.string.privacy_policy_sub),
                R.drawable.icon_privacy_policy,
                R.drawable.icon_open_link
            ),
            SettingsItem(
                LICENCE,
                context.resources.getString(R.string.licences),
                context.resources.getString(R.string.open_source_licences_description),
                R.drawable.icon_licence,
                R.drawable.icon_forward
            ),
            SettingsItem(
                DONATE,
                context.resources.getString(R.string.donate),
                context.resources.getString(R.string.donate_subtitle),
                R.drawable.icon_donate,
                R.drawable.icon_patreon
            ),

            // always put footer at the end
            SettingsItem(
                FOOTER,
                context.resources.getString(R.string.brand_name),
                null,
                R.drawable.icon_app_logo_demo,
                null
            ),
        )
    }
}