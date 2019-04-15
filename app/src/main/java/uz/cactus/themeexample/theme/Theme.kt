package uz.cactus.themeexample.theme

import android.graphics.Color
import org.json.JSONObject


class Theme(name: String) {

    var values: HashMap<String, Int> = hashMapOf()
    var theme = ThemeManager.getThemeFile(name)

    private fun load() {
        val content = theme.inputStream().bufferedReader().readText().split("\n")

        val styles: HashMap<String, Int> = hashMapOf()

        for (style in content) {
            style.split("=")
                .takeIf { it.count() == 2 }
                ?.apply {
                    styles[this[0]] = this[1].toInt()
                }
        }

        for (item in Companion.values) {
            try {
                values[item.key] = styles[item.key]!!
            } catch (e: Exception) {
                values[item.key] = item.value
            }
        }
    }

    fun save() {
        try {
            val writer = theme.outputStream().writer()

            for (item in values) {
                writer.write("${item.key}=${item.value}\n")
            }

            writer.close()
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }

    fun getStyle(key: String): Int {
        try {
            return this.values[key]!!
        } catch (e: Exception) {
            return Color.BLACK
        }
    }

    fun setStyle(key: String, color: Int) {
        this.values[key] = color
    }

    companion object {
        val KEY_ACTION_BAR_BACKGROUND_COLOR = "ActionBarBackgroundColor"
        val KEY_ACTION_BAR_TITLE_COLOR = "ActionBarTitleColor"
        val KEY_STATUS_BAR_COLOR = "StatusBarColor"

        val values: HashMap<String, Int> = hashMapOf(
            Pair(KEY_ACTION_BAR_BACKGROUND_COLOR, Color.parseColor("#ffffff")),
            Pair(KEY_ACTION_BAR_TITLE_COLOR, Color.parseColor("#000000")),
            Pair(KEY_STATUS_BAR_COLOR, Color.parseColor("#ffffff"))
        )
    }

    init {
        this.load()
    }
}