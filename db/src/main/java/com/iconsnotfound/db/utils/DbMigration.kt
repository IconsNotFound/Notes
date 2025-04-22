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

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// added create_time column, renamed timestamp to last_modified_time
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE ${DbConstants.TABLE_NOTES}_temp (
                ${DbConstants.Notes.TEXT} TEXT NOT NULL,
                ${DbConstants.Notes.LAST_MODIFIED_TIME} INTEGER,
                ${DbConstants.Notes.CREATE_TIME} INTEGER,
                id INTEGER PRIMARY KEY NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO ${DbConstants.TABLE_NOTES}_temp (${DbConstants.Notes.TEXT}, ${DbConstants.Notes.LAST_MODIFIED_TIME}, ${DbConstants.Notes.CREATE_TIME})
            SELECT ${DbConstants.Notes.TEXT}, ${DbConstants.Notes.TIMESTAMP}, ${DbConstants.Notes.TIMESTAMP} FROM ${DbConstants.TABLE_NOTES}
        """.trimIndent())

        db.execSQL("DROP TABLE ${DbConstants.TABLE_NOTES}")

        db.execSQL("ALTER TABLE ${DbConstants.TABLE_NOTES}_temp RENAME TO ${DbConstants.TABLE_NOTES}")
    }
}

val ALL_MIGRATIONS = arrayOf(MIGRATION_1_2)