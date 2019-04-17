package github.adizbek.themeable

interface ThemeListener {
    fun bindStyles(): Array<ThemeBinder>

    fun onNewThemeApplied() {}

    fun boundStyle(key: String, color: Int) {}
}