package uz.cactus.themeexample.theme

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.android.synthetic.main.activity_theme.*
import uz.cactus.themeexample.R


class ThemeActivity : AppCompatActivity(), ThemeListener {
    override fun onStyleChanged(key: String, color: Int) {
        if (key == Theme.KEY_ACTION_BAR_BACKGROUND_COLOR) {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
        } else if (key == Theme.KEY_ACTION_BAR_TITLE_COLOR) {
            toolbar.setTitleTextColor(color)
        } else if (key == Theme.KEY_STATUS_BAR_COLOR) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = color
            }
        }
    }

    var theme = ThemeManager.getCurrentTheme()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(uz.cactus.themeexample.R.layout.activity_theme)
        setSupportActionBar(toolbar)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(ThemeManager.getStyle(Theme.KEY_ACTION_BAR_BACKGROUND_COLOR)))
        toolbar.setTitleTextColor(ThemeManager.getStyle(Theme.KEY_ACTION_BAR_TITLE_COLOR))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ThemeManager.getStyle(Theme.KEY_STATUS_BAR_COLOR)
        }


        loadStyles()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_themes_activity, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.menu_action_select_theme -> {
                showThemesList()
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
            uz.cactus.themeexample.R.layout.list_item_theme_style,
            uz.cactus.themeexample.R.id.key,
            theme
        )

        list.setOnItemClickListener { parent, view, position, id ->
            val key = parent.getItemAtPosition(position) as String

            showColorPicker(key) { color ->
                view.findViewById<ImageView>(R.id.color).setBackgroundColor(color)

                ThemeManager.setStyle(key, color)
            }
        }
    }

    private fun showColorPicker(forKey: String, callback: (color: Int) -> Unit) {
        val oldColor = ThemeManager.getStyle(forKey)

        ColorPickerDialog.Builder(this)
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton("Pick",
                ColorEnvelopeListener { envelope, fromUser ->
                    callback(envelope.color)
                })
            .setNegativeButton("Cancel") { dialog, _ ->
                callback(oldColor)
                dialog.dismiss()
            }
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .apply {
                colorPickerView.setColorListener(ColorEnvelopeListener { envelope, fromUser ->
                    callback(envelope.color)
                })
            }
            .show()

    }

    override fun onResume() {
        super.onResume()

        ThemeManager.registerListener(this)
    }

    override fun onPause() {
        ThemeManager.removeListener(this)

        super.onPause()
    }

    override fun onDestroy() {
        ThemeManager.saveCurrent()

        super.onDestroy()
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
