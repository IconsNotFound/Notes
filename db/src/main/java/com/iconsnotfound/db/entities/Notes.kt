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

package com.iconsnotfound.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iconsnotfound.db.utils.DbConstants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = DbConstants.TABLE_NOTES)
class Notes (
    @ColumnInfo(DbConstants.Notes.TEXT) var text: String,
    @ColumnInfo(DbConstants.Notes.LAST_MODIFIED_TIME) var lastModifiedTime: Long?,
    @ColumnInfo(DbConstants.Notes.CREATE_TIME) var createTime: Long?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
): Parcelable