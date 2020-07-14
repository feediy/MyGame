package com.fung.server.gameserver.content.service.impl;

import com.fung.server.gameserver.content.dao.EquipmentDao;
import com.fung.server.gameserver.content.domain.backpack.PersonalBackpack;
import com.fung.server.gameserver.content.domain.calculate.PlayerValueCalculate;
import com.fung.server.gameserver.content.domain.player.PlayerInfo;
import com.fung.server.gameserver.content.entity.Equipment;
import com.fung.server.gameserver.content.entity.Player;
import com.fung.server.gameserver.content.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author skytrc@163.com
 * @date 2020/6/8 18:02
 */
@Component
public class GoodServiceImpl implements GoodService {

    @Autowired
    PlayerInfo playerInfo;

    @Autowired
    PlayerValueCalculate playerValueCalculate;

    @Autowired
    EquipmentDao equipmentDao;

    @Override
    public String useGood(int position, String channelId) {
        return null;
    }

    @Override
    public String putOnEquipment(int backpackPosition, String channelId) {
        Player currentPlayer = playerInfo.getCurrentPlayer(channelId);
        PersonalBackpack backpack = currentPlayer.getPersonalBackpack();
        List<Equipment> currentPlayerEquipments = currentPlayer.getEquipments();
        if (backpack.getGoodByPosition(backpackPosition) == null) {
            return "该格子没有物品";
        }
        if (!backpack.isEquipment(backpackPosition)) {
            return "无法穿戴，该物品不是装备";
        }
        Equipment equipment = backpack.removeEquipment(backpackPosition);
        // 需要装备到身上的位置
        int bodyPosition = equipment.getType().getPosition();
        equipment.setPosition(-1);
        // 如果装备的位置有东西，先卸到背包中
        if (currentPlayer.getEquipments().size() > bodyPosition && currentPlayerEquipments.get(bodyPosition) != null) {
            backpack.addEquipment(currentPlayerEquipments.remove(bodyPosition));
        }
        // List如果不够需要扩容
//        if (currentPlayerEquipments.size() < bodyPosition) {
//            currentPlayerEquipments.add(bodyPosition, equipment);
//            List<Equipment> list = new ArrayList<>(4);
//            list.add(bodyPosition - 1, equipment);
//            Collections.copy(list, currentPlayerEquipments);
//            currentPlayer.setEquipments(list);
//        } else {
//            currentPlayerEquipments.add(bodyPosition, equipment);
//        }
        // 装备到身上的数组中
        currentPlayerEquipments.add(bodyPosition, equipment);

        equipmentDao.updateEquipment(equipment);
        // 计算基础数值
        playerValueCalculate.calculatePlayerBaseValue(currentPlayer);

        return "穿戴装备成功" + playerInfo.showPlayerValue(currentPlayer);
    }

    @Override
    public String takeOffEquipment(int equipmentPosition, String channelId) {
        Player currentPlayer = playerInfo.getCurrentPlayer(channelId);
        List<Equipment> equipments = currentPlayer.getEquipments();
        int size = equipments.size();
        if (size > equipmentPosition && equipments.get(equipmentPosition) != null) {
            // 放入背包
            Equipment equipment = equipments.remove(equipmentPosition);
            PersonalBackpack personalBackpack = currentPlayer.getPersonalBackpack();
            Equipment equipment1 = personalBackpack.addEquipment(equipment);
            equipmentDao.updateEquipment(equipment1);
            playerValueCalculate.calculatePlayerBaseValue(currentPlayer);
            return "脱下装备成功" + playerInfo.showPlayerValue(currentPlayer);
        }
        return "该位置没有装备";
    }
}
