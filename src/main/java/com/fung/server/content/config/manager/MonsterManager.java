package com.fung.server.content.config.manager;

import com.fung.server.content.config.monster.Monster;
import com.fung.server.content.config.readconfig.ReadMonster;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author skytrc@163.com
 * @date 2020/6/3 15:35
 */
@Component
public class MonsterManager {

    private Map<Integer, Monster> monsterMap;

    @Autowired
    private ReadMonster readMonster;

    public void monsterInit() throws IOException, InvalidFormatException {
        readMonster.init();

        monsterMap = readMonster.getModelMap();
    }

    public Monster getMonsterById(int monsterId) {
        return monsterMap.get(monsterId);
    }

    public Map<Integer, Monster> getMonsterMap() {
        return monsterMap;
    }

    public void setMonsterMap(Map<Integer, Monster> monsterMap) {
        this.monsterMap = monsterMap;
    }
}