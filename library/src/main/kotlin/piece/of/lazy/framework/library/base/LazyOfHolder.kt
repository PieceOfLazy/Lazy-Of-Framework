package piece.of.lazy.framework.library.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import kotlin.reflect.KClass

/**
 * Created by zpdl
 */

abstract class LazyOfHolder<VH : RecyclerView.ViewHolder, VI : Any>(private val holderType: KClass<VH>, private val itemType: KClass<VI>): LazyOfViewItem<VI>() {

    protected var holder: VH? = null

    override fun onBindView(c: Context, v: View) {
        holder = onMakeViewHolder(v)
    }

    override fun onBindItem(c: Context, item: VI?) {
        if(item != null) {
            holder?.let { onBindViewHolder(c, it, item, 0) }
        }
    }

    fun makeViewHolder(inflater: LayoutInflater, parent: ViewGroup?): RecyclerView.ViewHolder {
        val view: View = onCreateView(inflater, parent)
        return onMakeViewHolder(view)
    }

    fun bindViewHolder(context: Context, viewHolder: RecyclerView.ViewHolder, item: Any, position: Int) {
        val castHolder: VH? = castHolder(viewHolder)
        val castItem: VI? = castItem(item)

        if(castHolder != null && castItem != null) {
            onBindViewHolder(context, castHolder, castItem, position)
        }
    }

    open fun getViewType(): Int = onLayout()

    open fun isBindItem(item: Any?): Boolean = itemType.isInstance(item)

    fun getBindItem(holder: VH): VI? {
        if(this@LazyOfHolder.holder == holder) {
            return item
        }

        val parent:ViewParent? = holder.itemView.parent
        if(parent is RecyclerView) {
            val adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder> = parent.adapter
            if(adapter is LazyOfAdapter) {
                val item: Any? = adapter.getBindItem(holder.adapterPosition)
                if(item != null) {
                    return castItem(item)
                }
            }
        }

        return null
    }

    private fun castHolder(viewHolder: RecyclerView.ViewHolder): VH? {
        if(holderType.isInstance(viewHolder)) {
            @Suppress("UNCHECKED_CAST")
            return viewHolder as VH
        }
        return null
    }

    private fun castItem(item: Any): VI? {
        if(itemType.isInstance(item)) {
            @Suppress("UNCHECKED_CAST")
            return item as VI
        }
        return null
    }

    private fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?):
            View = inflater.inflate(onLayout(), parent, false)

    abstract protected fun onMakeViewHolder(view: View): VH

    abstract protected fun onBindViewHolder(context: Context, holder: VH, item: VI, position: Int)

}
