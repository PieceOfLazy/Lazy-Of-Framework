package piece.of.lazy.framework.library.tree

import android.content.Context
import android.support.v7.widget.RecyclerView
import piece.of.lazy.framework.library.piece.PieceAdapter

/**
 * Created by zpdl
 */

abstract class TreeAdapter(context: Context) : PieceAdapter(context), TreeNotify {

    private val root by lazy {
        TreeRoot(this)
    }

    fun root(): TreeRoot = root

    fun getTreePosition(holder: RecyclerView.ViewHolder): TreePosition? {
        return root.getTreePosition(holder.adapterPosition)
    }

    fun getNode(holder: RecyclerView.ViewHolder): TreeNode<*>? {
        val treePosition = getTreePosition(holder)
        return if (treePosition != null) {
            root.getNode(treePosition)
        } else null
    }

    override fun getBindItem(position: Int): Any? {
        return root.getTreePosition(position)?.let {
            root.getNode(it)?.getModel()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pieceHolder = holderInterfaces.get(holder.itemViewType)
        if(pieceHolder is TreeHolder) {
            val treePosition = root.getTreePosition(position)
            treePosition?.let {
                root.getNode(it)?.let {
                    pieceHolder.bindViewHolder(context, holder, it.getModel(), treePosition)
                }
            }
        } else {
            super.onBindViewHolder(holder, position)
        }
    }

    override fun getItemCount(): Int = root.getViewCount()

    override fun getItemId(position: Int): Long {
        val itemId = root.getTreePosition(position)?.let {
            root.getNode(it)?.getNodeId()
        }
        return itemId ?: super.getItemId(position)
    }

    override fun treeSetChanged() {
        notifyDataSetChanged()
    }

    override fun treeMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun treeChanged(position: Int, count: Int) {
        notifyItemRangeChanged(position, count)
    }

    override fun treeInserted(position: Int, count: Int) {
        notifyItemRangeInserted(position, count)
    }

    override fun treeRemoved(position: Int, count: Int) {
        notifyItemRangeRemoved(position, count)
    }
}