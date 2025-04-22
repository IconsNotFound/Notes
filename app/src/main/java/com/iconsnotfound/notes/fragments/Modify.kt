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
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.iconsnotfound.db.entities.Notes
import com.iconsnotfound.notes.MainActivity
import com.iconsnotfound.notes.databinding.FragmentModifyBinding
import com.iconsnotfound.notes.utils.AppConstants
import com.iconsnotfound.ui.utils.AlertDialogTexts
import com.iconsnotfound.ui.utils.ScrollAdjuster
import com.iconsnotfound.ui.utils.Util
import com.iconsnotfound.ui.utils.Util.hideSoftKeyboard
import com.iconsnotfound.ui.utils.Util.isKeyboardVisible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Modify : Fragment() {
    private val args: ModifyArgs? by navArgs()
    private var _binding: FragmentModifyBinding? = null
    private val binding get() = _binding
    private val mainActivity get() = requireActivity() as MainActivity
    private val rootView get() = mainActivity.mainView
    private val viewModel get() = mainActivity.notesViewModel
    private var notes: Notes? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentModifyBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onBackPressed() {
        binding?.noteModifyEditText?.text?.let {
            rootView.hideSoftKeyboard()
            if(it.trim().isEmpty()) {
                finish()
                return
            }

            val tempNote = it.trim().toString()
            notes?.let { it3->
                if(tempNote == it3.text) {
                    finish()
                    return
                }
            }



            val yesFunction: () -> Unit = { saveFunction(tempNote) }
            val noFunction: () -> Unit = { lifecycleScope.launch { finish() } }
            val alertDialogTexts = AlertDialogTexts(
                requireContext(),
                if(notes == null) AlertDialogTexts.Template.Save else AlertDialogTexts.Template.Update
            )
            val icon = if(notes != null) com.iconsnotfound.ui.R.drawable.icon_update else com.iconsnotfound.ui.R.drawable.icon_save

            Util.showConfirmationDialog(
                requireContext(),
                alertDialogTexts,
                yesFunction, noFunction,
                layoutRes = com.iconsnotfound.ui.R.layout.confirm_dialog_2,
                icon = icon
            )
        }
    }

    private fun finish() {
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args?.notes?.let { notes = it }
        args?.title?.let { binding?.title?.text = it }

        mainActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            rootView.let {
                if(it.isKeyboardVisible()) {
                    it.hideSoftKeyboard()
                    return@addCallback
                }
            }
            onBackPressed()
        }
        lifecycleScope.launch {
            init()
        }
    }

    private fun init() {
        binding?.backButton?.setOnClickListener { onBackPressed() }
        binding?.scrollView?.let {
            it.viewTreeObserver.addOnGlobalLayoutListener( ScrollAdjuster(it).createGlobalLayoutListener() )
        }
        binding?.main?.setOnClickListener {
            binding?.noteModifyEditText?.requestFocus()
            Util.raiseSoftKeyboard(requireContext(), binding!!.noteModifyEditText)
        }
        binding?.noteModifyTextInputLayout?.setOnClickListener {
            binding?.noteModifyEditText?.requestFocus()
            Util.raiseSoftKeyboard(requireContext(), binding!!.noteModifyEditText)
        }

        binding?.noteModifyEditText?.requestFocus()
        notes?.let { binding?.noteModifyEditText?.append(it.text) }
        binding?.saveNote?.setOnClickListener {
            binding?.noteModifyEditText?.text?.trim().let { saveFunction(it) }
        }
        binding?.cancelNote?.setOnClickListener { finish() }
    }

    private fun saveFunction(charSequence: CharSequence?) {
        lifecycleScope.launch {
            if(notes == null) save(charSequence) else update(charSequence)
        }
    }

    private suspend fun save(charSequence: CharSequence?) {
        if(TextUtils.isEmpty(charSequence)) {
            Toast.makeText(requireContext(),
                requireContext().resources.getString(com.iconsnotfound.ui.R.string.empty_save_message),
                Toast.LENGTH_SHORT).show()
            return
        }

        withContext(Dispatchers.IO) {
            viewModel.insertNote(Notes(charSequence.toString(), System.currentTimeMillis(), System.currentTimeMillis()))
            viewModel.justModified = true
            parentFragmentManager.setFragmentResult(AppConstants.MODIFIED, Bundle().apply {
                putBoolean(AppConstants.MODIFIED, true)
            })
            withContext(Dispatchers.Main) { finish() }
        }
    }

    private suspend fun update(charSequence: CharSequence?) {
        if(TextUtils.isEmpty(charSequence)) {
            Toast.makeText(requireContext(),
                requireContext().resources.getString(com.iconsnotfound.ui.R.string.empty_save_message),
                Toast.LENGTH_SHORT).show()
            return
        }

        withContext(Dispatchers.IO) {
            val tempNote = charSequence.toString()
            notes?.let {
                if(tempNote == it.text) {
                    withContext(Dispatchers.Main) { finish() }
                    return@withContext
                }
                it.text = tempNote
                it.lastModifiedTime = System.currentTimeMillis()
                viewModel.updateNote(it)
                viewModel.justModified = true
                parentFragmentManager.setFragmentResult(AppConstants.MODIFIED, Bundle().apply {
                    putBoolean(AppConstants.MODIFIED, true)
                })
            }
            withContext(Dispatchers.Main) { finish() }
            return@withContext
        }
    }


}