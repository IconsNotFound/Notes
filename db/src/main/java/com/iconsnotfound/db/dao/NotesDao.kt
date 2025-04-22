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

package com.iconsnotfound.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.iconsnotfound.db.entities.Notes
import com.iconsnotfound.db.utils.DbConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Query("SELECT * " +
            "FROM ${DbConstants.TABLE_NOTES} " +
            "ORDER BY ${DbConstants.Notes.LAST_MODIFIED_TIME} DESC")
    fun getNotes(): PagingSource<Int, Notes>

    @Query("SELECT COUNT(*) " +
            "FROM ${DbConstants.TABLE_NOTES}")
    fun getTotalNotes(): Flow<Int>

    @Query("SELECT * " +
            "FROM ${DbConstants.TABLE_NOTES} " +
            "ORDER BY ${DbConstants.Notes.LAST_MODIFIED_TIME} DESC LIMIT 1")
    fun getLatestNote(): Flow<Notes>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(notes: Notes)

    @Query("DELETE FROM ${DbConstants.TABLE_NOTES} " +
            "WHERE id = :id")
    suspend fun deleteNote(id: Int)

    @Query("DELETE FROM ${DbConstants.TABLE_NOTES}")
    suspend fun deleteAllNotes()

    @Update
    suspend fun updateNote(notes: Notes)
}