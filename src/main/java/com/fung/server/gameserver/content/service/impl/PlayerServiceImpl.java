package com.fung.server.gameserver.content.service.impl;

import com.fung.server.gameserver.cache.mycache.PlayerCache;
import com.fung.server.gameserver.content.config.good.equipment.EquipmentCreated;
import com.fung.server.gameserver.content.config.good.equipment.EquipmentType;
import com.fung.server.gameserver.content.config.manager.EquipmentCreatedManager;
import com.fung.server.gameserver.content.config.manager.MapManager;
import com.fung.server.gameserver.content.dao.EquipmentDao;
import com.fung.server.gameserver.content.dao.GoodDao;
import com.fung.server.gameserver.content.dao.SkillDao;
import com.fung.server.gameserver.content.domain.backpack.PersonalBackpack;
import com.fung.server.gameserver.content.domain.calculate.PlayerValueCalculate;
import com.fung.server.gameserver.content.domain.player.PlayerCreated;
import com.fung.server.gameserver.content.entity.*;
import com.fung.server.gameserver.content.service.PlayerService;
import com.fung.server.gameserver.content.domain.player.OnlinePlayer;
import com.fung.server.gameserver.content.domain.player.PlayerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author skytrc@163.com
 * @date 2020/5/18 21:05
 */
@Component
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerCache playerCache;

    @Autowired
    PlayerCreated playerCreated;

    @Autowired
    PlayerInfo playerInfo;

    @Autowired
    PlayerValueCalculate playerValueCalculate;

    @Autowired
    OnlinePlayer onlinePlayer;

    @Autowired
    MapManager mapManager;

    @Autowired
    EquipmentCreatedManager createdManager;

    @Autowired
    SkillDao skillDao;

    @Autowired
    EquipmentDao equipmentDao;

    @Autowired
    GoodDao goodDao;

    @Override
    public String register(String playerName, String password) {
        Player player = playerCache.getPlayerByPlayerName(playerName);
        if (player != null) {
            return "角色已存在，请重新注册";
        }

        // 角色新建模块
        Player newPlayer = playerCreated.createPlayer(playerName, password);
        playerCache.createPlayer(newPlayer);

        // 插入数据库后再重新获取player,为了获取uuid
        Player player1 = playerCache.getPlayerByPlayerName(newPlayer.getPlayerName());
        // 新建、插入、关联config
        PlayerCommConfig playerCommConfig = playerCreated.playerCommConfigCreated(player1);
        playerCache.insertPlayerCommConfig(playerCommConfig);
        player1.setPlayerCommConfig(playerCommConfig);
        playerCreated.playerModuleCreated(player1, goodDao, equipmentDao, skillDao);

        return "角色创建完毕";
    }

    @Override
    public String login(String playerName, String password, String channelId) {
        Player player = playerCache.getPlayerByPlayerName(playerName);
        if (player == null) {
            return "不存在该名玩家";
        }
        if (!player.getPassword().equals(password)) {
            return "角色名或密码不正确";
        }
        // 把登录的玩家放在onlinePlayerMap中，与channel捆绑，方便操作
        Player playerLogin = playerCache.getPlayerByPlayerName(playerName);
        // 更新登录日期
        playerLogin.setLoginDate(System.currentTimeMillis());

        // 挂载
        playerLoad(player, player.getPlayerCommConfig());

        playerValueCalculate.calculatePlayerBaseValue(player);

        // onlinePlayer -> 指向PlayerCache的玩家实体
        onlinePlayer.getPlayerMap().put(channelId, playerLogin);
        // 地图Map捆绑线上玩家
        mapManager.getMapByMapId(playerLogin.getInMapId()).addPlayer(playerLogin);
        return "登录成功：\n " + playerInfo.showPlayerInfo(player);
    }

    @Override
    public String logout(String channelId) {
        if (onlinePlayer.getPlayerMap().containsKey(channelId)) {
            Player player = onlinePlayer.getPlayerMap().remove(channelId);
            // 地图 在线Map 移除该玩家
            playerInfo.getCurrentPlayerMap(channelId).removePlayer(player);
            return "登出成功";
        }
        return "没有角色登录";
    }

    public void playerLoad(Player player, PlayerCommConfig playerCommConfig) {
        // 挂载技能
        if (player.getSkills() == null) {
            List<Skill> skills = skillDao.findSkillsByPlayerId(player.getUuid());
            player.setSkills(skills);
        }
        Map<Integer, EquipmentCreated> equipmentCreatedMap = createdManager.getEquipmentCreatedMap();
        // 挂载装备
        if (player.getEquipments() == null) {
            player.setEquipments(new ArrayList<>(5));
            List<Equipment> equipments = equipmentDao.findEquipmentsByPlayerId(player.getUuid());
            equipments.forEach(equipment -> setEquipmentType(equipment, equipmentCreatedMap));
            player.setEquipments(equipments);
        }
        // 背包
        if (player.getPersonalBackpack() == null) {
            List<Good> goods = goodDao.getGoodByPlayerId(player.getUuid());
            List<Equipment> equipmentList = goodDao.findBackpackEquipment(player.getUuid());

            PersonalBackpack personalBackpack = new PersonalBackpack();
            // 设置背包最大格子数
            personalBackpack.setMaxBackpackGrid(playerCommConfig.getMaxBackpackGrid());
            Map<Integer, Good> backpack = personalBackpack.getBackpack();
            Map<Integer, Equipment> equipmentMap = personalBackpack.getEquipmentMap();
            // 背包物品放入背包中，装备放入equipmentMap中
            goods.forEach(value -> {
                backpack.put(value.getPosition(), value);
            });
            equipmentList.forEach(value -> {
                setEquipmentType(value, equipmentCreatedMap);
                backpack.put(value.getPosition(), value);
                equipmentMap.put(value.getPosition(), value);
            });
            player.setPersonalBackpack(personalBackpack);
        }
    }

    public void setEquipmentType(Equipment equipment, Map<Integer, EquipmentCreated> equipmentCreatedMap) {
        EquipmentCreated equipmentCreated = equipmentCreatedMap.get(equipment.getGoodId());
        String type = equipmentCreated.getType();
        equipment.setName(equipmentCreated.getName());
        equipment.setDescription(equipmentCreated.getDescription());
        equipment.setType(EquipmentType.getEquipmentTypeByPartName(type));
    }
}