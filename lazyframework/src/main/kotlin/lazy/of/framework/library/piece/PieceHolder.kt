package lazy.of.framework.library.piece

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

abstract class PieceHolder<H : RecyclerView.ViewHolder, I : Any>(private val holderClass: KClass<H>, private val itemClass: KClass<I>): PieceView<I>() {

    protected var holder: H? = null

    final override fun onBindView(c: Context, v: View) {
        holder = onMakeViewHolder(v)
    }

    final override fun onBindItem(context: Context, item: I?) {
        if(item != null) {
            holder?.let { onBindViewHolder(context, it, item, 0) }
        }
    }

    fun makeViewHolder(inflater: LayoutInflater, parent: ViewGroup?): RecyclerView.ViewHolder {
        holder = onMakeViewHolder(onCreateView(inflater, parent))
        return holder as H
    }

    fun bindViewHolder(context: Context, viewHolder: RecyclerView.ViewHolder, item: Any, position: Int) {
        val castHolder: H? = castHolder(viewHolder)
        val castItem: I? = castItem(item)

        if(castHolder != null && castItem != null) {
            onBindViewHolder(context, castHolder, castItem, position)
        }
    }

    open fun getViewType(): Int = onLayout()

    open fun isBindItem(item: Any?): Boolean = itemClass.isInstance(item)

    fun getBindItem(holder: H): I? {
        val parent:ViewParent? = holder.itemView.parent
        if(parent is RecyclerView) {
            val adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder> = parent.adapter
            if(adapter is PieceAdapter) {
                val item: Any? = adapter.getBindItem(holder.adapterPosition)
                if(item != null) {
                    return castItem(item)
                }
            }
        }

        if(this@PieceHolder.holder == holder) {
            return item
        }

        return null
    }

    protected fun castHolder(viewHolder: RecyclerView.ViewHolder): H? {
        if(holderClass.isInstance(viewHolder)) {
            @Suppress("UNCHECKED_CAST")
            return viewHolder as H
        }
        return null
    }

    protected fun castItem(item: Any): I? {
        if(itemClass.isInstance(item)) {
            @Suppress("UNCHECKED_CAST")
            return item as I
        }
        return null
    }

    abstract protected fun onMakeViewHolder(view: View): H

    abstract protected fun onBindViewHolder(context: Context, holder: H, item: I, position: Int)

}
