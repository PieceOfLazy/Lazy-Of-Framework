package lazy.of.framework.library.panel

import android.content.Context
import android.view.View

/**
 * Created by piece.of.lazy
 */
abstract class PanelLCView<I>: PanelLC() {

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
