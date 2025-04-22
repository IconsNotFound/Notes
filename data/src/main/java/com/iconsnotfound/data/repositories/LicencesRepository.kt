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

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.iconsnotfound.data.utils.LicencesItem
import kotlinx.coroutines.flow.Flow

class LicencesRepository(private val context: Context, private val licenceItemsList: List<LicencesItem>, private val pageSize: Int = 8, private val enablePlaceholder: Boolean = false) {
    private class LicencesPagingSource(
        private val list: List<LicencesItem>
    ): PagingSource<Int, LicencesItem>() {
        override fun getRefreshKey(state: PagingState<Int, LicencesItem>): Int? {
            return state.anchorPosition?.let {
                val closestPage = state.closestPageToPosition(it)
                closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
            }
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LicencesItem> {
            val currentPage = params.key ?: 0
            val pageSize = params.loadSize

            val fromIndex = currentPage * pageSize
            val toIndex = (fromIndex + pageSize).coerceAtMost(list.size)

            return if(fromIndex < list.size) {
                LoadResult.Page(
                    data = list.subList(fromIndex, toIndex),
                    prevKey = if(currentPage == 0) null else currentPage - 1,
                    nextKey = if(toIndex == list.size) null else currentPage + 1
                )
            }
            else {
                LoadResult.Error(Exception("No more data"))
            }

        }
    }
    fun getAllItems(): Flow<PagingData<LicencesItem>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = enablePlaceholder),
            pagingSourceFactory = {LicencesPagingSource(licenceItemsList)}
        ).flow
    }
}