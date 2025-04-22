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

package com.iconsnotfound.db.utils

object DbConstants {
    const val DATABASE_NAME = "notes_db"
    const val DATABASE_VERSION = 1

    const val TABLE_NOTES = "notes"

    object Notes {
        const val TEXT = "text"
        const val LAST_MODIFIED_TIME = "last_modified_time"
        const val CREATE_TIME = "create_time"
        const val TIMESTAMP = "timestamp"   // removed in ver. 2
    }
}