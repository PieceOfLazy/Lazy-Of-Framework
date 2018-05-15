package lazy.of.framework.library.panel

import android.content.Context

/**
 * Created by piece.of.lazy
 */
abstract class PanelView<I> : PanelBase() {
    var item: I? = null
        private set(value) {
            field = value
        }

    open fun bindItem(context: Context, item: I?) {
        this.item = item
        onBindItem(context, this.item)
    }

    protected abstract fun onBindItem(context: Context, item: I?)
}