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

package com.iconsnotfound.data.repositories

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.iconsnotfound.data.utils.RepoConstants
import com.iconsnotfound.db.dao.NotesDao
import com.iconsnotfound.db.entities.Notes
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val notesDao: NotesDao,
                      private val pageSize:Int = RepoConstants.PAGE_SIZE,
                      private val enablePlaceholder: Boolean = false) {
    
    fun allNotes(): Flow<PagingData<Notes>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = enablePlaceholder),
            pagingSourceFactory = { notesDao.getNotes() }
        ).flow
    }
    
    val totalNotes: Flow<Int> = notesDao.getTotalNotes()
    val getLatestNote: Flow<Notes> = notesDao.getLatestNote()
    
    @WorkerThread
    suspend fun insertNote(notes: Notes) = notesDao.insertNote(notes)

    @WorkerThread
    suspend fun deleteNote(id: Int) = notesDao.deleteNote(id)

    @WorkerThread
    suspend fun deleteAllNotes() = notesDao.deleteAllNotes()

    @WorkerThread
    suspend fun updateNote(notes: Notes) = notesDao.updateNote(notes)

}