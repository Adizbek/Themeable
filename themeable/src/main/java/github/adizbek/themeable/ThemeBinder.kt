package github.adizbek.themeable

import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children

class ThemeBinder(
    var view: View? = null,
    var flag: Array<Flag>? = null,
    var drawable: Drawable? = null,
    var paint: Paint? = null,
    private var key: String? = null,
    private var keyProvider: (() -> String)? = null,
    private var delegate: ((color: Int) -> Unit)? = null

) {
    constructor(view: View, flag: Flag, key: String) : this(view, flag = arrayOf(flag), key = key)

    constructor(view: View, flag: Flag, keyProvider: (() -> String)) : this(
        view,
        arrayOf(flag),
        keyProvider = keyProvider
    )


    fun getKey(): String {
        key?.let {
            return it
        }

        keyProvider?.let {
            return it()
        }

        throw RuntimeException("Style not defined")
    }


    enum class Flag {
        COLOR,
        COMPOUND_ICON,
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
                    flag?.apply {
                        if (contains(Flag.COLOR)) {
                            setTitleTextColor(color)

                            // TODO check later.
                            for (child in menu.children) {
                                child.icon?.apply {
                                    setColorFilter(color, PorterDuff.Mode.MULTIPLY)
                                    invalidate()
                                }
                            }

                        }

                        if (contains(Flag.BACKGROUND_COLOR)) {
                            setBackgroundDrawable(ColorDrawable(color))
                        }
                    }
                }

            } else if (view is TextView) {
                (view as TextView).apply {

                    flag?.apply {
                        if (contains(Flag.COLOR)) {
                            setTextColor(color)
                        }

                        if (contains(Flag.COMPOUND_ICON)) {
                            compoundDrawables.forEach {
                                it?.invalidateSelf()
                            }
                        }
                    }
                }
            }

            if (Flag.BACKGROUND_COLOR == flag) {
                view?.setBackgroundColor(color)
            }

            view?.invalidate()
        }

        delegate?.let {
            it(color)
        }
    }

}