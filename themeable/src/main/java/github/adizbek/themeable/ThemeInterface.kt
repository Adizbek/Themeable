package github.adizbek.themeable

import android.graphics.Color

abstract class ThemeInterface(name: String, manager: ThemeManager<*>) {
    var file = manager.getThemeFile(name)
    val fileName: CharSequence = file.name

    /**
     * All defined styles should be added here,
     * String is key, Int for default value
     */
    abstract val themeValues: HashMap<String, Int>

    /**
     * Theme file styles. It will be populated from file (+ themeValues when can't find keys)
     */
    val values: HashMap<String, Int> = hashMapOf()


    private fun load() {
        val content = file.inputStream().bufferedReader().readText().split("\n")

        val styles: HashMap<String, Int> = hashMapOf()

        for (style in content) {
            style.split("=")
                .takeIf { it.count() == 2 }
                ?.apply {
                    styles[this[0]] = this[1].toInt()
                }
        }

        for (item in themeValues) {
            try {
                this.values[item.key] = styles[item.key]!!
            } catch (e: Exception) {
                this.values[item.key] = item.value
            }
        }
    }

    fun save() {
        try {
            val writer = file.outputStream().writer()

            for (item in values) {
                writer.write("${item.key}=${item.value}\n")
            }

            writer.close()
        } catch (e: Exception) {
            print(e.localizedMessage)
        }
    }

    fun getStyle(key: String): Int {
        return try {
            this.values[key]!!
        } catch (e: Exception) {
            Color.BLACK
        }
    }

    fun setStyle(key: String, color: Int) {
        this.values[key] = color
    }

    init {
        this.load()
    }
}

//fun Int.toPx(): Float = (this * Resources.getSystem().displayMetrics.density)
//
//fun Int.toDp(): Float = (this / Resources.getSystem().displayMetrics.density)