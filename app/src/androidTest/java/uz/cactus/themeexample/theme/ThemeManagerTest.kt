package uz.cactus.themeexample.theme

import androidx.test.InstrumentationRegistry
import org.junit.Test

class ThemeManagerTest {

    @Test
    fun load() {
        val appContext = InstrumentationRegistry.getTargetContext()

        github.adizbek.themeable.ThemeManager.init(appContext)
        val t = github.adizbek.themeable.ThemeManager.create("Rocket")
        t.save()
    }

}