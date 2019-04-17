package uz.cactus.themeexample

import android.app.Application
import android.content.Context
import android.widget.Toast
import uz.cactus.themeexample.theme.Drawables
import uz.cactus.themeexample.theme.ThemeManager

class ThemeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        ThemeManager.init(this)
        Drawables.loadResources(this)
    }
}

fun toast(ctx: Context, msg: String) {
    Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
}
