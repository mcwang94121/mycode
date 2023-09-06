package com.example.demo.tcpTest.netty1.netty;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AlcoholAccessHandler
 *
 * @author mc
 * @date 2023年08月22日
 */
@Component
public class AlcoholDetectionChannelManager {

    /**
     * 存储活动的Channel的集合
     */
    private final Map<String, Channel> echaMap;

    private AlcoholDetectionChannelManager() {
        this.echaMap = new ConcurrentHashMap<>();
    }

    private static volatile AlcoholDetectionChannelManager instance = null;

    public static AlcoholDetectionChannelManager getInstance() {
        if (instance == null) {
            synchronized (AlcoholDetectionChannelManager.class) {
                instance = new AlcoholDetectionChannelManager();
                return instance;
            }
        }
        return instance;
    }

    /**
     * 建立会话，保存连接映射
     */
    public void bindChannel(String ip, Channel channel) {
        if (this.echaMap.get(ip) == null) {
            echaMap.put(ip, channel);
        }
    }

    /**
     * 移除管道
     */
    public void unBindChannel(String ip) {
        this.echaMap.remove(ip);
    }

    /**
     * 获取管道
     */
    public Channel getChannel(String ip) {
        return this.echaMap.get(ip);
    }

    /**
     * 获取所有管道，主要用于系统消息
     */
    public List<Channel> getAllChannel() {
        List<Channel> channelList = new ArrayList<>();
        for (Channel item : echaMap.values()) {
            if (item != null && item.isActive()) {
                channelList.add(item);
            }
        }
        return channelList;
    }

}
