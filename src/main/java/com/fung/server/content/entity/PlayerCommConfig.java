package com.fung.server.content.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author skytrc@163.com
 * @date 2020/6/4 11:56
 */
@Entity
@Table(name = "player_config")
public class PlayerCommConfig {

    @Id
    private String uuid;

    @Column(name = "max_backpack_grid")
    private int maxBackpackGrid;

    public int getMaxBackpackGrid() {
        return maxBackpackGrid;
    }

    public void setMaxBackpackGrid(int maxBackpackGrid) {
        this.maxBackpackGrid = maxBackpackGrid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}