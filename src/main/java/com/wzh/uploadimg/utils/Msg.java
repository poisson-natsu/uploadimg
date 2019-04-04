package com.wzh.uploadimg.utils;

//import com.google.gson.Gson;



public class Msg {

    private int code;

    private String msg;

    private Object data;

    public static  Msg success(boolean is) {
        Msg result = new Msg();
        result.setCode(is?0:-1);
        result.setMsg(is?"处理成功！":"处理失败！");
        return result;
    }

    public static Msg success() {
        Msg result = new Msg();
        result.setCode(0);
        result.setMsg("处理成功！");
        return result;
    }

    public static Msg fail() {
        Msg result  = new Msg();
        result.setCode(-1);
        result.setMsg("处理失败！");
        return result;
    }

//    public String toString(){
//        Gson gson = new Gson();
//        return gson.toJson(this);
//    }

    public Msg add(Object value) {
        this.setData(value);
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
