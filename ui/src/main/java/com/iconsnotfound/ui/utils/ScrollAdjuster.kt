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

package com.iconsnotfound.ui.utils

import android.graphics.Rect
import android.view.ViewTreeObserver
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout

class ScrollAdjuster(private val scrollView: ScrollView) {
    private fun dynamicEditTextHeight(){
        val rect = Rect()
        scrollView.getWindowVisibleDisplayFrame(rect)

        val screenHeight = scrollView.rootView.height
        val keypadHeight = screenHeight - rect.bottom

        val layoutParams = scrollView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = if(keypadHeight > screenHeight * 0.15) rect.bottom - scrollView.top else 0
        scrollView.layoutParams = layoutParams
    }
    fun createGlobalLayoutListener(): ViewTreeObserver.OnGlobalLayoutListener{
        return ViewTreeObserver.OnGlobalLayoutListener {
            scrollView.post {
                dynamicEditTextHeight()
            }
        }
    }
}