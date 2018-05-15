package lazy.of.framework.library.mvp

/**
 * @author lazy.of.zpdl
 */
interface MvpView<P> {
    /**
     * Sets presenter for view
     * @param presenter
     */
    fun setPresenter(presenter: P?)
}