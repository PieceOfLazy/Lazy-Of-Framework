package piece.of.lazy.framework.library.piece

import android.content.Context

/**
 * Created by piece.of.lazy
 */
abstract class PieceView<I> : PieceFrame() {
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