package piece.of.lazy.framework.library.mvp

/**
 * Created by piece.of.lazy
 */
interface View<in P> {
    /**
     * Sets presenter for view
     * @param presenter
     */
    fun setPresenter(presenter: P?)
}