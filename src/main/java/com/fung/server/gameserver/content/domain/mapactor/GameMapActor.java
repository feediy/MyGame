package com.fung.server.gameserver.content.domain.mapactor;

import com.fung.server.gameserver.content.config.map.Dungeon;
import com.fung.server.gameserver.content.config.map.GameMap;
import com.fung.server.gameserver.message.MessageHandler;

/**
 * GameMapActor 封装Map，用于申请多线程处理地图中的时间
 * @author skytrc@163.com
 * @date 2020/7/7 16:26
 */
public class GameMapActor extends MessageHandler<GameMapActor> implements IGameMap<GameMapActor> {

    private Dungeon gameMap;

    @Override
    public String getName() {
        return gameMap.getName();
    }

    @Override
    public int getId() {
        return gameMap.getId();
    }

    @Override
    public String getUuid() {
        return gameMap.getUuid();

    }

    public Dungeon getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = (Dungeon) gameMap;
    }

}
