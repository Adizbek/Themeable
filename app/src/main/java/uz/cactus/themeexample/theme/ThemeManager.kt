package uz.cactus.themeexample.theme

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class ThemeManager {

    fun update() {

    }

    fun remove() {

    }

    fun load() {

    }

    companion object {
        var themes = ArrayList<Theme>()
        var listeners = ArrayList<ThemeListener>()

        fun registerListener(listener: ThemeListener) {
            listeners.add(listener)
        }

        fun removeListener(listener: ThemeListener) {
            listeners.remove(listener)
        }

        private fun notifyListeners(key: String, color: Int) {
            for (listener in listeners) {
                listener.onStyleChanged(key, color)
            }
        }

        fun setStyle(key: String, color: Int) {
            getCurrentTheme().setStyle(key, color)

            notifyListeners(key, color)
        }

        lateinit var folder: File
        val ext: String = "rchatheme"

        fun getThemeFile(name: String): File {
            val f = File("${folder.absoluteFile}/$name.$ext")

            if (!f.exists()) {
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
        }

        private fun loadThemes() {
            themes.add(create("Default"))
        }

        fun getCurrentTheme(): Theme {
            return themes[0]
        }

        fun saveCurrent() {
            getCurrentTheme().save()
        }

        fun getStyle(key: String): Int {
            return getCurrentTheme().getStyle(key)
        }

    }
}

interface ThemeListener {
    fun onStyleChanged(key: String, color: Int)
}