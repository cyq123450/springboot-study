package com.cyq.other.tree.utils;

import com.cyq.other.tree.entity.CityTreeEntity;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * BaseTreeUtils测试类
 */
public class BaseTreeUtilsTest {

    /**
     * buildBaseTree方法测试
     */
    @Test
    public void buildBaseTreeTest() {
        /**
         * 北京地区
         */
        CityTreeEntity city1 = new CityTreeEntity("100000", "1", "北京市", 2, "北京是首都", 1);
        CityTreeEntity city2 = new CityTreeEntity("110000", "100000", "朝阳区", 1, "三里屯", 2);
        CityTreeEntity city3 = new CityTreeEntity("120000", "100000", "海淀区", 1, "清华大学", 3);
        CityTreeEntity city4 = new CityTreeEntity("130000", "100000", "石景山区", 1, "古城", 4);

        /**
         * 山东地区
         */
        CityTreeEntity city5 = new CityTreeEntity("200000", "1", "山东省", 2, "好客山东人", 5);
        CityTreeEntity city6 = new CityTreeEntity("210000", "200000", "济南市", 2, "大明湖", 6);
        CityTreeEntity city7 = new CityTreeEntity("211000", "210000", "章丘县", 1, "李清照", 7);
        CityTreeEntity city8 = new CityTreeEntity("212000", "210000", "历城区", 1, "唐冶新区", 8);
        CityTreeEntity city9 = new CityTreeEntity("220000", "200000", "枣庄市", 2, "铁道游击队", 9);
        CityTreeEntity city10 = new CityTreeEntity("221000", "220000", "滕州县", 1, "辣子鸡", 10);

        /**
         * 德州地区
         */
        CityTreeEntity city11 = new CityTreeEntity("230000", "200000", "德州市", 1, "扒鸡", 11);

        List<CityTreeEntity> datas = new LinkedList<CityTreeEntity>();
        datas.add(city1);
        datas.add(city2);
        datas.add(city3);
        datas.add(city4);
        datas.add(city5);
        datas.add(city6);
        datas.add(city7);
        datas.add(city8);
        datas.add(city9);
        datas.add(city10);
        datas.add(city11);

        List<CityTreeEntity> retListData = BaseTreeUtils.buildBaseTree(datas, "1");
        System.out.println("SUCCESS...");

    }

    /**
     * baseTreeDataFill方法测试
     */
    @Test
    public void baseTreeDataFillTest() {
        /**
         * 北京地区
         */
        CityTreeEntity city2 = new CityTreeEntity("110000", "100000", "朝阳区", 1, "三里屯", 2);
        CityTreeEntity city3 = new CityTreeEntity("120000", "100000", "海淀区", 1, "清华大学", 3);
        CityTreeEntity city4 = new CityTreeEntity("130000", "100000", "石景山区", 1, "古城", 4);
        List<CityTreeEntity> city1List = new ArrayList<CityTreeEntity>();
        city1List.add(city2);
        city1List.add(city3);
        city1List.add(city4);
        CityTreeEntity city1 = new CityTreeEntity("北京市", city1List, "北京是首都");

        /**
         * 山东地区
         */
        CityTreeEntity city7 = new CityTreeEntity("211000", "210000", "章丘县", 1, "李清照", 7);
        CityTreeEntity city8 = new CityTreeEntity("212000", "210000", "历城区", 1, "唐冶新区", 8);
        List<CityTreeEntity> city6List = new ArrayList<CityTreeEntity>();
        city6List.add(city7);
        city6List.add(city8);
        CityTreeEntity city6 = new CityTreeEntity("济南市", city6List, "大明湖");
        CityTreeEntity city10 = new CityTreeEntity("221000", "220000", "滕州县", 1, "辣子鸡", 10);
        List<CityTreeEntity> city9List = new ArrayList<CityTreeEntity>();
        city9List.add(city10);
        CityTreeEntity city9 = new CityTreeEntity("枣庄市", city9List, "铁道游击队");

        /**
         * 德州地区
         */
        CityTreeEntity city11 = new CityTreeEntity("230000", "200000", "德州市", 1, "扒鸡", 11);

        List<CityTreeEntity> city5List = new ArrayList<CityTreeEntity>();
        city5List.add(city6);
        city5List.add(city9);
        city5List.add(city11);
        CityTreeEntity city5 = new CityTreeEntity("山东省", city5List, "好客山东人");

        List<CityTreeEntity> city = new ArrayList<CityTreeEntity>();
        city.add(city1);
        city.add(city5);
        Map<String, Integer> orderMap = new HashMap();
        orderMap.put("order", 1);
        BaseTreeUtils.baseTreeDataFill(city, "1", orderMap);

        System.out.println("SUCCESS...");
    }

}
