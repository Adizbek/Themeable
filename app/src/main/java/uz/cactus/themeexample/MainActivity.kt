package uz.cactus.themeexample

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import uz.cactus.themeexample.theme.Theme
import uz.cactus.themeexample.theme.ThemeActivity
import uz.cactus.themeexample.theme.ThemeListener
import uz.cactus.themeexample.theme.ThemeManager

class MainActivity : AppCompatActivity(), ThemeListener {
    override fun onStyleChanged(key: String, color: Int) {
        if (key == Theme.KEY_ACTION_BAR_BACKGROUND_COLOR) {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
        } else if (key == Theme.KEY_ACTION_BAR_TITLE_COLOR) {
            toolbar.setTitleTextColor(ThemeManager.getStyle(Theme.KEY_ACTION_BAR_TITLE_COLOR))
        } else if (key == Theme.KEY_STATUS_BAR_COLOR) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = color
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(ThemeManager.getStyle(Theme.KEY_ACTION_BAR_BACKGROUND_COLOR)))
        toolbar.setTitleTextColor(ThemeManager.getStyle(Theme.KEY_ACTION_BAR_TITLE_COLOR))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ThemeManager.getStyle(Theme.KEY_STATUS_BAR_COLOR)
        }


        fab.setOnClickListener { view ->
            startActivity(Intent(this, ThemeActivity::class.java))
        }
    }


    override fun onPause() {
        ThemeManager.registerListener(this)

        super.onPause()
    }

    override fun onResume() {
        ThemeManager.registerListener(this)

        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
