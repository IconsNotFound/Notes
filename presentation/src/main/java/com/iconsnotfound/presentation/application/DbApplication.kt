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

package com.iconsnotfound.presentation.application

import androidx.multidex.MultiDexApplication
import com.iconsnotfound.data.repositories.NotesRepository
import com.iconsnotfound.db.database.NotesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class DbApplication: MultiDexApplication() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val db by lazy { NotesDatabase.getDb(this, applicationScope) }
    val repository by lazy { NotesRepository(db.notesDao()) }
}