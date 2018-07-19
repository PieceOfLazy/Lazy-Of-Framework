package lazy.of.framework.library.panel.viewpager

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lazy.of.framework.library.panel.PanelBase
import java.util.*

abstract class ViewPagerPanelAdapter(val context: Context) : PagerAdapter()  {

    internal abstract fun getBindItem(position: Int): PanelBase?

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val item = getBindItem(position)
        if(container == null || item == null) {
            return super.instantiateItem(container, position)
        }

        item.makeView(context, container)
        return item.view!!
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as View)
    }
}