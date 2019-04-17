package uz.cactus.themeexample.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import github.adizbek.themeable.Drawables
import github.adizbek.themeable.Theme
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import uz.cactus.themeexample.R

class MainActivity : AppCompatActivity(), github.adizbek.themeable.ThemeListener {
    override fun bindStyles(): Array<github.adizbek.themeable.ThemeBinder> {
        return arrayOf(
            github.adizbek.themeable.ThemeBinder(
                toolbar,
                github.adizbek.themeable.ThemeBinder.Flag.COLOR,
                Theme.KEY_ACTION_BAR_TITLE_COLOR
            ),
            github.adizbek.themeable.ThemeBinder(
                toolbar,
                github.adizbek.themeable.ThemeBinder.Flag.BACKGROUND_COLOR,
                Theme.KEY_ACTION_BAR_BACKGROUND_COLOR
            ),
            github.adizbek.themeable.ThemeBinder(
                list,
                github.adizbek.themeable.ThemeBinder.Flag.BACKGROUND_COLOR,
                Theme.KEY_DIALOG_LIST_BACKGROUND
            ),
            github.adizbek.themeable.ThemeBinder(
                fab,
                github.adizbek.themeable.ThemeBinder.Flag.BACKGROUND_COLOR,
                Theme.KEY_STATUS_BAR_COLOR
            ),
            github.adizbek.themeable.ThemeBinder(Theme.KEY_STATUS_BAR_COLOR) { color ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = color
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setupList(list)

        github.adizbek.themeable.ThemeManager.applyStyles(this)

        fab.setOnClickListener { view ->
            startActivity(Intent(this, ThemeActivity::class.java))
        }
    }


    private fun setupList(list: RecyclerView) {
        list.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)

        list.layoutManager = layoutManager
        list.adapter = DialogAdapter(
            arrayListOf(
                "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe", "Adizbek",
                "John Doe",
                "Muhriddin"
            )
        )
    }


    override fun onPause() {
        github.adizbek.themeable.ThemeManager.registerListener(this)

        super.onPause()
    }

    override fun onResume() {
        github.adizbek.themeable.ThemeManager.registerListener(this)

        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}


class DialogAdapter(var data: ArrayList<String>) : RecyclerView.Adapter<DialogAdapter.VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = View.inflate(parent.context, R.layout.list_item_dialog, null)

        return VH(view)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.apply {
            val item = data[position]

            name.text = item
            text.text = item

            logo.setImageDrawable(Drawables.saveIcon)
            name.setCompoundDrawablesWithIntrinsicBounds(Drawables.personIcon, null, null, null)
        }

    }

    class VH(view: View) : RecyclerView.ViewHolder(view), github.adizbek.themeable.ThemeListener {
        override fun bindStyles(): Array<github.adizbek.themeable.ThemeBinder> {
            return arrayOf(
                github.adizbek.themeable.ThemeBinder(
                    name,
                    github.adizbek.themeable.ThemeBinder.Flag.COLOR,
                    Theme.KEY_DIALOG_LIST_TITLE_COLOR
                ),
                github.adizbek.themeable.ThemeBinder(
                    text,
                    github.adizbek.themeable.ThemeBinder.Flag.COLOR,
                    Theme.KEY_DIALOG_LIST_TEXT_COLOR
                )
            )
        }

        val name = view.findViewById<TextView>(R.id.name)
        val text = view.findViewById<TextView>(R.id.text)
        val logo = view.findViewById<ImageView>(R.id.logo)

        init {
            github.adizbek.themeable.ThemeManager.registerListener(this)
            github.adizbek.themeable.ThemeManager.applyStyles(this)
        }
    }

}