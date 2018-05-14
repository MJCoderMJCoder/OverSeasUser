package com.ltt.overseasuser.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yunwen on 2018/5/9.
 * 降序排序
 */

public class DescKeyComparator implements Comparator<String> {
    public static void main(String[] args){
        Map<String, String> map = new HashMap<String, String>();    //构建键值对为<String, String>的Map集合
        map.put("a", "aaa");
        map.put("b", "bbb");
        map.put("c", "ccc");

        Set<String> entrySet = map.keySet();    //获取map集合的所有键的Set集合（于Set集合中无序存放）
        List<String> list = new ArrayList<String>(entrySet);    //新建List集合获取Set集合的所有元素（键对象）（顺序与Set集合一样）
        /**
         * 接下来的排序是list的专长了
         * 通过“比较器(DescKeyComparator)”，对list进行排序
         */
        Collections.sort(list, new DescKeyComparator());
        /*
        Collections.sort(list);    //String实现了Comparable，默认升序排列
        */
        Iterator<String> iter = list.iterator();    //获取List集合的迭代器,String为迭代元素的类型
        while(iter.hasNext()){
            String key = iter.next();
            String value = map.get(key);
            System.out.println("key:" + key + "-->value:" + value);
        }
        /*
        for(Map.Entry<String, String> item: list){
            String key = iter.next();
            String value = map.get(key);
            System.out.println("key:" + key + "-->value:" + value);
        }
        */
    }

    @Override
    public int compare(String key1, String key2){
        return key2.compareTo(key1);    //降序排序; String作为api提供的类，实现了Comparable的compareTo方法被设计成小于、等于、大于分别返回负数、零、正数
    }
}
