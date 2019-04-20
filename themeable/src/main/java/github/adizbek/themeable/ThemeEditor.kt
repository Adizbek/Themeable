package github.adizbek.themeable

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ThemeEditor(var manager: ThemeManager<*>) {

    private val editorWidth = 54.toPx()
    private val editorHeight = 54.toPx()
    private var windowView: FloatingActionButton? = null

    private var windowLayoutParams: WindowManager.LayoutParams? = null
    private var wManager: WindowManager? = null

    init {
        instance = this
    }

    fun show(): Unit {
        if (windowView != null || styleListDialogFragment != null)
            return

        activity?.apply {
            windowView = FloatingActionButton(activity).apply {
                layoutParams = ViewGroup.LayoutParams(editorWidth, editorHeight)
                setImageResource(R.drawable.ic_palette_white_24dp)

                setOnClickListener {
                    ThemeStyleListDialogFragment.newInstance(manager).show(activity?.supportFragmentManager, null)
                }
            }

            wManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager?

            windowLayoutParams = WindowManager.LayoutParams().apply {
                width = editorWidth
                height = editorHeight
                x = editorWidth + 200
                y = editorHeight + 200
                format = PixelFormat.TRANSLUCENT
                gravity = Gravity.TOP or Gravity.LEFT
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                flags =
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            }

            wManager?.addView(windowView, windowLayoutParams)
        }

    }

    fun hide() {
        windowView?.let {
            wManager?.removeView(it)
        }

        windowView = null
    }

    companion object {
        var activity: AppCompatActivity? = null
        var styleListDialogFragment: ThemeStyleListDialogFragment? = null
        var instance: ThemeEditor? = null
    }
}