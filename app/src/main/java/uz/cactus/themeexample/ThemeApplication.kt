package uz.cactus.themeexample

import android.app.Application
import android.content.Context
import android.widget.Toast
import github.adizbek.themeable.ThemeManager
import uz.cactus.themeexample.theme.Drawables
import uz.cactus.themeexample.theme.Theme

class ThemeApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        themeManager = ThemeManager(
            this,
            Theme::class.java,
            fileExtension = "rchatheme"
        )


        Drawables.loadResources(themeManager, this)
    }

    companion object {
        lateinit var themeManager: ThemeManager<Theme>
    }
}

fun toast(ctx: Context, msg: String) {
    Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
}
