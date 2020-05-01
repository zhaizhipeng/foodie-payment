package com.ysdrzp.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 自定义响应数据
 */
public class YSDRZPJSONResult {

    /**
     * 定义jackson对象
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 响应业务状态
     */
    private Integer status;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应中的数据
     */
    private Object data;

    @JsonIgnore
    private String ok;	// 不使用

    public YSDRZPJSONResult() {

    }

    public YSDRZPJSONResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public YSDRZPJSONResult(Integer status, String msg, Object data, String ok) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.ok = ok;
    }

    public YSDRZPJSONResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public static YSDRZPJSONResult build(Integer status, String msg, Object data) {
        return new YSDRZPJSONResult(status, msg, data);
    }

    public static YSDRZPJSONResult build(Integer status, String msg, Object data, String ok) {
        return new YSDRZPJSONResult(status, msg, data, ok);
    }

    public static YSDRZPJSONResult ok(Object data) {
        return new YSDRZPJSONResult(data);
    }

    public static YSDRZPJSONResult ok() {
        return new YSDRZPJSONResult(null);
    }

    public static YSDRZPJSONResult errorMsg(String msg) {
        return new YSDRZPJSONResult(500, msg, null);
    }

    public static YSDRZPJSONResult errorMap(Object data) {
        return new YSDRZPJSONResult(501, "error", data);
    }

    public static YSDRZPJSONResult errorTokenMsg(String msg) {
        return new YSDRZPJSONResult(502, msg, null);
    }

    public static YSDRZPJSONResult errorException(String msg) {
        return new YSDRZPJSONResult(555, msg, null);
    }

    public static YSDRZPJSONResult errorUserQQ(String msg) {
        return new YSDRZPJSONResult(556, msg, null);
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

}
