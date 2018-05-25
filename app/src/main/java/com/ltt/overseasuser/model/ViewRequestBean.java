package com.ltt.overseasuser.model;



import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

/**
 * Created by yunwen on 2018/5/24.
 */

public class ViewRequestBean extends BaseBean {

    /**
     * data : {"request":"#2-Machinery-Forklift","user":"TEST12E TEST123","questions":[{"question_title":"Looking to RENT / to BUY?","question_answer":"R"},{"question_title":"Condition of the forklift?","question_answer":"Used (Less than 10 years)"},{"question_title":"Forklift Power Type?","question_answer":"Electric"},{"question_title":"Lifting Capacity (kg)","question_answer":"1501kg to 2500kg"},{"question_title":"Height of goods to be lifted? (m)","question_answer":"2"},{"question_title":"Any height limit at the operation site? (m)","question_answer":"2"},{"question_title":"Operating Condition (Indoor/Outdoor)","question_answer":"O"},{"question_title":"Where do you need the forklift to be?","question_answer":"K"},{"question_title":"Anything else you would like the Pro to know?","question_answer":""}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * request : #2-Machinery-Forklift
         * user : TEST12E TEST123
         * questions : [{"question_title":"Looking to RENT / to BUY?","question_answer":"R"},{"question_title":"Condition of the forklift?","question_answer":"Used (Less than 10 years)"},{"question_title":"Forklift Power Type?","question_answer":"Electric"},{"question_title":"Lifting Capacity (kg)","question_answer":"1501kg to 2500kg"},{"question_title":"Height of goods to be lifted? (m)","question_answer":"2"},{"question_title":"Any height limit at the operation site? (m)","question_answer":"2"},{"question_title":"Operating Condition (Indoor/Outdoor)","question_answer":"O"},{"question_title":"Where do you need the forklift to be?","question_answer":"K"},{"question_title":"Anything else you would like the Pro to know?","question_answer":""}]
         */

        private String request;
        private String user;
        private List<QuestionsBean> questions;

        public String getRequest() {
            return request;
        }

        public void setRequest(String request) {
            this.request = request;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public List<QuestionsBean> getQuestions() {
            return questions;
        }

        public void setQuestions(List<QuestionsBean> questions) {
            this.questions = questions;
        }

        public static class QuestionsBean {
            /**
             * question_title : Looking to RENT / to BUY?
             * question_answer : R
             */

            private String question_title;
            private String question_answer;

            public String getQuestion_title() {
                return question_title;
            }

            public void setQuestion_title(String question_title) {
                this.question_title = question_title;
            }

            public String getQuestion_answer() {
                return question_answer;
            }

            public void setQuestion_answer(String question_answer) {
                this.question_answer = question_answer;
            }
        }
    }
}
