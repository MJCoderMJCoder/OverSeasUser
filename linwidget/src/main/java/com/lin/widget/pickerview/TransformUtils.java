package com.lin.widget.pickerview;

import com.lin.widget.pickerview.model.Area;
import com.lin.widget.pickerview.model.AreaImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lin on 2017/2/22.
 */

public class TransformUtils {

    public static List<AreaImpl> transform(List<String> list) {
        List<AreaImpl> list1 = new ArrayList<>();
        Map<String, AreaImpl> yearMap = new HashMap<>();
        Map<String, AreaImpl> monthMap = new HashMap<>();
        int i = 2;
        AreaImpl area;
        int parentId;
        for (String str : list) {
            String year = str.substring(0, 4);
            String yearMonth = str.substring(0, 7);
            String month = yearMonth.substring(5);
            String day = str.substring(8);
            area = yearMap.get(year);
            if (area == null) {
                area = new AreaImpl();
                area.id = i++;
                area.name = year;
                area.parentId = 1;
                list1.add(area);
                yearMap.put(year, area);
            }
            parentId = area.id;
            area = monthMap.get(yearMonth);
            if (area == null) {
                area = new AreaImpl();
                area.id = i++;
                area.name = month;
                area.parentId = parentId;
                list1.add(area);
                monthMap.put(yearMonth, area);
            }
            parentId = area.id;
            area = new AreaImpl();
            area.name = day;
            area.parentId = parentId;
            list1.add(area);
        }
        return list1;
    }
}
