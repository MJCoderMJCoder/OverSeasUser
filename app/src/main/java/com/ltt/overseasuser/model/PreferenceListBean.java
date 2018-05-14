package com.ltt.overseasuser.model;

import java.util.List;

/**
 * Created by yunwen on 2018/5/14.
 */

public class PreferenceListBean {

    /**
     * status : true
     * code : 200
     * data : [{"parent":{"main":"MAINTENANCE"},"child":[{"name":"REPAIR","section_id":"4","choose":"false"}]},{"parent":{"main":"MACHINERY"},"child":[{"name":"FORKLIFT","section_id":"1","choose":"false"},{"name":"AERIAL PLATFORM","section_id":"2","choose":"false"},{"name":"EXCAVATOR","section_id":"3","choose":"false"},{"name":"PALLET TRUCK","section_id":"5","choose":"false"},{"name":"STACKER","section_id":"6","choose":"false"},{"name":"ORDER PICKER","section_id":"7","choose":"false"},{"name":"OTHERS","section_id":"8","choose":"false"},{"name":"CONTAINER HANDLER","section_id":"9","choose":"false"},{"name":"BOOM LIFT","section_id":"11","choose":"false"},{"name":"SCISSOR LIFT","section_id":"12","choose":"false"}]},{"parent":{"main":"PARTS AND ACCESSORIES"},"child":[{"name":"PARTS AND ACCESSORIES","section_id":"13","choose":"false"}]},{"parent":{"main":"COMMERCIAL VEHICLES"},"child":[{"name":"TRUCKS","section_id":"10","choose":"false"}]}]
     */

    private boolean status;
    private int code;
    private List<DataBean> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * parent : {"main":"MAINTENANCE"}
         * child : [{"name":"REPAIR","section_id":"4","choose":"false"}]
         */

        private ParentBean parent;
        private List<ChildBean> child;

        public ParentBean getParent() {
            return parent;
        }

        public void setParent(ParentBean parent) {
            this.parent = parent;
        }

        public List<ChildBean> getChild() {
            return child;
        }

        public void setChild(List<ChildBean> child) {
            this.child = child;
        }

        public static class ParentBean {
            /**
             * main : MAINTENANCE
             */

            private String main;

            public String getMain() {
                return main;
            }

            public void setMain(String main) {
                this.main = main;
            }
        }

        public static class ChildBean {
            /**
             * name : REPAIR
             * section_id : 4
             * choose : false
             */

            private String name;
            private String section_id;
            private String choose;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSection_id() {
                return section_id;
            }

            public void setSection_id(String section_id) {
                this.section_id = section_id;
            }

            public String getChoose() {
                return choose;
            }

            public void setChoose(String choose) {
                this.choose = choose;
            }
        }
    }
}
