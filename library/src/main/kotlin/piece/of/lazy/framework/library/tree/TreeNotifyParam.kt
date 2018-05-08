package piece.of.lazy.framework.library.tree

/**
 * Created by zpdl
 */
internal class TreeNotifyParam(var state: STATE, var position: Int, var count: Int) {

    enum class STATE {
        MOVE,
        CHANGE,
        INSERT,
        REMOVE
    }

    internal fun compose(param: TreeNotifyParam): TreeNotifyParam? {
        if(state != param.state) {
            return null
        }

        return when(state) {
            STATE.MOVE -> null
            STATE.CHANGE -> composeChange(param)
            STATE.INSERT -> composeInsert(param)
            STATE.REMOVE -> composeRemove(param)
        }
    }

    private fun composeChange(param: TreeNotifyParam): TreeNotifyParam? {
        if ((position in param.position..(param.position + param.count))
            || (param.position in position..(position + count))) {
            val maxPosition = if(position + count > param.position + param.count)
                position + count
            else
                param.position + param.count

            return if(position < param.position) {
                TreeNotifyParam(STATE.CHANGE, position, maxPosition - position)
            } else {
                TreeNotifyParam(STATE.CHANGE, param.position, maxPosition - param.position)
            }
        }
        return null
    }

    private fun composeInsert(param: TreeNotifyParam): TreeNotifyParam? {
        if(param.position in position..(position + count)) {
            return TreeNotifyParam(STATE.INSERT, position, count + param.count)
        }
        return null
    }

    private fun composeRemove(param: TreeNotifyParam): TreeNotifyParam? {
        if(position in param.position..(param.position + param.count)) {
            return TreeNotifyParam(STATE.REMOVE, param.position, count + param.count)
        }
        return null
    }
}