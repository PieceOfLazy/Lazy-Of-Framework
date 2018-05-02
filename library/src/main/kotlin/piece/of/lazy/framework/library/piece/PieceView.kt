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

    open fun bindItem(c: Context?, item: I?) {
        this.item = item
        c?.let {
            onBindItem(it, this.item)
        }
    }

    protected abstract fun onBindItem(c: Context, item: I?)
}