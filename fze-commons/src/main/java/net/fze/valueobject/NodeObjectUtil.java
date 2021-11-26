package net.fze.valueobject;


import java.util.ArrayList;
import java.util.List;

public class NodeObjectUtil {
    /**
     * 获取数据目录节点
     */
    public static <T extends INodeObject> NodeObject getRootWrappedNode(List<T> list) {
        NodeObject root = new NodeObject();
        root.setLabel("root");
        List<NodeObject> rootNodes = new ArrayList<>();
        root.setChildren(rootNodes);
        // 加入根节点
        list.stream().filter(it -> it.getParent().equals("")).forEach(it -> {
            NodeObject node = it.get();
            rootNodes.add(node);
            walkNode(node, list);
        });
        return root;
    }

    public static <T extends INodeObject> void walkNode(NodeObject root, List<T> list) {
        list.stream().filter(it -> it.getParent().equals(root.getId())).forEach(it -> {
            NodeObject child = it.get();
            if (child.getChildren() == null) child.setChildren(new ArrayList<>());
            root.getChildren().add(child);
            walkNode(child, list);
        });
    }
}
