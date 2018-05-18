package lazy.of.framework.library.mvp

import android.content.res.Configuration
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import lazy.of.framework.library.panel.PanelBase

/**
 * @author lazy.of.zpdl
 */
open class MvpContract<V: MvpView<P>, P: MvpPresenter<V>> constructor(
        val view: V,
        val presenter: P) {

    protected var contractLifeCycle = false

    fun attach(activity: AppCompatActivity, parent: ViewGroup) {

        if(view is Fragment) {
            contractLifeCycle = false

            activity.supportFragmentManager
                    .beginTransaction()
                    .replace(parent.id, view, view::class.simpleName)
                    .commit()
        } else {
            contractLifeCycle = true

            if(view is PanelBase) {
                view.makeView(activity, parent)
            }
        }
    }

    fun detach() {
        if(contractLifeCycle)
            presenter.onViewDetach()
    }

    fun launch() {
        if(contractLifeCycle)
            presenter.onLaunch()
    }

    fun configurationChanged(newConfig: Configuration?) {

    }
}