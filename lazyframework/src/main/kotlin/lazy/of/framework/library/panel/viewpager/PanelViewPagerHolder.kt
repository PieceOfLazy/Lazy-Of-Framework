package lazy.of.framework.library.panel.viewpager

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lazy.of.framework.library.panel.PanelView
import kotlin.reflect.KClass

abstract class PanelViewPagerHolder<H : PanelViewPagerHolder.ViewHolder, I : Any>(private val holderClass: KClass<H>, private val itemClass: KClass<I>): PanelView<I>() {

    protected var holder: H? = null
    internal var viewType = 0

    final override fun onBindView(context: Context, view: View) {
        holder = onMakeViewHolder(view)
    }

    final override fun onBindItem(context: Context, item: I?) {
        if(item != null) {
            holder?.let { onBindViewHolder(context, it, item, 0) }
        }
    }

    fun makeViewHolder(inflater: LayoutInflater, parent: ViewGroup?): PanelViewPagerHolder.ViewHolder {
        holder = onMakeViewHolder(onCreateView(inflater, parent))
        holder?.viewType = viewType
        return holder as H
    }

    fun bindViewHolder(context: Context, viewHolder: PanelViewPagerHolder.ViewHolder, item: Any, position: Int) {
        val castHolder: H? = castHolder(viewHolder)
        val castItem: I? = castItem(item)

        if(castHolder != null && castItem != null) {
            onBindViewHolder(context, castHolder, castItem, position)
        }
    }

    open fun isBindItem(item: Any?): Boolean = itemClass.isInstance(item)

    open fun isBindHolder(holder: PanelViewPagerHolder.ViewHolder?): Boolean = holderClass.isInstance(holder)

    fun getBindItem(holder: H): I? {
        val parent = holder.view.parent
        parent?.let{
            if(it is ViewPager) {
                val adapter = it.adapter
                if(adapter is PanelViewPagerAdapter) {
                    return castItem(adapter.getBindItem(holder.position))
                }
            }
        }

        return if (this.holder === holder) {
            this.item
        } else null
    }

    private fun castHolder(viewHolder: PanelViewPagerHolder.ViewHolder?): H? {
        if(holderClass.isInstance(viewHolder)) {
            @Suppress("UNCHECKED_CAST")
            return viewHolder as H
        }
        return null
    }

    private fun castItem(item: Any?): I? {
        if(itemClass.isInstance(item)) {
            @Suppress("UNCHECKED_CAST")
            return item as I
        }
        return null
    }

    protected abstract fun onMakeViewHolder(view: View): H

    protected abstract fun onBindViewHolder(context: Context, holder: H, item: I, position: Int)

    internal fun onUnBindViewHolder(holder: PanelViewPagerHolder.ViewHolder, position: Int) {

    }

    open class ViewHolder(val view: View) {
        var position: Int = 0
            internal set
        var viewType: Int = 0
            internal set
    }
}