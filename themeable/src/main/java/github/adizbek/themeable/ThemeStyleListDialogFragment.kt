package github.adizbek.themeable

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_style_list_dialog.*
import kotlinx.android.synthetic.main.fragment_style_list_dialog_item.view.*

class ThemeStyleListDialogFragment : BottomSheetDialogFragment() {
    private lateinit var themeManager: ThemeManager<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_style_list_dialog, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setDimAmount(0f)
        }
    }

    override fun onStart() {
        super.onStart()

        themeManager.hideThemeEditor()
        themeManager.styleList = this
    }


    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)

        themeManager.saveCurrent()
        themeManager.styleList = null
        themeManager.showThemeEditor()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = ItemAdapter(view, themeManager.getCurrentTheme())
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_style_list_dialog_item, parent, false)) {

        internal val text: TextView = itemView.text
        internal val color: ImageView = itemView.color

    }

    private inner class ItemAdapter internal constructor(
        private val parentList: View,
        private val theme: ThemeInterface
    ) :
        RecyclerView.Adapter<ViewHolder>() {

        val keys = theme.values.keys.toList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder) {
                val key = keys[position]

                text.text = key
                color.setImageDrawable(ColorDrawable(theme.getStyle(key)))

                itemView.setOnClickListener {
                    parentList.visibility = View.GONE

                    themeManager.showColorPicker(key, itemView.context, {
                        color.setImageDrawable(ColorDrawable(it))
                        themeManager.setStyle(key, it)
                    }, {
                        parentList.visibility = View.VISIBLE
                    })
                }
            }
        }

        override fun getItemCount(): Int {
            return keys.size
        }
    }


    companion object {
        fun newInstance(themeManager: ThemeManager<*>): ThemeStyleListDialogFragment =
            ThemeStyleListDialogFragment().apply {
                this.themeManager = themeManager
            }
    }
}
