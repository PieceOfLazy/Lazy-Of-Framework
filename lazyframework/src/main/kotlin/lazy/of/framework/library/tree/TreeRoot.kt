package lazy.of.framework.library.tree

import lazy.of.framework.library.util.Log

/**
 * Created by zpdl
 */

class TreeRoot internal constructor(private val notify: TreeNotify) : TreeNodeImpl<TreeModelEmpty>(TreeModelEmpty()) {

    private var nextNodeId = 0L

    private var isTransition = false
    private var param: TreeNotifyParam? = null

    internal fun nextNodeId(): Long {
        nextNodeId++
        if(nextNodeId == Long.MAX_VALUE) {
            nextNodeId = 1L
        }
        return nextNodeId
    }

    override fun getParent(): TreeNodeImpl<*>? = null

    override fun getRoot(): TreeRoot? = this

    override fun setNodeId(root: TreeRoot) {
    }

    override fun beginTransition() {
        isTransition = getViewCount() > 0
        this.param = null
    }

    override fun applyTo() {
        if(!isTransition) {
            traversals()
            this.param = null
            notify.treeSetChanged()
        } else {
            this.param?.let {
                applyTo(it)
            }
            this.param = null
            isTransition = false
        }
    }

    override fun notifyCompose(param: TreeNotifyParam) {
        if(!isTransition) {
            return
        }

        if(this.param != null) {
            val composeParam = this.param?.compose(param)
            if(composeParam == null) {
                applyTo(this.param)
                this.param = param
            } else {
                this.param = composeParam
            }
        } else {
            this.param = param
        }
    }

    private fun applyTo(param: TreeNotifyParam?) {
        if(param == null) {
            return
        }

        when (param.state) {
            TreeNotifyParam.STATE.MOVE -> {
                Log.d("TreeRoot", "applyTo MOVED : POS = ${param.position}, CNT = ${param.count}")
                notify.treeMoved(param.position, param.count)
            }
            TreeNotifyParam.STATE.CHANGE -> {
                Log.d("TreeRoot", "applyTo CHANGED : POS = ${param.position}, CNT = ${param.count}")
                notify.treeChanged(param.position, param.count)
            }
            TreeNotifyParam.STATE.INSERT -> {
                Log.d("TreeRoot", "applyTo INSERTED : POS = ${param.position}, CNT = ${param.count}")
                traversals()
                notify.treeInserted(param.position, param.count)
            }
            TreeNotifyParam.STATE.REMOVE -> {
                Log.d("TreeRoot", "applyTo REMOVED : POS = ${param.position}, CNT = ${param.count}")
                traversals()
                notify.treeRemoved(param.position, param.count)
            }
        }
    }
}