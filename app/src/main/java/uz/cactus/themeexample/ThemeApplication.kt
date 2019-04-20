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

        registerActivityLifecycleCallbacks(themeManager);

        Drawables.loadResources(themeManager, this)
    }

    override fun onTerminate() {
        super.onTerminate()

        this.unregisterActivityLifecycleCallbacks(themeManager)
    }



    companion object {
        lateinit var themeManager: ThemeManager<Theme>
    }
}

fun toast(ctx: Context, msg: String) {
    Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
}
