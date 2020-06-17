package com.fung.server.content.service;

/**
 * @author skytrc@163.com
 * @date 2020/6/17 17:13
 */
public interface NpcService {

    /**
     * 返回NPC所有的选项
     * @param channelId channel id
     * @return NPC所有的选项
     */
    String allChoose(String channelId);

    /**
     * 返回选项内容
     * @param channelId channel id
     * @param choose 选项
     * @return 选项内容
     */
    String oneChoose(String channelId, int choose);
}
