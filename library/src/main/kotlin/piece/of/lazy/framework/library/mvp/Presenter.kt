package piece.of.lazy.framework.library.mvp

/**
 * Created by piece.of.lazy
 */
interface Presenter<in V> {

    fun onViewAttach(view: V)

    fun onViewDetach()

    fun onLaunch()
}