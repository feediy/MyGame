package com.fung.server.gameserver.content.entity;

import com.fung.server.gameserver.content.config.good.equipment.EquipmentType;

import javax.persistence.*;

/**
 * @author skytrc@163.com
 * @date 2020/6/1 15:05
 */
@Entity(name = "equipment")
@DiscriminatorValue("equipment")
public class Equipment extends Good {

    /**
     * 强化等级
     */
    private int level;

    /**
     * 装备类型
     */
    @Transient
    private EquipmentType type;

    /**
     * 装备最大耐久度
     */
    @Transient
    private int maxDurable;

    /**
     * 装备目前耐久度
     */
    private int durable;

    /**
     * 装备穿戴的最低等级
     */
    @Transient
    private int minLevel;

    /**
     * 装备增加血量
     */
    @Column(name = "plus_hp")
    private int plusHp;

    /**
     * 装备增加魔法值
     */
    @Column(name = "plus_mp")
    private int plusMp;

    /**
     * 装备增加攻击力
     */
    @Column(name = "attack_power")
    private int attackPower;

    /**
     * 装备增加魔法力
     */
    @Column(name = "magic_power")
    private int magicPower;

    /**
     * 装备增加暴击率
     */
    @Column(name = "critical_rate")
    private float criticalRate;

    /**
     * 装备增加防御力
     */
    private int defense;

    /**
     * 词条
     */
    @Column(name = "entries_num")
    private String entriesNum;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public EquipmentType getType() {
        return type;
    }

    public void setType(EquipmentType type) {
        this.type = type;
    }

    public int getMaxDurable() {
        return maxDurable;
    }

    public void setMaxDurable(int maxDurable) {
        this.maxDurable = maxDurable;
    }

    public int getDurable() {
        return durable;
    }

    public void setDurable(int durable) {
        this.durable = durable;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    public int getPlusHp() {
        return plusHp;
    }

    public void setPlusHp(int plusHp) {
        this.plusHp = plusHp;
    }

    public int getPlusMp() {
        return plusMp;
    }

    public void setPlusMp(int plusMp) {
        this.plusMp = plusMp;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getMagicPower() {
        return magicPower;
    }

    public void setMagicPower(int magicPower) {
        this.magicPower = magicPower;
    }

    public float getCriticalRate() {
        return criticalRate;
    }

    public void setCriticalRate(float criticalRate) {
        this.criticalRate = criticalRate;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public String getEntriesNum() {
        return entriesNum;
    }

    public void setEntriesNum(String entriesNum) {
        this.entriesNum = entriesNum;
    }
}
