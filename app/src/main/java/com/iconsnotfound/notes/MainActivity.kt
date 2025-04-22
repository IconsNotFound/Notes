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

package com.iconsnotfound.notes

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.iconsnotfound.data.repositories.LicencesRepository
import com.iconsnotfound.data.repositories.SettingsRepository
import com.iconsnotfound.data.sharedpreferences.NotesSharedPreferences
import com.iconsnotfound.data.sharedpreferences.SharedPreferencesConstants
import com.iconsnotfound.presentation.application.DbApplication
import com.iconsnotfound.presentation.viewmodels.LicencesViewModel
import com.iconsnotfound.presentation.viewmodels.LicencesViewModelFactory
import com.iconsnotfound.presentation.viewmodels.NotesViewModel
import com.iconsnotfound.presentation.viewmodels.NotesViewModelFactory
import com.iconsnotfound.presentation.viewmodels.SettingsViewModel
import com.iconsnotfound.presentation.viewmodels.SettingsViewModelFactory
import com.iconsnotfound.ui.utils.LicencesConstants
import com.iconsnotfound.ui.utils.SettingsConstants
import com.iconsnotfound.ui.utils.Util
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    val notesViewModel: NotesViewModel by viewModels {
        NotesViewModelFactory((application as DbApplication).repository)
    }
    private lateinit var settingsRepository: SettingsRepository
    val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(settingsRepository)
    }
    private lateinit var licencesRepository: LicencesRepository
    val licencesViewModel: LicencesViewModel by viewModels {
        LicencesViewModelFactory(licencesRepository)
    }
    lateinit var notesSharedPreferences: NotesSharedPreferences
    lateinit var mainView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainView = findViewById(R.id.main)

        notesSharedPreferences = NotesSharedPreferences(this)
        settingsRepository = SettingsRepository(this, SettingsConstants.getSettingsList(this, notesSharedPreferences))
        licencesRepository = LicencesRepository(this, LicencesConstants.getList(this))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        lifecycleScope.launch {
            val appTheme = notesSharedPreferences.getEnum(SharedPreferencesConstants.THEME)
            Util.setAppTheme(appTheme)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}