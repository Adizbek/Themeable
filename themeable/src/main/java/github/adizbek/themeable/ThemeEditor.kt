package github.adizbek.themeable

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class ThemeEditor {

    private val editorWidth = 54.toPx()
    private val editorHeight = 54.toPx()

    private var windowLayoutParams: WindowManager.LayoutParams? = null
    private var windowManager: WindowManager? = null

    constructor(context: Context, manager: ThemeManager<*>) {
        var windowView = ImageView(context)
        windowView.layoutParams = ViewGroup.LayoutParams(editorWidth, editorHeight)
        windowView.setImageResource(R.drawable.palette)
        windowView.setOnClickListener {
            ThemeStyleListDialogFragment.newInstance(manager).show(activity?.supportFragmentManager, null)
        }

        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?

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

        windowManager?.addView(windowView, windowLayoutParams)
    }

    companion object {
        var activity: AppCompatActivity? = null
    }
}