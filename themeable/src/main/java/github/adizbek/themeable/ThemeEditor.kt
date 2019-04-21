package github.adizbek.themeable

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ThemeEditor(var manager: ThemeManager<*>, context: Context) {
    private val editorWidth = 54.toPx()
    private val editorHeight = 54.toPx()

    private var windowView: FloatingActionButton? = null
    private var windowLayoutParams: WindowManager.LayoutParams? = null
    private var wManager: WindowManager? = null

    private val defaultEditorX = editorWidth + 200
    private val defaultEditorY = editorHeight + 200

    private var editorX = defaultEditorX
    private var editorY = defaultEditorY

    var prefs = context.getSharedPreferences("theme_editor", Context.MODE_PRIVATE)

    init {
        loadFloatingEditorPosition()
    }

    private fun loadFloatingEditorPosition() {
        prefs?.apply {
            editorX = prefs.getInt("editor_x", defaultEditorX)
            editorY = prefs.getInt("editor_y", defaultEditorY)
        }
    }

    private fun saveEditorPosition() {
        prefs?.edit()?.apply {
            putInt("editor_x", editorX)
            putInt("editor_y", editorY)

            apply()
        }
    }

    fun show() {
        if (windowView != null || manager.styleListShown)
            return

        activity?.let { act ->
            windowView = FloatingThemeEditorView(act, object : FloatingThemeEditorDelegate {
                override fun onClick() {
                    ThemeStyleListDialogFragment.newInstance(manager).show(
                        act.supportFragmentManager,
                        null
                    )
                }

                override fun onMove(event: MotionEvent) {
                    windowLayoutParams?.apply {
                        this.x = event.rawX.toInt() - editorWidth / 2
                        this.y = event.rawY.toInt() - editorHeight

                        editorX = x
                        editorY = y
                    }

                    wManager?.updateViewLayout(windowView, windowLayoutParams)
                }
            }).apply {
                layoutParams = ViewGroup.LayoutParams(editorWidth, editorHeight)
                setImageResource(R.drawable.ic_palette_white_24dp)
            }

            wManager = act.getSystemService(Context.WINDOW_SERVICE) as WindowManager?

            windowLayoutParams = WindowManager.LayoutParams().apply {
                width = editorWidth
                height = editorHeight
                x = editorX
                y = editorY
                format = PixelFormat.TRANSLUCENT
                gravity = Gravity.TOP or Gravity.START
                type = WindowManager.LayoutParams.TYPE_APPLICATION
                flags =
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            }

            wManager?.addView(windowView, windowLayoutParams)
        }

    }

    fun hide() {
        saveEditorPosition()

        windowView?.let {
            wManager?.removeView(it)
        }

        windowView = null

    }

    companion object {
        var activity: FragmentActivity? = null
    }

    interface FloatingThemeEditorDelegate {
        fun onClick()

        fun onMove(event: MotionEvent)
    }
}

class FloatingThemeEditorView(context: Context, val delegate: ThemeEditor.FloatingThemeEditorDelegate) :
    FloatingActionButton(context) {
    private val MAX_CLICK_DURATION = 200
    private var startClickTime: Long = 0


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when {
            ev.action == MotionEvent.ACTION_MOVE -> {
                if (System.currentTimeMillis() - startClickTime <= MAX_CLICK_DURATION)
                    return false

                delegate.onMove(ev)
            }

            ev.action == MotionEvent.ACTION_UP -> {
                if (System.currentTimeMillis() - startClickTime <= MAX_CLICK_DURATION)
                    delegate.onClick()
            }

            ev.action == MotionEvent.ACTION_DOWN -> startClickTime = System.currentTimeMillis()
        }

        return super.onTouchEvent(ev)
    }
}