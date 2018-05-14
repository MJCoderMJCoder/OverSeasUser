package com.ltt.overseasuser.model;


import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

public class TypeSectionBean extends BaseBean {

    private TypeBean type;
      private List<SectionBean> section_list;

    public TypeBean getType() {
        return type;
    }

    public void setType(TypeBean type) {
        this.type = type;
    }

    public List<SectionBean> getSection_list() {
        return section_list;
    }

    public void setSection_list(List<SectionBean> section_list) {
        this.section_list = section_list;
    }

}
