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

import android.animation.ValueAnimator
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.InputType
import android.text.SpannableString
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import com.iconsnotfound.data.sharedpreferences.SharedPreferencesConstants.AppTheme
import com.iconsnotfound.ui.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.net.toUri

object Util {

    fun showConfirmationDialog(
        context: Context,
        alertDialogTexts: AlertDialogTexts,
        onYesClicked: () -> Unit,
        onNoClicked: (() -> Unit)? = null,
        onNeutralClicked: (() -> Unit)? = null,
        bCancellable: Boolean = true,
        layoutRes: Int = R.layout.confirm_dialog,
        icon: Int? = null,
        inputToCheck: String? = null,
        inputHint: String? = null,
        inputType: Int? = InputType.TYPE_CLASS_TEXT,
        inputErrorMsg: String? = null
    ) {
        showConfirmationDialog(
            context,
            alertDialogTexts.adTitle,
            SpannableString(alertDialogTexts.adMessage),
            onYesClicked,
            onNoClicked,
            onNeutralClicked,
            alertDialogTexts.adYesButton,
            alertDialogTexts.adNoButton,
            alertDialogTexts.adNeutral,
            bCancellable,
            layoutRes,
            icon,
            inputToCheck,
            inputHint,
            inputType,
            inputErrorMsg
        )
    }

    fun showConfirmationDialog(
        context: Context,
        title: String,
        message: SpannableString,
        onYesClicked: () -> Unit,
        onNoClicked: (() -> Unit)? = null,
        onNeutralClicked: (() -> Unit)? = null,
        yesButtonText: String = context.resources.getString(R.string.yes),
        noButtonText: String = context.resources.getString(R.string.no),
        neutralButtonText: String? = null,
        bCancellable: Boolean = true,
        layoutRes: Int = R.layout.confirm_dialog,
        icon: Int? = null,
        inputToCheck: String? = null,
        inputHint: String? = null,
        inputType: Int? = InputType.TYPE_CLASS_TEXT,
        inputErrorMsg: String? = null,
        bShowTitle: Boolean = true,
        bShowNoButton: Boolean = true,
        bShowScrollbar: Boolean = false
    ) {
        val dialogView = (context as Activity).layoutInflater.inflate(layoutRes, null)

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .setCancelable(bCancellable)
            .create()

        val titleTextView = dialogView.findViewById<TextView>(R.id.dialog_title)
        val bodyTextView = dialogView.findViewById<TextView>(R.id.dialog_message)
        val yesButton = dialogView.findViewById<Button>(R.id.dialog_yes_button)
        val noButton = dialogView.findViewById<Button>(R.id.dialog_no_button)
        val neutralButton = dialogView.findViewById<Button>(R.id.dialog_neutral_button)
        val iconImageView = dialogView.findViewById<ImageView>(R.id.dialog_icon)
        val inputTextLayout = dialogView.findViewById<TextInputLayout>(R.id.input_text)
        val rootView = dialogView.findViewById<ConstraintLayout>(R.id.dialog_root_view)
        val horizontalBar = dialogView.findViewById<View>(R.id.horizontal_bar)
        val scrollView = dialogView.findViewById<ScrollView>(R.id.parent_scroll_view)

        titleTextView?.text = title
        bodyTextView?.text = message
        yesButton?.text = yesButtonText
        noButton?.text = noButtonText
        scrollView?.isVerticalScrollBarEnabled = bShowScrollbar

        if(!bShowTitle && icon == null) {
            titleTextView.visibility = View.GONE
            horizontalBar.visibility = View.GONE
        }

        rootView?.setOnClickListener {
            dialogView.hideSoftKeyboard()
        }

        inputToCheck?.let {
            inputTextLayout.hint = inputHint
            inputTextLayout.editText?.isEnabled = true
            inputTextLayout.visibility = View.VISIBLE
            inputType?.let{ inputTextLayout.editText?.inputType = it }
        }

        if(icon==null) {
            if(Build.VERSION.SDK_INT > 16) {
                (titleTextView.layoutParams as MarginLayoutParams).marginStart = 0
            }
            else (titleTextView.layoutParams as MarginLayoutParams).leftMargin = 0
        }
        else {
            iconImageView.setImageResource(icon)
            iconImageView.visibility = View.VISIBLE
        }

        yesButton?.setOnClickListener {
            inputToCheck?.let {
                if(inputTextLayout.editText?.text?.toString()?.trim() != it.trim()) {
                    inputTextLayout.error = inputErrorMsg
                    return@setOnClickListener
                }
            }
            onYesClicked()
            dialog.dismiss()
        }

        noButton?.setOnClickListener {
            onNoClicked?.invoke()
            dialog.dismiss()
        }
        if(!bShowNoButton) {
            noButton?.visibility = View.GONE
        }

        if(neutralButtonText == null) neutralButton.visibility = View.GONE
        else {
            neutralButton?.setOnClickListener {
                onNeutralClicked?.invoke()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    fun raiseSoftKeyboard(context: Context, view: View){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun shareText(context: Context, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(shareIntent, null))
    }

    fun copyToClipboard(context: Context, text: String) {
        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText("Copied Text", text))
        Toast.makeText(context, context.resources.getString(R.string.note_copied), Toast.LENGTH_SHORT).show()
    }

    fun getAppVersion(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "N/A"
        }
    }

    fun animateCounter(textView: TextView, startValue: Int = 0, endValue: Int, duration: Long = 2000L) {
        val animator = ValueAnimator.ofInt(startValue, endValue)
        animator.duration = duration
        animator.addUpdateListener { animation ->
            val currentValue = animation.animatedValue as Int
            textView.text = String.format(currentValue.toString())
        }
        animator.start()
    }

    fun setAppTheme(appTheme: AppTheme) {
        when(appTheme) {
            AppTheme.THEME_SYSTEM_DEFAULT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            AppTheme.THEME_LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            AppTheme.THEME_DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    fun getAppTheme(): AppTheme {
        return when(AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> AppTheme.THEME_DARK
            AppCompatDelegate.MODE_NIGHT_NO -> AppTheme.THEME_LIGHT
            else -> AppTheme.THEME_SYSTEM_DEFAULT
        }
    }

    fun showThemeDialog(
        context: Context,
        onSystemDefaultClicked: () -> Unit,
        onLightClicked: (() -> Unit),
        onDarkClicked: (() -> Unit),
        title: String = context.resources.getString(R.string.theme),
        systemDefaultText: String = context.resources.getString(R.string.theme_default),
        lightText: String = context.resources.getString(R.string.theme_light),
        darkText: String = context.resources.getString(R.string.theme_dark),
        selectedTheme: AppTheme = AppTheme.THEME_SYSTEM_DEFAULT,
        bCancellable: Boolean = true,
        layoutRes: Int = R.layout.theme_dialog
    ) {
        val dialogView = (context as Activity).layoutInflater.inflate(layoutRes, null)

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .setCancelable(bCancellable)
            .create()

        val titleTextView = dialogView.findViewById<TextView>(R.id.theme_dialog_title)
        val systemDefaultButton = dialogView.findViewById<RadioButton>(R.id.theme_dialog_radio_system_default)
        val lightButton = dialogView.findViewById<RadioButton>(R.id.theme_dialog_radio_light)
        val darkButton = dialogView.findViewById<RadioButton>(R.id.theme_dialog_radio_dark)

        when(selectedTheme) {
            AppTheme.THEME_SYSTEM_DEFAULT -> systemDefaultButton.isChecked = true
            AppTheme.THEME_LIGHT -> lightButton.isChecked = true
            else -> darkButton.isChecked = true
        }

        titleTextView?.text = title
        systemDefaultButton.text = systemDefaultText
        lightButton.text = lightText
        darkButton.text = darkText

        systemDefaultButton?.setOnClickListener {
            onSystemDefaultClicked()
            dialog.dismiss()
        }

        lightButton?.setOnClickListener {
            onLightClicked()
            dialog.dismiss()
        }

        darkButton?.setOnClickListener {
            onDarkClicked()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showAboutDialog(
        context: Context,
        appLogoRes: Int,
        version: String,
        appLogoBackgroundRes: Int? = null,
        bCancellable: Boolean = true,
        layoutRes: Int = R.layout.app_about_dialog
    ) {
        val dialogView = (context as Activity).layoutInflater.inflate(layoutRes, null)

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .setCancelable(bCancellable)
            .create()

        val appLogo = dialogView.findViewById<ImageView>(R.id.app_logo)
        val appLogoBackground = dialogView.findViewById<ShapeableImageView>(R.id.app_logo_background)
        val appVersion = dialogView.findViewById<TextView>(R.id.app_version)
        val close = dialogView.findViewById<Button>(R.id.close)

        appLogoRes.let { appLogo.setImageResource(it) }
        appLogoBackgroundRes?.let { appLogoBackground.setImageResource(it) }
        appVersion.text = context.resources.getString(R.string.version, version)
        close.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    fun startTextScroll(textView: TextView, lifecycleScope: LifecycleCoroutineScope) {
        textView.let {
            if(isTextOverflowing(it)) {
                it.isHorizontalFadingEdgeEnabled = true
                lifecycleScope.launch {
                    startScrollingAnimation(it, getScrollDuration(it))
                }
            }
        }
    }

    private suspend fun startScrollingAnimation(
        textView: TextView,
        duration: Long = 0L
    ) {
        textView.let {
            it.ellipsize = TextUtils.TruncateAt.MARQUEE
            it.isSingleLine = true
            it.setHorizontallyScrolling(true)
            it.marqueeRepeatLimit = 1
        }

        while (true) {
            textView.let {
                it.isSelected = true
                delay( duration)
                it.isSelected = false
            }
        }
    }

    private fun isTextOverflowing(textView: TextView): Boolean {
        val textWidth = textView.paint.measureText(textView.text.toString())
        return textWidth > textView.width
    }

    private fun getScrollDuration(textView: TextView): Long {
        val textWidth = textView.paint.measureText(textView.text.toString())
        val viewWidth = textView.width.toFloat()
        return ((textWidth+viewWidth)/30).toLong()*1000
    }



    fun View.setKeyboardAnimationListener(onKeyboardHidden: () -> Unit) {
        if(!this.isKeyboardVisible()) {
            onKeyboardHidden()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ViewCompat.setWindowInsetsAnimationCallback(this, object : WindowInsetsAnimationCompat.Callback(
                DISPATCH_MODE_CONTINUE_ON_SUBTREE
            ) {
                override fun onProgress(
                    insets: WindowInsetsCompat,
                    runningAnimations: MutableList<WindowInsetsAnimationCompat>
                ): WindowInsetsCompat {
                    return insets
                }

                override fun onEnd(animation: WindowInsetsAnimationCompat) {
                    super.onEnd(animation)
                    val insets = rootWindowInsets
                    if (insets?.isVisible(WindowInsets.Type.ime()) == false) {
                        onKeyboardHidden()
                    }
                }
            })
        } else {
            var lastKeyboardVisible = false

            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val insets = ViewCompat.getRootWindowInsets(this@setKeyboardAnimationListener)
                    if (insets != null) {
                        val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
                        if (!imeVisible && lastKeyboardVisible) {
                            onKeyboardHidden()
                        }
                        lastKeyboardVisible = imeVisible
                    } else {
                        val rootView = rootView ?: return
                        val visibleFrameHeight = rootView.height
                        val screenHeight = resources.displayMetrics.heightPixels
                        val heightDiff = screenHeight - visibleFrameHeight
                        val isKeyboardVisible = heightDiff > screenHeight * 0.15

                        if (!isKeyboardVisible && lastKeyboardVisible) {
                            onKeyboardHidden()
                        }
                        lastKeyboardVisible = isKeyboardVisible
                    }
                }
            })
        }
    }

    fun View.isKeyboardVisible(): Boolean {
        val insets = ViewCompat.getRootWindowInsets(this) ?: return false
        return insets.isVisible(WindowInsetsCompat.Type.ime())
    }

    fun View.hideSoftKeyboard(){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun openUrlInBrowser(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            val packageManager = context.packageManager
            if (intent.resolveActivity(packageManager) == null) {
                val chooser = Intent.createChooser(intent, context.resources.getString(R.string.choose_a_browser))
                context.startActivity(chooser)
            } else {
                context.startActivity(intent)
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context,
                context.resources.getString(R.string.no_browser_found), Toast.LENGTH_SHORT).show()
        }
    }

    fun viewCompletelyRendered(view: View, onRenderComplete: () -> Unit) {
        view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if (view.isShown) {
                    view.viewTreeObserver.removeOnPreDrawListener(this)
                    onRenderComplete()
                }
                return true
            }
        })
    }

    fun getRawTextFile(context: Context, resId: Int): String {
        return context.resources.openRawResource(resId).bufferedReader().use { it.readText() }
    }
}