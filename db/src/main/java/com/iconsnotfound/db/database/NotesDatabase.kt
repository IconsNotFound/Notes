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

package com.iconsnotfound.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.iconsnotfound.db.dao.NotesDao
import com.iconsnotfound.db.entities.Notes
import com.iconsnotfound.db.utils.ALL_MIGRATIONS
import com.iconsnotfound.db.utils.DbConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Notes::class], version = 2, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getDb(context: Context, scope: CoroutineScope): NotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    DbConstants.DATABASE_NAME
                ).addCallback(NotesDatabaseCallback(scope))
                    .addMigrations(*ALL_MIGRATIONS)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

    private class NotesDatabaseCallback(private val scope: CoroutineScope): Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { scope.launch { populateDb(it.notesDao()) } }
        }

        suspend fun populateDb(notesDao: NotesDao) {
            notesDao.deleteAllNotes()
        }
    }
}