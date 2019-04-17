package uz.cactus.themeexample

import android.app.Application
import android.content.Context
import android.widget.Toast
import uz.cactus.themeexample.theme.Drawables
import github.adizbek.themeable.ThemeManager

class ThemeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        github.adizbek.themeable.ThemeManager.init(this)
        Drawables.loadResources(this)
    }
}

fun toast(ctx: Context, msg: String) {
    Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
}
