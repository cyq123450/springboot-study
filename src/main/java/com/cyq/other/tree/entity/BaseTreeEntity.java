package com.cyq.other.tree.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 树型结构的抽象实体类
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseTreeEntity<T> implements Serializable {

    // 节点id
    private String nodeId;
    // 父节点id
    private String nodeParentId;
    // 节点名称
    private String nodeName;
    // 是否是叶子节点(1:是,2:否)
    private Integer leaf;
    // 子节点集合
    private List<T> childNodes;

}
