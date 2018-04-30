package piece.of.lazy.framework.library.mvp

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by piece.of.lazy
 */
open class PresenterRetainFragment : Fragment() {

    open var presenter: Presenter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
    }
}