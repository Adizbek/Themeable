package github.adizbek.themeable

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.appcompat.app.AlertDialog
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import java.io.File

class ThemeManager<T : ThemeInterface>(
    context: Context,
    var themeClass: Class<T>,
    val folder: File = context.getDir("themes", Context.MODE_PRIVATE),
    val fileExtension: String = "theme"
) {
    var themes = ArrayList<T>()
    var selectedTheme: T? = null

    var listeners = ArrayList<ThemeListener>()

    init {
        ThemeEditor(context, this)

        loadThemes()
        loadSavedTheme(context)
    }

    fun registerListener(listener: ThemeListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ThemeListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners(key: String, color: Int) {
        for (listener in listeners) {
            for (bindStyle in listener.bindStyles()) {
                if (bindStyle.getKey() == key) {
                    bindStyle.processColor(color)

                    listener.boundStyle(key, color)
                }
            }

            listener.onNewThemeApplied()
        }
    }

    private fun notifyListeners(theme: ThemeInterface) {
        for (key in theme.values.keys) {
            notifyListeners(key, theme.getStyle(key))
        }
    }

    fun applyStyles(themeListener: ThemeListener) {
        val styles = getCurrentTheme().values

        for (binder in themeListener.bindStyles()) {

            if (styles.containsKey(binder.getKey())) {
                binder.processColor(styles[binder.getKey()]!!)
            }

        }
    }

    fun setStyle(key: String, color: Int) {
        getCurrentTheme().setStyle(key, color)

        notifyListeners(key, color)
    }


    fun getThemeFile(name: String, create: Boolean = true): File {
        val f = File("${folder.absoluteFile}/$name.$fileExtension")

        if (!f.exists() && create) {
            f.createNewFile()
        }

        return f
    }

    fun create(name: String): T {
        val constructor = themeClass.getDeclaredConstructor(String::class.java, ThemeManager::class.java)

        val theme = constructor.newInstance(name, this)
        theme.save()

        return theme
    }

    private fun loadSavedTheme(context: Context) {
        val prefs = context.getSettingsPreferences()

        val themeName = prefs.getString("selectedTheme", null)
        var themeLoaded = false

        if (themeName != null) {
            for (theme in themes) {
                if (theme.fileName == themeName) {
                    themeLoaded = true

                    selectedTheme = theme

                    break
                }
            }
        }

        if (!themeLoaded)
            selectedTheme = themes[0]
    }


    private fun loadThemes() {
        loadPredefinedThemes()
        loadUserCreatedThemes()
    }

    private fun loadPredefinedThemes() {
        themes.add(
            create(
                "Default"
            )
        )
        themes.add(
            create(
                "Rocket"
            )
        )
        themes.add(
            create(
                "IOS"
            )
        )
        themes.add(
            create(
                "Telegram"
            )
        )
    }

    private fun loadUserCreatedThemes() {

    }

    fun getCurrentTheme(): T {
        return selectedTheme!!
    }

    fun saveCurrent() {
        getCurrentTheme().save()
    }

    fun getStyle(key: String): Int {
        return getCurrentTheme().getStyle(key)
    }

    fun applyTheme(theme: T, context: Context) {
        selectedTheme = theme

        context.getSettingsPreferences().edit().putString("selectedTheme", theme.fileName.toString()).apply()

        notifyListeners(theme)
    }

    fun showColorPicker(
        forKey: String,
        context: Context,
        callback: (int: Int) -> Unit,
        onDismiss: (() -> Unit)? = null,
        onColorPickerStart: (() -> Unit)? = null,
        onColorPickerEnd: (() -> Unit)? = null
    ) {
        var picker: AlertDialog? = null

        val oldColor = getStyle(forKey)
        val listener = object : ColorEnvelopeListener {
            override fun colorPickerStart() {
                picker?.window?.setDimAmount(0f)
                picker?.window?.decorView?.apply {
                    alpha = 0.25f
                }

                onColorPickerStart?.invoke()
            }

            override fun colorPickerEnd() {
                picker?.window?.setDimAmount(0.6f)
                picker?.window?.decorView?.apply {
                    alpha = 1f
                }

                onColorPickerEnd?.invoke()
            }

            override fun onColorSelected(envelope: ColorEnvelope, fromUser: Boolean) {
                callback(envelope.color)
            }
        }

        picker = ColorPickerDialog.Builder(context)
            .setPreferenceName(null)
            .setTitle("ColorPicker Dialog")
            .setPositiveButton("Pick", listener)
            .setNegativeButton("Cancel") { dialog, _ ->
                callback(oldColor)
                dialog.dismiss()
            }
            .setOnDismissListener {
                onDismiss?.invoke()
            }
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .apply {
                colorPickerView.setColorListener(listener)
                colorPickerView.pureColor = oldColor
            }
            .show()
    }
}

fun Context.getSettingsPreferences(): SharedPreferences {
    return this.getSharedPreferences("settings", Context.MODE_PRIVATE)
}

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()