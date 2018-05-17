package lazy.of.framework.library.mvp

/**
 * @author lazy.of.zpdl
 */
interface MvpPresenter<V> {

    fun onViewAttach(view: V)

    fun onViewDetach()

    fun onLaunch()
}