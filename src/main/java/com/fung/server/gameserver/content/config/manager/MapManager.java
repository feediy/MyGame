package com.fung.server.gameserver.content.config.manager;

import com.fung.server.gameserver.content.config.readconfig.ReadMap;
import com.fung.server.gameserver.content.config.readconfig.ReadMapGates;
import com.fung.server.gameserver.content.config.map.GameMap;
import com.fung.server.gameserver.content.config.map.GameMapGates;
import com.fung.server.gameserver.content.domain.mapactor.GameMapActor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author skytrc@163.com
 * @date 2020/4/30 9:45
 */
@Component
public class MapManager {

    /**
     * 用于记录地图  key 地图id  value 地图
     */
    private Map<Integer, GameMap> gameMapCollection;

    /**
     * GameMapActor 封装Map，用于申请多线程处理地图中的时间
     */
    private Map<Integer, GameMapActor> gameMapActorMap;

    private static final Logger LOGGER = LoggerFactory.getLogger(MapManager.class);

    @Autowired
    private ReadMap readMap;

    @Autowired
    private ReadMapGates readMapGates;

    public MapManager() {
        gameMapActorMap = new HashMap<>();
    }

    public void mapInit() throws IOException, InvalidFormatException {
        readMap.init();
        readMapGates.init();

        // 初始化各张地图实体
        gameMapCollection = readMap.getModelMap();
        gameMapCollection.forEach((id,item) -> {
            item.setElements(new HashMap<>());
            item.setGates(new HashMap<>());
            item.setPlayerInPosition(new HashMap<>());
            item.setMonsterMap(new HashMap<>());

            // 封装到MapActor中
            GameMapActor gameMapActor = new GameMapActor();
            gameMapActor.setGameMap(item);
            gameMapActorMap.put(id, gameMapActor);
        });

        // 增加传送门
        Map<Integer, GameMapGates> gamMapGates = readMapGates.getModelMap();
        gamMapGates.forEach((id, item) -> {
//            gameMapCollection.get(item.getThisMapId()).addGate(item.getLocation(), gameMapCollection.get(item.getNextMapId()));
            GameMap currentMap = gameMapCollection.get(item.getThisMapId());
            GameMap nextMap = gameMapCollection.get(item.getNextMapId());
            currentMap.addGate(item.getLocation(), nextMap);
        });

        // 增加Element

        LOGGER.info("地图初始化成功");
    }

    /**
     * 初始化一张地图的基本信息
     * @param gameMap 需要初始化地图实例
     * @param id 地图id
     * @param name 地图名字
     * @param x x轴
     * @param y y轴
     */
    @Deprecated
    public void setMapBasicInfo(GameMap gameMap, int id, String name, int x, int y) {
        gameMap.setId(id);
        gameMap.setName(name);
        gameMap.setX(x);
        gameMap.setY(y);
        gameMap.setElements(new HashMap<>(x * y));
        gameMap.setGates(new HashMap<>());
    }

    public GameMap getMapByMapId(int i) {
        return gameMapCollection.get(i);
    }

    public GameMapActor getGameMapActorById(int id) {
        return gameMapActorMap.get(id);
    }

    public Map<Integer, GameMap> getGameMapCollection() {
        return gameMapCollection;
    }

    public void setGameMapCollection(Map<Integer, GameMap> gameMapCollection) {
        this.gameMapCollection = gameMapCollection;
    }
}