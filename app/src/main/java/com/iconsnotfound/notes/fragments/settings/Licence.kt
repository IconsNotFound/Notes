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

package com.iconsnotfound.notes.fragments.settings

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.iconsnotfound.notes.MainActivity
import com.iconsnotfound.notes.databinding.FragmentLicenceBinding
import com.iconsnotfound.ui.licenceslist.LicencesAdapter
import com.iconsnotfound.ui.utils.LicencesConstants
import com.iconsnotfound.ui.utils.Util
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class Licence : Fragment(), LicencesAdapter.onItemClickListener {
    private var _binding: FragmentLicenceBinding? = null
    private val binding get() = _binding
    private val mainActivity get() = requireActivity() as MainActivity

    private lateinit var adapter: LicencesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLicenceBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onBackPressed() {
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner) { onBackPressed() }
        Util.viewCompletelyRendered(view) {
            lifecycleScope.launch {
                init()
            }
        }
    }

    private suspend fun init() {
        binding?.backButton?.setOnClickListener { onBackPressed() }

        adapter = LicencesAdapter(lifecycleScope, this)
        binding?.recyclerView?.let {
            it.layoutManager = GridLayoutManager(requireContext(), 1)
            it.adapter = adapter
            mainActivity.licencesViewModel.allItems.collectLatest { data ->
                adapter.submitData(data)
            }
        }
    }

    override fun onLicencesItemClick(view: View, position: Int) {
        val clickedItem = adapter.getItemAt(position)
        when(clickedItem?.id) {
            LicencesConstants.NOTES -> {
                lifecycleScope.launch { showDialog(com.iconsnotfound.data.R.raw.notes_license) }
            }
            LicencesConstants.ANDROIDX -> {
                lifecycleScope.launch { showDialog(com.iconsnotfound.data.R.raw.androidx_license) }
            }
            LicencesConstants.MATERIAL_DESIGN -> {
                lifecycleScope.launch { showDialog(com.iconsnotfound.data.R.raw.material_design_license) }
            }
            LicencesConstants.INTUIT_SDP -> {
                lifecycleScope.launch { showDialog(com.iconsnotfound.data.R.raw.intuit_sdp_license) }
            }
            LicencesConstants.INTUIT_SSP -> {
                lifecycleScope.launch { showDialog(com.iconsnotfound.data.R.raw.intuit_ssp_license) }
            }
        }
    }

    private fun showDialog(licenceID: Int) {
        Util.showConfirmationDialog(
            context = requireContext(),
            title = "",
            message = SpannableString(Util.getRawTextFile(requireContext(), licenceID)),
            {},
            bShowNoButton = false,
            bShowTitle = false,
            yesButtonText = requireContext().resources.getString(com.iconsnotfound.ui.R.string.ok),
            bShowScrollbar = true,
        )
    }
}