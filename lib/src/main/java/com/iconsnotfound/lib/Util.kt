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

package com.iconsnotfound.lib

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class Util {
    companion object{
        fun convertToDateTime(timestamp: Long): String {
            val clock = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date(timestamp))
                .replace("AM", "am").replace("PM", "pm")

            val currentTime = System.currentTimeMillis()
            val difference = currentTime - (timestamp)

            return when {
                difference < TimeUnit.MINUTES.toMillis(1) ||
                        difference < TimeUnit.HOURS.toMillis(1) ||
                        difference < TimeUnit.DAYS.toMillis(1)-> clock
                else -> {
                    val sdf = SimpleDateFormat("dd-MMM-yyyy, hh:mm:ss a", Locale.getDefault())
                    val date = Date(timestamp)
                    sdf.format(date).replace("AM", "am").replace("PM", "pm")
                }
            }
        }

        fun isNumber(input: String?): Boolean {
            return !input.isNullOrEmpty() && input.all { it.isDigit() }
        }

        fun isGreaterThanZero(input: String?): Boolean {
            return isNumber(input) && input.toString().toInt() > 0
        }
    }
}