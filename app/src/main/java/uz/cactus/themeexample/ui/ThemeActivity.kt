package uz.cactus.themeexample.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import github.adizbek.themeable.ThemeBinder
import github.adizbek.themeable.ThemeListener
import kotlinx.android.synthetic.main.activity_theme.*
import uz.cactus.themeexample.R
import uz.cactus.themeexample.R.id.*
import uz.cactus.themeexample.ThemeApplication
import uz.cactus.themeexample.theme.Theme
import uz.cactus.themeexample.toast


class ThemeActivity : AppCompatActivity(), ThemeListener {
    override fun bindStyles(): Array<ThemeBinder> {
        return arrayOf(
            ThemeBinder(
                toolbar,
                ThemeBinder.Flag.COLOR,
                Theme.KEY_ACTION_BAR_TITLE_COLOR
            ),
            ThemeBinder(
                toolbar,
                ThemeBinder.Flag.BACKGROUND_COLOR,
                Theme.KEY_ACTION_BAR_BACKGROUND_COLOR
            ),
            ThemeBinder(key = Theme.KEY_STATUS_BAR_COLOR) { color ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = color
                }
            }
        )
    }

    var theme = ThemeApplication.themeManager.getCurrentTheme()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(uz.cactus.themeexample.R.layout.activity_theme)
        setSupportActionBar(toolbar)

        ThemeApplication.themeManager.applyStyles(this)

        loadStyles()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(uz.cactus.themeexample.R.menu.menu_themes_activity, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            menu_action_select_theme -> {
                showThemesList()
            }

            menu_action_save_theme -> {
                ThemeApplication.themeManager.saveCurrent()
                toast(this, "Theme saved")
            }

            menu_action_edit_theme -> {
                ThemeApplication.themeManager.toggleThemeEditor()
            }
        }


        return super.onOptionsItemSelected(item)
    }

    private fun showThemesList() {
        startActivity(Intent(this, ThemeListActivity::class.java))
    }

    private fun loadStyles() {
        list.adapter = ThemeStyleAdapter(
            this,
            R.layout.list_item_theme_style,
            R.id.key,
            theme
        )

        list.setOnItemClickListener { parent, view, position, id ->
            val key = parent.getItemAtPosition(position) as String

            showColorPicker(key) { color ->
                view.findViewById<ImageView>(uz.cactus.themeexample.R.id.color).setBackgroundColor(color)

                ThemeApplication.themeManager.setStyle(key, color)
            }
        }
    }


    override fun onNewThemeApplied() {
        theme = ThemeApplication.themeManager.getCurrentTheme()

        loadStyles()
    }

    private fun showColorPicker(forKey: String, callback: (color: Int) -> Unit) {
        ThemeApplication.themeManager.showColorPicker(forKey, this, callback)
    }

    override fun onPause() {
        ThemeApplication.themeManager.removeListener(this)

        super.onPause()
    }

    override fun onResume() {
        ThemeApplication.themeManager.registerListener(this)

        super.onResume()
    }
}

class ThemeStyleAdapter(var context: ThemeActivity, var resource: Int, var text: Int, var theme: Theme) :
    ArrayAdapter<String>(
        context,
        resource,
        text,
        theme.values.keys.toList()
    ) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        val key = getItem(position)

        view.findViewById<TextView>(uz.cactus.themeexample.R.id.key).text = key
        view.findViewById<ImageView>(uz.cactus.themeexample.R.id.color).setBackgroundColor(theme.getStyle(key))

        return view
    }
}
