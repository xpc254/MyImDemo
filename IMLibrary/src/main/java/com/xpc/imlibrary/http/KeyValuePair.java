package com.xpc.imlibrary.http;

/**
 * Created by xiepc on 2016/8/14 0014 23:50
 */
public class KeyValuePair {
    /**键名*/
    private String key;
    /**值*/
    private String value;

    public KeyValuePair(String key,String value){
        this.key = key;
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
