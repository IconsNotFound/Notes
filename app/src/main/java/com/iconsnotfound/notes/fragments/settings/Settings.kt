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

import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import com.iconsnotfound.data.sharedpreferences.SharedPreferencesConstants
import com.iconsnotfound.data.sharedpreferences.SharedPreferencesConstants.AppTheme
import com.iconsnotfound.notes.MainActivity
import com.iconsnotfound.notes.databinding.FragmentSettingsBinding
import com.iconsnotfound.ui.settingslist.SettingsAdapter
import com.iconsnotfound.ui.utils.SettingsConstants
import com.iconsnotfound.ui.utils.Util
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class Settings : Fragment(), SettingsAdapter.OnItemClickListener {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding
    private val mainActivity get() = requireActivity() as MainActivity
    private val viewModel get() = mainActivity.notesViewModel

    private lateinit var settingsAdapter: SettingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
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

        settingsAdapter = SettingsAdapter(lifecycleScope, this)
        binding?.recyclerView?.let {
            it.layoutManager = GridLayoutManager(requireContext(), 2)

            (it.layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    return if (position == settingsAdapter.itemCount - 1) 2 else 1
                }
            }

            it.adapter = settingsAdapter
            mainActivity.settingsViewModel.allSettingsItems.collectLatest { data ->
                settingsAdapter.submitData(data)
            }
        }
    }

    override fun onSettingsItemClick(view: View, position: Int) {
        val clickedItem = settingsAdapter.getItemAt(position)
        when(clickedItem?.id) {
            SettingsConstants.ITEM_THEME -> {
                Util.showThemeDialog(
                    requireContext(),
                    onSystemDefaultClicked = {
                        Util.setAppTheme(AppTheme.THEME_SYSTEM_DEFAULT)
                        mainActivity.notesSharedPreferences.setEnum(SharedPreferencesConstants.THEME, Util.getAppTheme())
                        clickedItem.subTitle = resources.getString(com.iconsnotfound.ui.R.string.theme_default)
                        settingsAdapter.notifyItemChanged(position)
                    },
                    onLightClicked = {
                        Util.setAppTheme(AppTheme.THEME_LIGHT)
                        mainActivity.notesSharedPreferences.setEnum(SharedPreferencesConstants.THEME, Util.getAppTheme())
                        clickedItem.subTitle = resources.getString(com.iconsnotfound.ui.R.string.theme_light)
                        settingsAdapter.notifyItemChanged(position)
                    },
                    onDarkClicked = {
                        Util.setAppTheme(AppTheme.THEME_DARK)
                        mainActivity.notesSharedPreferences.setEnum(SharedPreferencesConstants.THEME, Util.getAppTheme())
                        clickedItem.subTitle = resources.getString(com.iconsnotfound.ui.R.string.theme_dark)
                        settingsAdapter.notifyItemChanged(position)
                    },
                    selectedTheme = Util.getAppTheme()
                )
                return
            }
            SettingsConstants.DELETE_ALL -> {
                val (description, solution) = prepareDeleteAllDescriptionAndAnswer()
                Util.showConfirmationDialog(
                    requireContext(),
                    requireContext().resources.getString(com.iconsnotfound.ui.R.string.delete_all_note_title),
                    description,
                    onYesClicked = { deleteAllNote() },
                    yesButtonText = requireContext().resources.getString(com.iconsnotfound.ui.R.string.delete_all),
                    noButtonText = requireContext().resources.getString(com.iconsnotfound.ui.R.string.cancel),
                    layoutRes = com.iconsnotfound.ui.R.layout.confirm_dialog_2,
                    icon = com.iconsnotfound.ui.R.drawable.icon_delete_all_notes,
                    inputToCheck = solution,
                    inputHint = requireContext().resources.getString(com.iconsnotfound.ui.R.string.enter_the_answer_here),
                    inputType = InputType.TYPE_CLASS_NUMBER,
                    inputErrorMsg = requireContext().resources.getString(com.iconsnotfound.ui.R.string.incorrect_answer),
                )
                return
            }
            SettingsConstants.PRIVACY_POLICY -> {
                Util.openUrlInBrowser(requireContext(), requireContext().resources.getString(com.iconsnotfound.ui.R.string.privacy_policy_url))
                return
            }
            SettingsConstants.INFO -> {
                Util.showAboutDialog(
                    requireContext(),
                    appLogoRes = com.iconsnotfound.notes.R.mipmap.logo_foreground,
                    version = Util.getAppVersion(requireContext()),
                    appLogoBackgroundRes = com.iconsnotfound.notes.R.mipmap.logo_background
                )
                return
            }
            SettingsConstants.LICENCE -> {
                findNavController().navigate(
                    com.iconsnotfound.notes.R.id.action_settings_to_licence,
                    null,
                    navOptions {
                        popUpTo(com.iconsnotfound.notes.R.id.licence) {
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
                return
            }
            SettingsConstants.DONATE -> {
                Util.openUrlInBrowser(requireContext(), requireContext().resources.getString(com.iconsnotfound.ui.R.string.patreon_url))
                return
            }
        }
        when(view.id) {
            com.iconsnotfound.ui.R.id.youtube -> {
                Util.openUrlInBrowser(requireContext(), requireContext().resources.getString(com.iconsnotfound.ui.R.string.youtube_url))
            }
            com.iconsnotfound.ui.R.id.github -> {
                Util.openUrlInBrowser(requireContext(), requireContext().resources.getString(com.iconsnotfound.ui.R.string.github_url))
            }
            else -> {
                Toast.makeText(requireContext(), "Not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteAllNote() {
        viewModel.deleteAllNotes().invokeOnCompletion {
            lifecycleScope.launch {
                delay(300)
                if(viewModel.totalNotes.value == 0) {
                    Toast.makeText(requireContext(), requireContext().resources.getString(com.iconsnotfound.ui.R.string.delete_all_confirmation), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun prepareDeleteAllDescriptionAndAnswer(): Pair<SpannableString, String> {
        val (number1, number2) = generateRandomNumbers()
        val numberColor = ContextCompat.getColor(requireContext(), com.iconsnotfound.ui.R.color.md_theme_tertiary)
        val dltMsg = getString(com.iconsnotfound.ui.R.string.delete_all_note_message)
        val mathProblem = getString(com.iconsnotfound.ui.R.string.delete_all_note_message_ext, number1.toString(), number2.toString())
        val spannable = SpannableString(mathProblem)
        val combinedString = SpannableString(dltMsg + spannable.toString())

        combinedString.setSpan(StyleSpan(Typeface.BOLD_ITALIC), dltMsg.length, combinedString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        combinedString.setSpan(ForegroundColorSpan(numberColor), dltMsg.length, combinedString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return Pair(combinedString, (number1+number2).toString())
    }

    private fun generateRandomNumbers(): Pair<Int, Int> {
        val number1 = (1..9).random()
        val number2 = (1..9).random()
        return Pair(number1, number2)
    }
}