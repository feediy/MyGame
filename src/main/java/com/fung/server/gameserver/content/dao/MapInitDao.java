package com.fung.server.gameserver.content.dao;

import com.fung.server.gameserver.content.config.map.GameMap;

/**
 * @author skytrc@163.com
 * @date 2020/5/14 11:36
 */
public interface MapInitDao {
    /**
     * 把地图信息导入数据库总
     * @param gameMap 地图信息
     */
    void saveMap(GameMap gameMap);
}
