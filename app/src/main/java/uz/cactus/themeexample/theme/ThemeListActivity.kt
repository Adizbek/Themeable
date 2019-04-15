package uz.cactus.themeexample.theme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_theme_list.*
import uz.cactus.themeexample.R

class ThemeListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_list)

        setSupportActionBar(toolbar)
    }
}
