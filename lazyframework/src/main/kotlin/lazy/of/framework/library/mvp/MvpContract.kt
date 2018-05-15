package lazy.of.framework.library.mvp

import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import lazy.of.framework.library.panel.PanelBase

/**
 * @author lazy.of.zpdl
 */
open class MvpContract<V: MvpView<P>, P: MvpPresenter<V>> constructor(
        val view: V,
        val presenter: P) {

    fun attach(activity: AppCompatActivity, parent: ViewGroup) {
        detach()

        if(activity is MvpContractListener) {
            presenter.onViewAttach(view, activity)
        } else {
            presenter.onViewAttach(view, null)
        }

        if(view is PanelBase) {
            view.makeView(activity, parent)
        }
    }

    fun detach() {
        this.presenter.onViewDetach()
    }

    fun launch() {
        this.presenter.onLaunch()
    }
}