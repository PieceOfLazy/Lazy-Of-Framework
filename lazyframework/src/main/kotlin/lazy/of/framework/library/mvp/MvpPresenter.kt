package lazy.of.framework.library.mvp

/**
 * @author lazy.of.zpdl
 */
interface MvpPresenter<in V> {

    fun onViewAttach(view: V)

    fun onViewDetach()

    fun onLaunch()
}