package lazy.of.framework.library.panel

import android.content.Context
import android.view.View

/**
 * Created by piece.of.lazy
 */
abstract class PanelLC: PanelBase() {

    protected var layoutContainer: PanelLayoutContainer? = null
        private set(value) {
            field = value
        }

    override fun onBindView(context: Context, view: View) {
        layoutContainer = PanelLayoutContainer(view)
        onBindLayoutContainer(context, layoutContainer!!)
    }

    protected abstract fun onBindLayoutContainer(context: Context, layoutContainer: PanelLayoutContainer)
}
