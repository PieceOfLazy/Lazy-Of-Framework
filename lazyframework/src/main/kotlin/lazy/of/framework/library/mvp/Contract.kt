package lazy.of.framework.library.mvp

import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import lazy.of.framework.library.piece.PieceFrame

/**
 * Created by piece.of.lazy
 */
abstract class Contract<V: View<P>, P: Presenter<V>> constructor(private val tag: String) {

    protected var presenter: P? = null
    protected var view: V? = null

    open fun attach(activity: AppCompatActivity, parent: android.view.ViewGroup) {
        detach()
        attach(activity)

        view?.let {
            if(it is PieceFrame) {
                (it as PieceFrame).makeView(activity, parent)
            }
            presenter?.onViewAttach(it)
        }
    }

    open fun attach(activity: AppCompatActivity, bindView: android.view.View) {
        detach()
        attach(activity)

        view?.let {
            if(it is PieceFrame) {
                (it as PieceFrame).bindView(activity, bindView)
            }
            presenter?.onViewAttach(it)
        }
    }

    private fun attach(activity: AppCompatActivity) {
        if(!TextUtils.isEmpty(tag)) {
            var fragment = activity.supportFragmentManager.findFragmentByTag(tag)
            when (fragment) {
                null -> {
                    presenter = onPresenter()

                    fragment = PresenterRetainFragment().apply {
                        this.presenter = this@Contract.presenter
                    }
                    val transaction = activity.supportFragmentManager.beginTransaction()
                    transaction.add(fragment, tag).commit()
                }
                is PresenterRetainFragment ->  {
                    @Suppress("UNCHECKED_CAST")
                    presenter = fragment.presenter as P
                }
            }
        }

        if(presenter == null) {
            presenter = onPresenter()
        }
        view = onView().apply {
            this.setPresenter(presenter)
        }
    }

    open fun detach() {
        this.view?.setPresenter(null)
        this.view = null
        this.presenter?.onViewDetach()
        this.presenter = null
    }

    abstract fun onPresenter(): P

    abstract fun onView(): V
}