# The development of **Themeable** has been started on 12-april 2019 during [GSOC 2019](http://summerofcode.withgoogle.com) , special for [Rocket.Chat Android](https://github.com/RocketChat/Rocket.Chat.Android)

![Rocket.Chat Android](https://raw.githubusercontent.com/RocketChat/Rocket.Chat.Artwork/master/Logos/logo-dark.svg?sanitize=true)



## HOW TO INSTALL
Include jitpack to your repositories
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the [themeable](https://github.com/Adizbek/Themeable) to your 
project

Latets version on Jitpack 
[![](https://jitpack.io/v/Adizbek/Themeable.svg)](https://jitpack.io/#Adizbek/Themeable)

```gradle
dependencies {
        implementation 'com.github.Adizbek:Themeable:${themeable.version}'
}
```

## HOW TO USE
First create our theme. Theme.java
```java
class Theme(val name: String, val manager: ThemeManager<Theme>) : ThemeInterface(name, manager) {
    // default values for styles
    override val themeValues: HashMap<String, Int>
        get() = hashMapOf(
            Pair(KEY_ACTION_BAR_BACKGROUND_COLOR, Color.parseColor("#ffffff")),
            Pair(KEY_ACTION_BAR_TITLE_COLOR, Color.parseColor("#000000")),
            Pair(KEY_STATUS_BAR_COLOR, Color.parseColor("#ffffff")),

            Pair(KEY_DIALOG_LIST_ICON, Color.parseColor("#000000")),
            Pair(KEY_DIALOG_LIST_TITLE_COLOR, Color.parseColor("#000000")),
            Pair(KEY_DIALOG_LIST_TEXT_COLOR, Color.parseColor("#000000")),
            Pair(KEY_DIALOG_LIST_BACKGROUND, Color.parseColor("#ffffff"))
        )

    // we define all style names here 
    companion object {
        val KEY_ACTION_BAR_BACKGROUND_COLOR = "ActionBarBackgroundColor"
        val KEY_ACTION_BAR_TITLE_COLOR = "ActionBarTitleColor"
        val KEY_STATUS_BAR_COLOR = "StatusBarColor"
        val KEY_DIALOG_LIST_ICON = "DialogListIcon"
        val KEY_DIALOG_LIST_TITLE_COLOR = "DialogListTitleColor"
        val KEY_DIALOG_LIST_TEXT_COLOR = "DialogListTextColor"
        val KEY_DIALOG_LIST_BACKGROUND = "DialogListBackground"
    }
}

// example of ThemeListener interface
object Drawables : ThemeListener {
    override fun bindStyles(): Array<ThemeBinder> {
        return arrayOf(
            ThemeBinder(
                drawable = saveIcon,
                key = Theme.KEY_STATUS_BAR_COLOR
            ),
            ThemeBinder(
                drawable = addIcon,
                key = Theme.KEY_STATUS_BAR_COLOR
            ),
            ThemeBinder(
                drawable = personIcon,
                key = Theme.KEY_DIALOG_LIST_ICON
            )
        )
    }

    lateinit var saveIcon: Drawable
    lateinit var addIcon: Drawable
    lateinit var personIcon: Drawable

    fun loadResources(manager: ThemeManager<Theme>, context: Context) {
        val rs = context.resources

        saveIcon = rs.getDrawable(R.drawable.ic_save_white_24dp)
        addIcon = rs.getDrawable(R.drawable.ic_add_alert_white_24dp)
        personIcon = rs.getDrawable(R.drawable.ic_account_circle_white_18dp)

        manager.registerListener(this)
        manager.applyStyles(this)
    }
}

```

Setup theme manager in our application

```java
class ThemeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        themeManager = ThemeManager(
            this, // context
            Theme::class.java, // theme class which we created, all defined styles will be here
            fileExtension = "rchatheme" // theme file extension, this will be used while save/loading theme files from filesystem
        )

        registerActivityLifecycleCallbacks(themeManager); // register listener for activity lifecycle

        Drawables.loadResources(themeManager, this)
    }

    override fun onTerminate() {
        super.onTerminate()

        this.unregisterActivityLifecycleCallbacks(themeManager) // unregister listener for activity lifecycle
    }


    companion object {
        lateinit var themeManager: ThemeManager<Theme> // you can access theme manager from everywhere
    }
}
```

## HOW TO USE IN ACTIVITY
```java
class MainActivity : AppCompatActivity(), ThemeListener {
    
    //bind our style to views
    override fun bindStyles(): Array<ThemeBinder> {
        return arrayOf(
            // binds Theme.KEY_ACTION_BAR_TITLE_COLOR to toolbars text color
            ThemeBinder( 
                toolbar,
                ThemeBinder.Flag.COLOR,
                Theme.KEY_ACTION_BAR_TITLE_COLOR
            ),
            // binds Theme.KEY_ACTION_BAR_BACKGROUND_COLOR to toolbars background color
            ThemeBinder(
                toolbar,
                ThemeBinder.Flag.BACKGROUND_COLOR,
                Theme.KEY_ACTION_BAR_BACKGROUND_COLOR
            ),
            // binds Theme.KEY_DIALOG_LIST_BACKGROUND to list background color
            ThemeBinder(
                list, // RecyclerView
                ThemeBinder.Flag.BACKGROUND_COLOR,
                Theme.KEY_DIALOG_LIST_BACKGROUND
            ),
            // binds Theme.KEY_STATUS_BAR_COLOR to fab background color
            ThemeBinder(
                fab, // FloatingActiveButton
                ThemeBinder.Flag.BACKGROUND_COLOR,
                key = Theme.KEY_STATUS_BAR_COLOR
            ),
            // just listen for Theme.KEY_STATUS_BAR_COLOR
            ThemeBinder(key = Theme.KEY_STATUS_BAR_COLOR) { color ->
                // your logic 😏, very dynamic isn't?
            
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = color
                }
            }
        )
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      
        ...
        
        ThemeApplication.themeManager.applyStyles(this)
    }
    
    
    override fun onResume() {
        // We need to define theme listener in order to be aware about theme/style changing
        ThemeApplication.themeManager.registerListener(this)

        super.onResume()
    }

    override fun onPause() {
        ThemeApplication.themeManager.removeListener(this)

        super.onPause()
    }
}
```

#### HOW TO USE IN VIEWHOLDER
[Example](https://github.com/Adizbek/Themeable/blob/master/app/src/main/java/uz/cactus/themeexample/ui/MainActivity.kt)


#### THE POWER OF THEMEABLE
Themeable is very dynamic and flexible. You can style every thing in
your app and has internal theme editor.

#### Ways of style binding 

```java
ThemeBinder( // binds key to view's text color
    view,
    ThemeBinder.Flag.COLOR,
    key
)

ThemeBinder( // condition based key
    view,
    ThemeBinder.Flag.COLOR,
    keyProvider = {
        return@ThemeBinder if (System.currentTimeMillis() % 2 == 0L) Theme.KEY_DIALOG_LIST_TITLE_COLOR
        else Theme.KEY_DIALOG_LIST_TEXT_COLOR
    }
)

// custom logic
ThemeBinder(key = Theme.KEY_STATUS_BAR_COLOR) { color ->
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = color
    }
}
```