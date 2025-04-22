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

package com.iconsnotfound.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iconsnotfound.data.repositories.NotesRepository
import com.iconsnotfound.db.entities.Notes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NotesViewModel(private val notesRepository: NotesRepository): ViewModel() {
    var countAllNotes = 0
    var justModified = false

    val allNotes: Flow<PagingData<Notes>> = notesRepository.allNotes().cachedIn(viewModelScope)
    val totalNotes: LiveData<Int> = notesRepository.totalNotes.asLiveData()
    val getLatestNote: LiveData<Notes> = notesRepository.getLatestNote.asLiveData()

    fun insertNote(notes: Notes) = viewModelScope.launch { notesRepository.insertNote(notes) }
    fun deleteNote(id: Int) = viewModelScope.launch { notesRepository.deleteNote(id) }
    fun deleteAllNotes() = viewModelScope.launch { notesRepository.deleteAllNotes() }
    fun updateNote(notes: Notes) = viewModelScope.launch { notesRepository.updateNote(notes) }
}