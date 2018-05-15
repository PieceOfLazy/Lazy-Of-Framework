package lazy.of.framework.library.panel

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by zpdl
 */

abstract class PanelAdapter(protected val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    protected val holderInterfaces: SparseArray<PanelHolder<*,*>> = SparseArray()

    init {
        this.setHasStableIds(true)
        this.onBindHolder(holderInterfaces)
    }

    private fun onBindHolder(list: SparseArray<PanelHolder<*,*>>) {
        val bindHolder = mutableListOf<PanelHolder<*,*>>()
        onBindHolder(bindHolder)

        for (holder in bindHolder) {
            list.put(holder.getViewType(), holder)
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int {
        val item = getBindItem(position)

        return (0 until holderInterfaces.size())
                .map { holderInterfaces.valueAt(it) }
                .firstOrNull { it.isBindItem(item) }
                ?.getViewType()
                ?: super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderInterface: PanelHolder<*,*>? = holderInterfaces.get(viewType)
        return if(holderInterface != null) {
            holderInterface.makeViewHolder(inflater, parent)
        } else {
            val tv = TextView(context).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                gravity = Gravity.CENTER

                setTextColor(Color.WHITE)
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
                setPadding(20, 20, 20, 20)
                setBackgroundColor(Color.DKGRAY)
            }

            object : RecyclerView.ViewHolder(tv) {
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getBindItem(position)
        val holderInterface: PanelHolder<*,*>? = holderInterfaces.get(holder.itemViewType)

        when {
            holderInterface != null -> item?.let {
                holderInterface.bindViewHolder(context, holder, item, position)
            }
            holder.itemView is TextView -> {
                (holder.itemView as TextView).text = String.format("ERROR : position : %d", position)
            }
            else -> {

            }
        }
    }

    protected abstract fun onBindHolder(list: MutableList<PanelHolder<*,*>>)

    abstract fun getBindItem(position: Int): Any?
}