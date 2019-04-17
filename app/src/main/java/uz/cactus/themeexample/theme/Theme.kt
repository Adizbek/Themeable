package uz.cactus.themeexample.theme

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import github.adizbek.themeable.ThemeBinder
import github.adizbek.themeable.ThemeInterface
import github.adizbek.themeable.ThemeListener
import github.adizbek.themeable.ThemeManager
import uz.cactus.themeexample.R


class Theme(val name: String, val manager: ThemeManager<Theme>) : ThemeInterface(name, manager) {
    override val themeValues: HashMap<String, Int>
        get() = hashMapOf(
            Pair(KEY_ACTION_BAR_BACKGROUND_COLOR, Color.parseColor("#ffffff")),
            Pair(KEY_ACTION_BAR_TITLE_COLOR, Color.parseColor("#000000")),
            Pair(KEY_STATUS_BAR_COLOR, Color.parseColor("#ffffff")),

            Pair(KEY_DIALOG_LIST_ICON, Color.parseColor("#000000")),
            Pair(KEY_DIALOG_LIST_TITLE_COLOR, Color.parseColor("#000000")),
            Pair(KEY_DIALOG_LIST_TEXT_COLOR, Color.parseColor("#000000")),
            Pair(KEY_DIALOG_LIST_BACKGROUND, Color.parseColor("#ffffff"))
        )

    companion object {
        val KEY_ACTION_BAR_BACKGROUND_COLOR = "ActionBarBackgroundColor"
        val KEY_ACTION_BAR_TITLE_COLOR = "ActionBarTitleColor"
        val KEY_STATUS_BAR_COLOR = "StatusBarColor"
        val KEY_DIALOG_LIST_ICON = "DialogListIcon"
        val KEY_DIALOG_LIST_TITLE_COLOR = "DialogListTitleColor"
        val KEY_DIALOG_LIST_TEXT_COLOR = "DialogListTextColor"
        val KEY_DIALOG_LIST_BACKGROUND = "DialogListBackground"
    }
}

object Drawables : ThemeListener {
    override fun bindStyles(): Array<ThemeBinder> {
        return arrayOf(
            ThemeBinder(
                saveIcon,
                Theme.KEY_STATUS_BAR_COLOR
            ),
            ThemeBinder(
                addIcon,
                Theme.KEY_STATUS_BAR_COLOR
            ),
            ThemeBinder(
                personIcon,
                Theme.KEY_DIALOG_LIST_ICON
            )
        )
    }

    lateinit var saveIcon: Drawable
    lateinit var addIcon: Drawable
    lateinit var personIcon: Drawable

    fun loadResources(manager: ThemeManager<Theme>, context: Context) {
        val rs = context.resources

        saveIcon = rs.getDrawable(R.drawable.ic_save_white_24dp)
        addIcon = rs.getDrawable(R.drawable.ic_add_alert_white_24dp)
        personIcon = rs.getDrawable(R.drawable.ic_account_circle_white_18dp)

        manager.registerListener(this)
        manager.applyStyles(this)
    }
}
