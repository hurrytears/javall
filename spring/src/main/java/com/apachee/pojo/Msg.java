package com.apachee.pojo;


public class Msg {
    private int code;
    private String status;
    private Object msg;

    public static Msg success(){
        return success("请求成功");
    }

    public static Msg fail(){
        return fail("请求失败");
    }

    public static Msg success(Object msg){
        return new Msg(200, "成功", msg);
    }

    public static Msg fail(Object msg){
        return new Msg(200, "失败", msg);
    }

    public Msg(){
    }

    public Msg(int code, String status, Object msg){
        this.code = code;
        this.status = status;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }




}
