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

package com.iconsnotfound.data.sharedpreferences

import com.iconsnotfound.data.sharedpreferences.SharedPreferencesConstants.AppTheme
import android.content.Context
import androidx.core.content.edit

class NotesSharedPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        SharedPreferencesConstants.NOTES_SHARED_PREFS_NAME,
        Context.MODE_PRIVATE
    )

    fun setInt(key: String, value: Int) {
        sharedPreferences.edit { putInt(key, value) }
    }

    fun getInt(key: String, default: Int = SharedPreferencesConstants.DEFAULT_VALUE): Int {
        return sharedPreferences.getInt(key, default)
    }

    fun setEnum(key: String, value: AppTheme) {
        sharedPreferences.edit { putString(key, value.name) }
    }

    fun getEnum(key: String, default: AppTheme = AppTheme.THEME_SYSTEM_DEFAULT): AppTheme {
        return AppTheme.valueOf(sharedPreferences.getString(key, default.name) ?: default.name)
    }

    fun clearAll() {
        sharedPreferences.edit { clear() }
    }

    fun removeKey(key: String) {
        sharedPreferences.edit { remove(key) }
    }
}