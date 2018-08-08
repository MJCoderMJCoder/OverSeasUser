package com.ltt.overseasuser.utils;

import com.ltt.overseasuser.base.Constants;
import com.ltt.overseasuser.model.ChatMessageBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yunwen on 2018/5/9.
 * 升序排序
 */

public class AscKeyComparator implements Comparator<Map.Entry<String, Object>> {

    public static List<ChatMessageBean.MessageBean> AscMap(Map<String, Object> map){
        List<ChatMessageBean.MessageBean> listmessage = new ArrayList<>();
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();    //获取map集合的所有"映射"的Set集合,这里规范每个映射的类型为Map.Entry<K, V>（于Set集合中无序存放）
        List<Map.Entry<String, Object>> list = new ArrayList<Map.Entry<String, Object>>(entrySet);    //新建List集合获取Set集合的所有元素（"映射"对象）（顺序与Set集合一样）
        /**
         * 接下来的排序是list的专长了
         * 通过“比较器(AscKeyComparator)”，对list进行排序
         */
        Collections.sort(list, new AscKeyComparator());

        Iterator<Map.Entry<String, Object>> iter = list.iterator();    //获取List集合的迭代器,Map.Entry<K, V>为迭代元素的类型
        while(iter.hasNext()){
            Map.Entry<String, Object> item = iter.next();
            String key = item.getKey();
            Object value = item.getValue();
            Map<String, Object> mapObj = (Map<String, Object>) value;
            ChatMessageBean.MessageBean match = new ChatMessageBean.MessageBean();
            match.setCreatedAt( mapObj.get(Constants.CREATEAT) +"");
            match.setMessage((String) mapObj.get(Constants.MESSAGE));
            match.setSenderId((String) mapObj.get(Constants.SENDERID));
            match.setSenderName((String) mapObj.get(Constants.SENDERNAME));
            match.setType((String) mapObj.get(Constants.TYPE));
            listmessage.add(match);
            System.out.println("key:" + key + "-->value:" + value);
        }

        return listmessage;
        /*
        for(Map.Entry<String, String> item: list){
            String key = item.getKey();
            String value = item.getValue();
            System.out.println("key:" + key + "-->value:" + value);
        }
        */
    }

    @Override
    public int compare(Map.Entry<String, Object> item1, Map.Entry<String, Object> item2){
        return item1.getKey().compareTo(item2.getKey());    //升序排序
    }
}
