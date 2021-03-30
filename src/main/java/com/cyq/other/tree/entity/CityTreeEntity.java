package com.cyq.other.tree.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 城市树结构实体类(BaseTreeEntity的扩展类)
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CityTreeEntity extends BaseTreeEntity<CityTreeEntity> {

    /**
     * 扩展属性
     */
    // 城市信息
    private String info;
    // 顺序号
    private Integer order;

    public CityTreeEntity(String nodeId, String nodeParentId, String nodeName, Integer leaf, String info, Integer order) {
        super(nodeId, nodeParentId, nodeName, leaf, null);
        this.info = info;
        this.order = order;
    }

    public CityTreeEntity(String nodeName, List<CityTreeEntity> childNodes, String info) {
        super(null, null, nodeName, null, childNodes);
        this.info = info;
    }

}
