package com.lin.widget.pickerview.model;

/**
 * Created by lin on 2017/8/10.
 */

public class AreaImpl implements Area {

    public int id;
    public String name;
    public int parentId;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getParentId() {
        return parentId;
    }
}
