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
import github.adizbek.themeable.ThemeBinder
import github.adizbek.themeable.ThemeListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import uz.cactus.themeexample.R
import uz.cactus.themeexample.ThemeApplication
import uz.cactus.themeexample.theme.Drawables
import uz.cactus.themeexample.theme.Theme

class MainActivity : AppCompatActivity(), ThemeListener {
    override fun bindStyles(): Array<ThemeBinder> {
        return arrayOf(
            ThemeBinder(
                toolbar,
                ThemeBinder.Flag.COLOR,
                Theme.KEY_ACTION_BAR_TITLE_COLOR
            ),
            ThemeBinder(
                toolbar,
                ThemeBinder.Flag.BACKGROUND_COLOR,
                Theme.KEY_ACTION_BAR_BACKGROUND_COLOR
            ),
            ThemeBinder(
                list,
                ThemeBinder.Flag.BACKGROUND_COLOR,
                Theme.KEY_DIALOG_LIST_BACKGROUND
            ),
            ThemeBinder(
                fab,
                ThemeBinder.Flag.BACKGROUND_COLOR,
                key = Theme.KEY_STATUS_BAR_COLOR
            ),
            ThemeBinder(key = Theme.KEY_STATUS_BAR_COLOR) { color ->
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

        ThemeApplication.themeManager.applyStyles(this)

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


    override fun onResume() {
        ThemeApplication.themeManager.registerListener(this)

        super.onResume()
    }

    override fun onPause() {
        ThemeApplication.themeManager.removeListener(this)

        super.onPause()
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

    override fun onViewRecycled(holder: VH) {
        ThemeApplication.themeManager.removeListener(holder)

        super.onViewRecycled(holder)
    }

    class VH(view: View) : RecyclerView.ViewHolder(view), ThemeListener {

        override fun bindStyles(): Array<ThemeBinder> {
            return arrayOf(
                ThemeBinder(
                    name,
                    ThemeBinder.Flag.COLOR,
                    keyProvider = {
                        return@ThemeBinder if (System.currentTimeMillis() % 2 == 0L) Theme.KEY_DIALOG_LIST_TITLE_COLOR
                        else Theme.KEY_DIALOG_LIST_TEXT_COLOR
                    }
                ),
                ThemeBinder(
                    text,
                    ThemeBinder.Flag.COLOR,
                    Theme.KEY_DIALOG_LIST_TEXT_COLOR
                ),
                ThemeBinder(
                    name, ThemeBinder.Flag.COMPOUND_ICON, key = Theme.KEY_DIALOG_LIST_ICON
                )
            )
        }

        val name = view.findViewById<TextView>(R.id.name)
        val text = view.findViewById<TextView>(R.id.text)
        val logo = view.findViewById<ImageView>(R.id.logo)

        init {
            ThemeApplication.themeManager.registerListener(this)
            ThemeApplication.themeManager.applyStyles(this)
        }
    }

}