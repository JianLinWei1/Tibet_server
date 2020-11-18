package com.jian.common.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ResultUtil implements Serializable {

        private Integer code;
        private String msg;
        private Long count;
        private Object data;


        public ResultUtil() {
            super();
        }

        public ResultUtil(Integer code) {
            super();
            this.code = code;
        }

        public ResultUtil(Integer code, String msg) {
            super();
            this.code = code;
            this.msg = msg;
        }
    public ResultUtil(Integer code, Object data , String msg) {
        super();
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public static ResultUtil ok(){
            return new ResultUtil(0);
        }

        public static ResultUtil ok(Object list){
            ResultUtil result = new ResultUtil();
            result.setCode(0);
            result.setData(list);;
            return result;
        }
        public static ResultUtil ok(String msg){
            ResultUtil result = new ResultUtil();
            result.setCode(0);
            result.setMsg(msg);
            return result;
        }

        public static ResultUtil error(){
            return new ResultUtil(500,"error");
        }
        public static ResultUtil error(String str){
            return new ResultUtil(500,str);
        }

        public static ResultUtil empty(){
            ResultUtil n  = new ResultUtil();
            n.setCode(0);
            n.setCount(0l);
            n.setData(null);
            n.setMsg("");
            return n;
        }


}
