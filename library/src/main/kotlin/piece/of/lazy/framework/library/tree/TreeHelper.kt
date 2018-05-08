package piece.of.lazy.framework.library.tree

/**
 * Created by zpdl
 */

class TreeHelper {

    companion object {
        fun <M: Any> makeNode(model: M): TreeNode<M> {
            return TreeNodeImpl(model)
        }

        fun makeEmptyNode(): TreeNode<TreeModelEmpty> {
            return TreeNodeImpl(TreeModelEmpty())
        }

        inline fun <reified T: TreeModel> getNode(model: T): TreeNode<T>? {
            @Suppress("UNCHECKED_CAST")
            return model.getNode() as? TreeNode<T>
        }

        inline fun <reified T: Any> getChildNode(model: TreeModel): TreeNode<T>? {
            model.getNode()?.let {
                return getChildNode(it)
            }
            return null
        }

        inline fun <reified T: Any> getChildNode(node: TreeNode<*>): TreeNode<T>? {
            @Suppress("UNCHECKED_CAST")
            return (0 until node.getChildNodeCount())
                    .map { i -> node.getChildNode(i) }
                    .firstOrNull { it?.getModel() is T } as? TreeNode<T>
        }

        inline fun <reified T: Any> getChildNodes(model: TreeModel): MutableList<TreeNode<T>> {
            model.getNode()?.let {
                return getChildNodes(it)
            }
            return mutableListOf()
        }

        inline fun <reified T: Any> getChildNodes(node: TreeNode<*>): MutableList<TreeNode<T>> {
            val list = mutableListOf<TreeNode<T>>()

            (0 until node.getChildNodeCount())
                    .map { i -> node.getChildNode(i) }
                    .filter { it?.getModel() is T }
                    .forEach {
                        @Suppress("UNCHECKED_CAST")
                        list.add(it as TreeNode<T>)
                    }
            return list
        }

        inline fun <reified T: Any> getChildModel(model: TreeModel): T? {
            model.getNode()?.let {
                return getChildModel(it)
            }
            return null
        }

        inline fun <reified T: Any> getChildModel(node: TreeNode<*>): T? {
            (0 until node.getChildNodeCount())
                    .map { i -> node.getChildNode(i) }
                    .filter { it?.getModel() is T }
                    .forEach {
                        @Suppress("UNCHECKED_CAST")
                        return it?.getModel() as T
                    }

            return null
        }

        inline fun <reified T: Any> getChildModels(model: TreeModel): MutableList<T> {
            model.getNode()?.let {
                return getChildModels(it)
            }
            return mutableListOf()
        }

        inline fun <reified T: Any> getChildModels(node: TreeNode<*>): MutableList<T> {
            val list = mutableListOf<T>()

            (0 until node.getChildNodeCount())
                    .map { i -> node.getChildNode(i) }
                    .filter { it?.getModel() is T }
                    .forEach {
                        @Suppress("UNCHECKED_CAST")
                        list.add(it?.getModel() as T)
                    }
            return list
        }

        fun <T: Any> addModel(parent: TreeNode<*>, model: T, update: Boolean = false): TreeNode<T> {
            val node = makeNode(model)
            addNode(parent, node, update)
            return node
        }

        fun <T: Any> addModel(parent: TreeNode<*>, position: Int, model: T, update: Boolean = false): TreeNode<T> {
            val node = makeNode(model)
            addNode(parent, position, node, update)
            return node
        }

        fun <T: Any> addModels(parent: TreeNode<*>, vararg models: T, update: Boolean = false) {
            val nodes = Array<TreeNode<*>>(models.size, { makeNode(models[it]) })
            addNodes(parent, nodes = *nodes, update = update)
        }

        fun <T: Any> addModels(parent: TreeNode<*>, position: Int, vararg models: T, update: Boolean = false) {
            val nodes = Array<TreeNode<*>>(models.size, { makeNode(models[it]) })
            addNodes(parent, position, nodes = *nodes, update = update)
        }

        fun addNode(parent: TreeNode<*>, node: TreeNode<*>, update: Boolean = false) {
            if(update)
                parent.beginTransition()

            parent.addChildNode(node)

            if(update)
                parent.applyTo()
        }

        fun addNode(parent: TreeNode<*>, position: Int, node: TreeNode<*>, update: Boolean = false) {
            if(update)
                parent.beginTransition()

            parent.addChildNode(position, node)

            if(update)
                parent.applyTo()
        }

        fun addNodes(parent: TreeNode<*>, vararg nodes: TreeNode<*>, update: Boolean = false) {
            if(update)
                parent.beginTransition()

            parent.addChildNode(*nodes)

            if(update)
                parent.applyTo()
        }

        fun addNodes(parent: TreeNode<*>, position: Int, vararg nodes: TreeNode<*>, update: Boolean = false) {
            if(update)
                parent.beginTransition()

            parent.addChildNode(position, *nodes)

            if(update)
                parent.applyTo()
        }

        fun addEmptyNode(parent: TreeNode<*>, update: Boolean = false): TreeNode<TreeModelEmpty> {
            if(update)
                parent.beginTransition()

            val node = makeEmptyNode()
            parent.addChildNode(node)

            if(update)
                parent.applyTo()
            return node
        }

        fun removeNode(model: TreeModel, update: Boolean = false) {
            model.getNode()?.getParent()?.let {
                if(update)
                    it.beginTransition()
                it.removeChildNode(model.getNode()!!)
                if(update)
                    it.applyTo()
            }
        }

        fun changedNode(model: TreeModel, update: Boolean = false) {
            model.getNode()?.let {
                if(update)
                    it.beginTransition()
                it.changedNode()
                if(update)
                    it.applyTo()
            }
        }

        fun upSert(parent: TreeNode<*>, from: TreeNode<*>?, to: TreeNode<*>, update: Boolean = false) {
            if(update)
                parent.beginTransition()

            from?.let {
                parent.replaceChildNode(from, to)
            }.let {
                parent.addChildNode(to)
            }
            if(update)
                parent.applyTo()
        }

        fun upSert(parent: TreeNode<*>, from: TreeNode<*>?, toModel: Any, update: Boolean = false) {
            if(update)
                parent.beginTransition()

            from?.let {
                parent.replaceChildNode(from, makeNode(toModel))
            }.let {
                parent.addChildNode(makeNode(toModel))
            }

            if(update)
                parent.applyTo()
        }
    }
}