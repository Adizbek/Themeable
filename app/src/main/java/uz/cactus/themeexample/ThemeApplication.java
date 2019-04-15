package uz.cactus.themeexample;

import android.app.Application;
import uz.cactus.themeexample.theme.ThemeManager;

public class ThemeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ThemeManager.Companion.init(this);
    }
}
