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

package com.iconsnotfound.notes.fragments

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.iconsnotfound.data.sharedpreferences.SharedPreferencesConstants
import com.iconsnotfound.db.entities.Notes
import com.iconsnotfound.notes.MainActivity
import com.iconsnotfound.notes.databinding.FragmentHomeBinding
import com.iconsnotfound.notes.utils.AppConstants
import com.iconsnotfound.ui.noteslist.NotesListAdapter
import com.iconsnotfound.ui.utils.RecyclerViewScroll
import com.iconsnotfound.ui.utils.Util
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Home : Fragment(), NotesListAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val mainActivity get() = requireActivity() as MainActivity
    private val viewModel get() = mainActivity.notesViewModel

    private lateinit var notesListAdapter: NotesListAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private lateinit var recyclerViewScroll: RecyclerViewScroll


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner) { onBackPressed() }

        lifecycleScope.launch {
            if(!(binding?.recyclerView?.visibility == View.VISIBLE || binding?.emptyViewMsg?.visibility == View.VISIBLE)) {
                binding?.progressCircular?.visibility = View.VISIBLE
            }
        }
        Util.viewCompletelyRendered(view) {
            lifecycleScope.launch {
                init()
            }
        }
    }

    private fun init() {
        viewModel.totalNotes.observe(viewLifecycleOwner) {
            val b = it.toString().toInt() > 0
            if(binding?.progressCircular?.visibility == View.VISIBLE) {
                lifecycleScope.launch {
                    binding?.progressCircular?.visibility = View.GONE
                    delay(100)
                }
            }

            binding?.recyclerView?.visibility = if(b) View.VISIBLE else View.GONE
            binding?.emptyViewMsg?.visibility = if(!b) View.VISIBLE else View.GONE
            binding?.notesCounter?.let {textView ->
                Util.animateCounter(textView, textView.text.toString().toInt(), endValue = it, duration = 1000)
            }
            mainActivity.notesSharedPreferences.setInt(SharedPreferencesConstants.TOTAL_NOTES, it)
        }

        layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        viewModel.countAllNotes = mainActivity.notesSharedPreferences.getInt(SharedPreferencesConstants.TOTAL_NOTES)

        notesListAdapter = NotesListAdapter(this)
        binding?.recyclerView?.let {
            it.layoutManager = layoutManager

            binding?.addNoteFab?.let { addNote ->
                recyclerViewScroll = RecyclerViewScroll(this, it, notesListAdapter, viewModel, viewsToDisable =  arrayOf(addNote, it))
            }
        }

        binding?.addNoteFab?.let {
            it.setOnClickListener {
                recyclerViewScroll.resumeAdapter()
                val action = HomeDirections.actionHomeToModify(null, null)
                findNavController().navigate(action)
            }
        }

        binding?.settingsButton?.let {
            it.setOnClickListener {
                findNavController().navigate(
                    com.iconsnotfound.notes.R.id.action_home_to_settings,
                    null,
                    navOptions {
                        popUpTo(com.iconsnotfound.notes.R.id.home) {
                            inclusive = false
                            saveState = true
                        }
                        restoreState = true
                        anim {
                            enter = com.iconsnotfound.ui.R.anim.slide_in
                            popExit = com.iconsnotfound.ui.R.anim.slide_out
                        }
                    }
                )
            }
        }

        parentFragmentManager.setFragmentResultListener(AppConstants.MODIFIED, viewLifecycleOwner) { _, it ->
            if(it.getBoolean(AppConstants.MODIFIED)) {
                recyclerViewScroll.resumeAdapter(true)
            }
        }

        binding?.addNoteFab?.visibility = View.VISIBLE
    }


    private fun onBackPressed() {
        lifecycleScope.launch {
            mainActivity.moveTaskToBack(true)
        }
    }

    private fun deleteNote(clickedItem: Notes?) {
        clickedItem?.let {
            recyclerViewScroll.pauseAdapter()
            viewModel.deleteNote(it.id)
            notesListAdapter.removeItem(it)
        }
    }

    override fun onNoteItemClick(view: View, position: Int) {
        val clickedItem = notesListAdapter.getItemAt(position)
        when(view.id) {
            com.iconsnotfound.ui.R.id.share_note -> {
                clickedItem?.let { Util.shareText(requireContext(), it.text) }
            }

            com.iconsnotfound.ui.R.id.copy_note -> {
                clickedItem?.let { Util.copyToClipboard(requireContext(), it.text) }
            }

            com.iconsnotfound.ui.R.id.delete_note -> {
                Util.showConfirmationDialog(
                    requireContext(),
                    requireContext().resources.getString(com.iconsnotfound.ui.R.string.delete_note_title),
                    SpannableString(requireContext().resources.getString(com.iconsnotfound.ui.R.string.delete_note_message)),
                    onYesClicked = { deleteNote(clickedItem) },
                    yesButtonText = requireContext().resources.getString(com.iconsnotfound.ui.R.string.delete),
                    noButtonText = requireContext().resources.getString(com.iconsnotfound.ui.R.string.cancel),
                    icon = com.iconsnotfound.ui.R.drawable.icon_delete
                )
            }

            com.iconsnotfound.ui.R.id.note_card_view -> {
                clickedItem?.let {
                    val action = HomeDirections.actionHomeToModify(requireContext().resources.getString(com.iconsnotfound.ui.R.string.modify_note), it)
                    findNavController().navigate(action)
                }
            }
        }
    }
}