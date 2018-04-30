package piece.of.lazy.framework.library.base

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by piece.of.lazy
 */
abstract class LazyOfView {
    var view: View? = null
        private set(value) {
            field = value
        }

    open fun makeView(context: Context, parent: ViewGroup?): View? {
        val view: View? = makeViewOnly(context, parent) ?: return null

        view?.let {
            parent?.addView(it)
        }

        return view
    }

    open fun makeViewOnly(context: Context, parent: ViewGroup?): View? {
        val view = onCreatePieceOfView(
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater, parent) ?: return null
        bindView(context, view)

        return view
    }

    open fun bindView(context: Context, parent: ViewGroup?, @IdRes id: Int) {
        val view: View? = parent?.findViewById(id)
        bindView(context, view)
    }

    open fun bindView(context: Context, view: View?) {
        this.view = view
        this.view?.let {
            onBindView(context, it)
        }
    }

    protected open fun onCreatePieceOfView(inflater: LayoutInflater?, parent: ViewGroup?): View? =
            inflater?.inflate(onLayout(), parent, false)

    @LayoutRes
    protected abstract fun onLayout(): Int

    protected abstract fun onBindView(c: Context, v: View)
}
