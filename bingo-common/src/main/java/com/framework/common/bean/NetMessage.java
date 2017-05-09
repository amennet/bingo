package com.framework.common.bean;

import io.protostuff.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangGe on 2017/4/7.
 */
public class NetMessage implements Serializable{

    @Tag(1)
    private int command;

    @Tag(2)
    private String userId;

    @Tag(3)
    private String topic;

    @Tag(4)
    private String src;

    @Tag(5)
    private String dst;

    @Tag(6)
    private int errorCode;

    @Tag(7)
    private String errorMSG;

    @Tag(8)
    private boolean isSuccess;

    @Tag(9)
    private Object object;

    @Tag(10)
    private Map<String, Object> map = new HashMap<>();

    @Tag(11)
    private List<Object> list = new ArrayList<>();

    public NetMessage() {

    }

    public NetMessage(String topic) {
        this.topic = topic;
    }

    public NetMessage(String topic, String userId) {
        this.topic = topic;
        this.userId = userId;
    }

    public NetMessage(boolean isSuccess, int command) {
        this.isSuccess = isSuccess;
        this.command = command;
    }

    public NetMessage(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public NetMessage(boolean isSuccess, String userId) {
        this.isSuccess = isSuccess;
        this.userId = userId;
    }

    public NetMessage(boolean isSuccess, String userId, int command) {
        this.isSuccess = isSuccess;
        this.userId = userId;
        this.command = command;
    }

    public NetMessage(boolean isSuccess, int errorCode, String errorMSG) {
        this.isSuccess = isSuccess;
        this.errorCode = errorCode;
        this.errorMSG = errorMSG;
    }

    public NetMessage(boolean isSuccess, String userId, int errorCode, String errorMSG) {
        this.isSuccess = isSuccess;
        this.userId = userId;
        this.errorCode = errorCode;
        this.errorMSG = errorMSG;
    }

    public NetMessage(boolean isSuccess, String userId, int command, int errorCode, String errorMSG) {
        this.isSuccess = isSuccess;
        this.userId = userId;
        this.command = command;
        this.errorCode = errorCode;
        this.errorMSG = errorMSG;
    }

    public static NetMessage client(String topic) {
        return new NetMessage(topic);
    }

    public static NetMessage client(String topic, String userId) {
        return new NetMessage(topic, userId);
    }

    public static NetMessage instance() {
        return new NetMessage();
    }

    public static NetMessage success() {
        return new NetMessage(true);
    }

    public static NetMessage success(String userId) {
        return new NetMessage(true, userId);
    }

    public static NetMessage fail() {
        return new NetMessage(false);
    }

    public static NetMessage fail(String userId) {
        return new NetMessage(false, userId);
    }

    public NetMessage setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getDst() {
        return dst;
    }

    public NetMessage setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    /**
     * 向result中添加额外的参数
     *
     * @param key
     * @param value
     * @return
     */
    public NetMessage put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public NetMessage putAll(Map parameters) {
        this.map.putAll(parameters);
        return this;
    }

    public Object getObject() {
        return object;
    }

    public NetMessage setObject(Object object) {
        this.object = object;
        return this;
    }

    public NetMessage setSrc(String src) {
        this.src = src;
        return this;
    }

    public String getSrc() {
        return src;
    }

    public String getTopic() {
        return topic;
    }

    public NetMessage setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public Object get(String key) {
        return map.get(key);
    }

    public List<Object> getList() {
        return list;
    }

    public NetMessage add(Object object) {
        list.add(object);
        return this;
    }

    public NetMessage addAll(List objects) {
        list.addAll(objects);
        return this;
    }

    public NetMessage setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public NetMessage setErrorMSG(String errorMSG) {
        this.errorMSG = errorMSG;
        return this;
    }

    public NetMessage setCommand(int command) {
        this.command = command;
        return this;
    }

    public NetMessage setDst(String dst) {
        this.dst = dst;
        return this;
    }

    public int getErrorCode() {
        return errorCode;
    }

    /*判断是否成功*/
    public boolean isSuccess() {
        return isSuccess;
    }

    public NetMessage copy() {
        NetMessage netMessage = new NetMessage(this.isSuccess);
        netMessage.command = this.command;
        netMessage.errorMSG = this.errorMSG;
        netMessage.userId = this.userId;
        netMessage.src = this.src;
        netMessage.dst = this.dst;
        netMessage.map.putAll(this.map);
        netMessage.list.addAll(this.list);
        return netMessage;
    }

    /*public static NetMessage parseResult(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof  Exception) {
            GatewayException exception = (GatewayException) object;
            return NetMessage.fail()
                    .setErrorCode(exception.getMessage())
                    .setErrorMSG(exception.getErrorMSG());
        }
        if (object instanceof Map) {
            return  NetMessage.success()
                    .putAll((Map) object);
        }
        if (object instanceof List) {
            return NetMessage.success()
                    .addAll((List) object);
        }
        throw new GameserverException("result parse netMessage error, object" + object);
    }*/

    /**
     * 修改errorMSG
     *
     * @param errorMSG
     * @return
     */
    public NetMessage errorMSG(String errorMSG) {
        this.errorMSG = errorMSG;
        return this;
    }

    public Map getParamter() {
        return map;
    }

    public String getErrorMSG() {
        return errorMSG;
    }

    public int getCommand() {
        return command;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "NetMessage{" +
                "command=" + command +
                ", userId=" + userId +
                ", topic='" + topic + '\'' +
                ", src='" + src + '\'' +
                ", dst='" + dst + '\'' +
                ", errorCode=" + errorCode +
                ", errorMSG='" + errorMSG + '\'' +
                ", isSuccess=" + isSuccess +
                ", object=" + object +
                ", map=" + map +
                ", list=" + list +
                '}';
    }
}
