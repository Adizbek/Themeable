package uz.cactus.themeexample.ui

import android.content.Context
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
import kotlinx.android.synthetic.main.activity_theme_list.*
import uz.cactus.themeexample.R
import uz.cactus.themeexample.ThemeApplication
import uz.cactus.themeexample.theme.Theme
import uz.cactus.themeexample.toast

class ThemeListActivity : AppCompatActivity(), ThemeListener {

    private var addMenu: MenuItem? = null

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

            ThemeBinder(
                null,
                null,
                Theme.KEY_STATUS_BAR_COLOR
            ) { color ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = color
                }
            }
        )
    }

    override fun onResume() {
        ThemeApplication.themeManager.registerListener(this)

        super.onResume()
    }

    override fun onPause() {
        ThemeApplication.themeManager.removeListener(this)

        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, 0, 0, "Add new").apply {
            icon = resources.getDrawable(R.drawable.ic_add_alert_white_24dp)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

            addMenu = this
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            0 -> {
                showCreateThemeDialog()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showCreateThemeDialog() {
        toast(this, "Add")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_list)
        setSupportActionBar(toolbar)

        setupThemesList()

        ThemeApplication.themeManager.applyStyles(this)
    }

    fun setupThemesList() {
        list.adapter = ThemeListAdapter(
            this,
            R.layout.list_item_theme_style,
            R.id.key,
            ThemeApplication.themeManager.themes
        )

        list.setOnItemClickListener { parent, view, position, id ->
            val theme = parent.adapter.getItem(position) as Theme

            ThemeApplication.themeManager.applyTheme(theme, this)
        }
    }

    class ThemeListAdapter(context: Context, resource: Int, textViewResourceId: Int, objects: MutableList<Theme>) :
        ArrayAdapter<Theme>(context, resource, textViewResourceId, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)

            val theme: Theme = getItem(position)!!

            view.findViewById<TextView>(R.id.key).text = theme.fileName
            view.findViewById<ImageView>(R.id.color)
                .setBackgroundColor(theme.getStyle(Theme.KEY_ACTION_BAR_BACKGROUND_COLOR))

            return view
        }
    }
}
