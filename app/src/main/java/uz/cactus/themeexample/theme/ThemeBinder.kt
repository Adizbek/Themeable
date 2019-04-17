package uz.cactus.themeexample.theme

import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children

class ThemeBinder(var view: View?, var flag: Flag?, var drawable: Drawable?, var paint: Paint?, var key: String) {
    private var delegate: ((color: Int) -> Unit)? = null

    constructor(view: View?, flag: Flag?, key: String, delegate: (color: Int) -> Unit) : this(
        view,
        flag,
        null,
        null,
        key
    ) {
        this.delegate = delegate
    }

    constructor(drawable: Drawable, key: String) : this(null, null, drawable, null, key)
    constructor(paint: Paint, key: String) : this(null, null, null, paint, key)

    constructor(view: View?, flag: Flag, key: String) : this(view, flag, null, null, key)


    enum class Flag {
        COLOR,
        BACKGROUND_COLOR
    }

    fun processColor(color: Int) {
        if (drawable != null) {
            drawable?.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        }

        paint?.let {
            it.color = color

            if (it is TextPaint) {
                it.linkColor = color
            }
        }

        if (view != null) {
            if (view is Toolbar) {
                (view as Toolbar).apply {
                    if (flag == Flag.COLOR) {
                        setTitleTextColor(color)

                        // TODO check later.
                        for (child in menu.children) {
                            child.icon?.apply {
                                setColorFilter(color, PorterDuff.Mode.MULTIPLY)
                                invalidate()
                            }
                        }

                    } else if (flag == Flag.BACKGROUND_COLOR) {
                        setBackgroundDrawable(ColorDrawable(color))
                    }
                }

            } else if (view is TextView) {
                (view as TextView).apply {
                    if (Flag.COLOR == flag) {
                        setTextColor(color)
                    }
                }
            }

            if (Flag.BACKGROUND_COLOR == flag) {
                view?.setBackgroundColor(color)
            }
        }

        delegate?.let {
            it(color)
        }
    }

}