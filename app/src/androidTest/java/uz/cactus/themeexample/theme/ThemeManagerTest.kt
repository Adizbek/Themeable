package uz.cactus.themeexample.theme

import android.app.Application
import androidx.test.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test

class ThemeManagerTest {

    @Test
    fun load() {
        val appContext = InstrumentationRegistry.getTargetContext()

        ThemeManager.init(appContext)
        val t = ThemeManager.create("Rocket")
        t.save()
    }

}