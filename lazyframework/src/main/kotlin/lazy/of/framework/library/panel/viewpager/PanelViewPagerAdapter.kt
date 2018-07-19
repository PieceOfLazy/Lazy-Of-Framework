package lazy.of.framework.library.panel.viewpager

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

abstract class PanelViewPagerAdapter(val context: Context) : PagerAdapter()  {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private val holderInterfaces= mutableListOf<PanelViewPagerHolder<*,*>>()
    private val viewHolders = SparseArray<PanelViewPagerHolder.ViewHolder>()
    private val viewHolderSaves = ArrayList<PanelViewPagerHolder.ViewHolder>()

    init {
        this.bindHolder(holderInterfaces)
    }

    private fun bindHolder(list: MutableList<PanelViewPagerHolder<*, *>>) {
        onBindHolder(list)

        for (i: Int in 0 until list.size) {
            list[i].viewType = i
        }
    }

    fun notifyItemChanged() {
        for (i in 0..viewHolders.size()) {
            val position = viewHolders.keyAt(i)
            val viewHolder = viewHolders.valueAt(i)

            notifyItemBind(viewHolder, position)
        }
    }

    fun notifyItemChanged(position: Int) {
        val viewHolder = viewHolders.get(position)
        if (viewHolder != null) {
            notifyItemBind(viewHolder, position)
        }
    }

    private fun notifyItemBind(viewHolder: PanelViewPagerHolder.ViewHolder, position: Int) {
        val item = getBindItem(position) ?: return

        var holder: PanelViewPagerHolder<*,*>? = null
        for (i in 0 until holderInterfaces.size) {
            if (holderInterfaces[i].isBindItem(item)) {
                holder = holderInterfaces[i]
                break
            }
        }

        holder?.bindViewHolder(context, viewHolder, item, position)
    }

    protected abstract fun onBindHolder(list: MutableList<PanelViewPagerHolder<*,*>>)

    internal abstract fun getBindItem(position: Int): Any?

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getItemPosition(`object`: Any?): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val item = getBindItem(position)
        if(container == null || item == null) {
            return super.instantiateItem(container, position)
        }

        var holder: PanelViewPagerHolder<*,*>? = null
        for(i in holderInterfaces.indices) {
            if(holderInterfaces[i].isBindItem(item)) {
                holder = holderInterfaces[i]
            }
        }

        if(holder == null) {
            return super.instantiateItem(container, position)
        }

        val viewHolder = getViewHolder(holder, container)
        viewHolder.position = position

        holder.bindViewHolder(context, viewHolder, item, position)

        viewHolders.put(position, viewHolder)

        container.addView(viewHolder.view)
        return viewHolder.view
    }

    private fun getViewHolder(holder: PanelViewPagerHolder<*,*>, container: ViewGroup): PanelViewPagerHolder.ViewHolder {
        var viewHolder: PanelViewPagerHolder.ViewHolder? = null

        for (i in viewHolderSaves.indices) {
            if (holder.isBindHolder(viewHolderSaves[i])) {
                viewHolder = viewHolderSaves.removeAt(i)
                break
            }
        }

        if (viewHolder == null) {
            viewHolder = holder.makeViewHolder(inflater, container)
        }

        return viewHolder
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as View)

        val viewHolder = viewHolders.get(position)
        if (viewHolder != null) {
            viewHolders.remove(position)
            if(position in 0 until holderInterfaces.size) {
                holderInterfaces[viewHolder.viewType].onUnBindViewHolder(viewHolder, position)
            }

            viewHolderSaves.add(viewHolder)
        }
    }

}