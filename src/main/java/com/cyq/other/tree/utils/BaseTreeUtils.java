package com.cyq.other.tree.utils;

import com.cyq.other.tree.entity.BaseTreeEntity;
import com.cyq.other.tree.entity.CityTreeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 树型结构工具类接口
 */
public class BaseTreeUtils<T> {

    /**
     * 创建树结构
     * @param baseTreeList 需要组装的树集合
     * @param nodeParentId 节点父ID
     * @return
     */
    public static <T> List<T> buildBaseTree(List<T> baseTreeList, String nodeParentId) {
        List<T> treeList = new ArrayList<T>();
        for (int i = 0; i < baseTreeList.size(); i++) {
            T nextNode = baseTreeList.get(i);
            if (nextNode instanceof BaseTreeEntity) {
                if (((BaseTreeEntity) nextNode).getNodeParentId().equals(nodeParentId)) {
                    if (((BaseTreeEntity) nextNode).getLeaf() == 1) {
                        // 是叶子节点
                        treeList.add(nextNode);
                    } else {
                        List<T> retTreeList = buildBaseTree(baseTreeList, ((BaseTreeEntity) nextNode).getNodeId());
                        ((BaseTreeEntity) nextNode).setChildNodes(retTreeList);
                        treeList.add(nextNode);
                    }
                    baseTreeList.remove(i);
                    i--;
                }
            }
        }
        return treeList;
    }

    /**
     * 填充树结构数据
     * @param baseTree
     * @param parentId
     * @param orderMap
     * @param <T>
     */
    public static <T> void baseTreeDataFill(List<T> baseTree, String parentId, Map<String, Integer> orderMap) {
        for(T node : baseTree) {
            if (node instanceof CityTreeEntity) {
                Integer order = orderMap.get("order");
                ((CityTreeEntity) node).setOrder(order);
                orderMap.put("order", ++order);
                if (((CityTreeEntity) node).getNodeId() == null) {
                    // 无ID填充ID
                    ((CityTreeEntity) node).setNodeId(UUID.randomUUID().toString());
                }
                if (((CityTreeEntity) node).getChildNodes() != null && ((CityTreeEntity) node).getChildNodes().size() > 0) {
                    // 非叶子节点
                    baseTreeDataFill(((CityTreeEntity) node).getChildNodes(), ((CityTreeEntity) node).getNodeId(), orderMap);
                    ((CityTreeEntity) node).setLeaf(2);
                } else {
                    // 叶子节点
                    ((CityTreeEntity) node).setLeaf(1);
                }
                ((CityTreeEntity) node).setNodeParentId(parentId);
            }
        }
    }

    /**
     * 拆解树结构
     * @param baseTree
     * @param treeList
     * @param <T>
     */
    public static <T> void parseBaseTree(List<T> baseTree, List<T> treeList) {
        for(T nodeTree : baseTree) {
            if (nodeTree instanceof BaseTreeEntity) {
                if (((BaseTreeEntity) nodeTree).getLeaf() == 1) {
                    // 叶子节点
                    treeList.add(nodeTree);
                } else {
                    // 非叶子节点
                    if (((BaseTreeEntity) nodeTree).getChildNodes() != null && ((BaseTreeEntity) nodeTree).getChildNodes().size() > 0) {
                        parseBaseTree(((BaseTreeEntity) nodeTree).getChildNodes(), treeList);
                    }
                    ((BaseTreeEntity) nodeTree).setChildNodes(null);
                    treeList.add(nodeTree);
                }
            }
        }
    }

}
