package uz.cactus.themeexample.theme

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import uz.cactus.themeexample.R


class Theme(name: String) {

    var values: HashMap<String, Int> = hashMapOf()
    var theme = ThemeManager.getThemeFile(name)
    val name: CharSequence? = theme.name

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
        val KEY_DIALOG_LIST_ICON = "DialogListIcon"
        val KEY_DIALOG_LIST_TITLE_COLOR = "DialogListTitleColor"
        val KEY_DIALOG_LIST_TEXT_COLOR = "DialogListTextColor"
        val KEY_DIALOG_LIST_BACKGROUND = "DialogListBackground"

        val values: HashMap<String, Int> = hashMapOf(
            Pair(KEY_ACTION_BAR_BACKGROUND_COLOR, Color.parseColor("#ffffff")),
            Pair(KEY_ACTION_BAR_TITLE_COLOR, Color.parseColor("#000000")),
            Pair(KEY_STATUS_BAR_COLOR, Color.parseColor("#ffffff")),

            Pair(KEY_DIALOG_LIST_ICON, Color.parseColor("#000000")),
            Pair(KEY_DIALOG_LIST_TITLE_COLOR, Color.parseColor("#000000")),
            Pair(KEY_DIALOG_LIST_TEXT_COLOR, Color.parseColor("#000000")),
            Pair(KEY_DIALOG_LIST_BACKGROUND, Color.parseColor("#ffffff"))
        )
    }

    init {
        this.load()

    }


}

object Drawables : ThemeListener {
    override fun bindStyles(): Array<ThemeBinder> {
        return arrayOf(
            ThemeBinder(saveIcon, Theme.KEY_STATUS_BAR_COLOR),
            ThemeBinder(addIcon, Theme.KEY_STATUS_BAR_COLOR),
            ThemeBinder(personIcon, Theme.KEY_DIALOG_LIST_ICON)
        )
    }

    lateinit var saveIcon: Drawable
    lateinit var addIcon: Drawable
    lateinit var personIcon: Drawable

    fun loadResources(context: Context) {
        val rs = context.resources

        saveIcon = rs.getDrawable(R.drawable.ic_save_white_24dp)
        addIcon = rs.getDrawable(R.drawable.ic_add_alert_white_24dp)
        personIcon = rs.getDrawable(R.drawable.ic_account_circle_white_18dp)


        ThemeManager.registerListener(this)
        ThemeManager.applyStyles(this)
    }

}

fun Int.toPx(): Float = (this * Resources.getSystem().displayMetrics.density)

fun Int.toDp(): Float = (this / Resources.getSystem().displayMetrics.density)