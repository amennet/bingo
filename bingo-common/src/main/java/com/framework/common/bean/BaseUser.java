package com.framework.common.bean;

import io.netty.channel.Channel;

import java.io.Serializable;

/**
 * Created by ZhangGe on 2017/4/26.
 */
public class BaseUser implements Serializable{

    private long lastUpdateTimestamp;
    private Channel channel;
    private String topic;  // 123456789
    private String userId;  // 123456789
    private String site;  // serverName:ip:port

    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(long lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
