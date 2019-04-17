package uz.cactus.themeexample.theme

import android.content.Context
import android.content.SharedPreferences
import java.io.File

class ThemeManager {

    fun update() {

    }

    fun remove() {

    }

    fun load() {

    }

    companion object {
        lateinit var folder: File
        val ext: String = "rchatheme"

        var themes = ArrayList<Theme>()

        var selectedTheme: Theme? = null

        var listeners = ArrayList<ThemeListener>()

        fun registerListener(listener: ThemeListener) {
            listeners.add(listener)
        }

        fun removeListener(listener: ThemeListener) {
            listeners.remove(listener)
        }

        private fun notifyListeners(key: String, color: Int) {
            for (listener in listeners) {
                for (bindStyle in listener.bindStyles()) {
                    if (bindStyle.key == key) {
                        bindStyle.processColor(color)

                        listener.boundStyle(key, color)
                    }
                }

                listener.onNewThemeApplied()
            }
        }

        private fun notifyListeners(theme: Theme) {
            for (key in Theme.values.keys) {
                notifyListeners(key, theme.getStyle(key))
            }
        }

        fun applyStyles(themeListener: ThemeListener) {
            val styles = getCurrentTheme().values

            for (binder in themeListener.bindStyles()) {

                if (styles.containsKey(binder.key)) {
                    binder.processColor(styles[binder.key]!!)
                }

            }
        }

        fun setStyle(key: String, color: Int) {
            getCurrentTheme().setStyle(key, color)

            notifyListeners(key, color)
        }


        fun getThemeFile(name: String, create: Boolean = true): File {
            val f = File("${folder.absoluteFile}/$name.$ext")

            if (!f.exists() && create) {
                f.createNewFile()
            }

            return f
        }

        fun create(name: String): Theme {
            val theme = Theme(name)

            theme.save()

            return theme
        }

        fun init(context: Context) {
            folder = context.getDir("themes", Context.MODE_PRIVATE)

            loadThemes()
            loadSavedTheme(context)
        }

        private fun loadSavedTheme(context: Context) {
            val prefs = context.getSettingsPreferences()

            val themeName = prefs.getString("selectedTheme", null)
            var themeLoaded = false

            if (themeName != null) {
                for (theme in themes) {
                    if (theme.name == themeName) {
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
            themes.add(create("Default"))
            themes.add(create("Rocket"))
            themes.add(create("IOS"))
            themes.add(create("Telegram"))
        }

        private fun loadUserCreatedThemes() {

        }

        fun getCurrentTheme(): Theme {
            return selectedTheme!!
        }

        fun saveCurrent() {
            getCurrentTheme().save()
        }

        fun getStyle(key: String): Int {
            return getCurrentTheme().getStyle(key)
        }

        fun applyTheme(theme: Theme, context: Context) {
            selectedTheme = theme

            context.getSettingsPreferences().edit().putString("selectedTheme", theme.name.toString()).apply()

            notifyListeners(theme)
        }
    }
}

interface ThemeListener {
    fun bindStyles(): Array<ThemeBinder>

    fun onNewThemeApplied() {}

    fun boundStyle(key: String, color: Int) {}
}

fun Context.getSettingsPreferences(): SharedPreferences {
    return this.getSharedPreferences("settings", Context.MODE_PRIVATE)
}