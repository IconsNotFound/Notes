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

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.iconsnotfound.db.entities.Notes
import com.iconsnotfound.presentation.viewmodels.NotesViewModel
import com.iconsnotfound.ui.noteslist.NotesListAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class RecyclerViewScroll(
    private val fragment: Fragment,
    private var recyclerView: RecyclerView,
    private val adapter: NotesListAdapter,
    private val viewModel: NotesViewModel,
    private var startScrolling: Boolean? = false,
    private var viewsToDisable: Array<View>? = null,
    private var totalColumns: Int = 1
) {
    var reached = true
    var topNote: Notes? = null
    var columns: Int = 1
    private var deleteCalledAndExecuted = AtomicBoolean(false)
    private var shouldRefresh = AtomicBoolean(false)

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPositions(IntArray(columns)).minOrNull() ?: RecyclerView.NO_POSITION
                if(!reached && (adapter.snapshot().items[firstVisiblePosition].id == topNote?.id)) {
                    recyclerView.removeOnScrollListener(this)
                    reached = true
                }
            }
        }
    }

    init {
        columns = totalColumns

        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(onScrollListener)
        adapter.addLoadStateListener { restoreDesiredState(viewsToDisable) }

        viewModel.getLatestNote.observe(fragment.viewLifecycleOwner) { topNote = it }

        fragment.lifecycleScope.launch {
            viewModel.allNotes.collectLatest {
                delay(200)
                if(deleteCalledAndExecuted.get()) {
                    deleteCalledAndExecuted.set(false)
                    shouldRefresh.set(!shouldRefresh.get())
                    if(shouldRefresh.get()) adapter.refresh()
                }
                else {
                    adapter.clearCache()
                    adapter.submitData(it)
                }

                if(viewModel.justModified) {
                    viewModel.justModified = false
                    startScrolling = true
                }
            }
        }
    }

    private fun restoreDesiredState(viewsToDisable: Array<View>?) {
        if(startScrolling == false) return
        startScrolling = false
        reached = false
        fragment.lifecycleScope.launch {
            viewsToDisable?.forEach { it.isEnabled = false }
            while (!reached && adapter.itemCount>0 && (adapter.getItemAt(0)?.id != topNote?.id)) {
                delay(400)
                recyclerView.smoothScrollToPosition(0)
            }
            viewsToDisable?.forEach { it.isEnabled = true }
        }

    }

    fun resumeAdapter(dbUpdated: Boolean = false) {
        if(dbUpdated) {
            deleteCalledAndExecuted.set(false)
            shouldRefresh.set(true)
            fragment.lifecycleScope.launch {
                adapter.refresh()
                delay(450)
                startScrolling = true
                restoreDesiredState(viewsToDisable)
                adapter.notifyItemChanged(0)
            }
            return
        }
        if(!shouldRefresh.get()) {
            deleteCalledAndExecuted.set(false)
            shouldRefresh.set(true)
            fragment.lifecycleScope.launch {
                adapter.refresh()
            }
        }
    }

    fun pauseAdapter() {
        deleteCalledAndExecuted.set(true)
    }
}