package com.fung.server.gameserver.content.dao.impl;

import com.fung.server.gameserver.content.entity.Player;
import com.fung.server.gameserver.content.dao.PlayerDao;
import com.fung.server.gameserver.content.entity.PlayerCommConfig;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;


/**
 * @author skytrc@163.com
 * @date 2020/5/11 12:05
 */
@Transactional
@Component
public class PlayerDaoImpl extends HibernateDaoSupport implements PlayerDao {

    @Autowired
    public void setSuperSessionFactory(SessionFactory superSessionFactory) {
        super.setSessionFactory(superSessionFactory);
    }

    @Override
    public List<String> getAllPlayerId() {
        Session session = this.getSessionFactory().openSession();
        try {
            return session.createNativeQuery("SELECT player_id FROM player", String.class).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void playerRegister(Player player) {
        this.getHibernateTemplate().save(player);
    }

    @Override
    public boolean findPlayerInfo(Player player) {
        return this.getHibernateTemplate().findByExample(player).size() > 0;
    }

    @Override
    public Player getPlayerByPlayerNamePassword(String playerName, String password) {
        Session session = this.getSessionFactory().openSession();
        try {
            return session.createNativeQuery(
                    "SELECT * " +
                            "FROM player " +
                            "WHERE player_name= '" + playerName + "' AND password='" + password + "'", Player.class).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Player getPlayerAllInfo(Player player) {
        List<Player> players = this.getHibernateTemplate().findByExample(player);
        return players.remove(0);
    }

    @Override
    public Player getPlayerByPlayerName(String playerName) {
        Session session = this.getSessionFactory().openSession();
        try {
            return session.createNativeQuery(
                    "SELECT * " +
                            "FROM player " +
                            "WHERE player_name= '" + playerName + "';", Player.class).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void updatePlayer(Player player) {
        this.getHibernateTemplate().update(player);
    }

    @Override
    public PlayerCommConfig getPlayerCommConfigByPlayerId(String playerId) {
        Session session = this.getSessionFactory().openSession();
        try {
            return session.createNativeQuery(
                    "SELECT * " +
                            "FROM player_config " +
                            "WHERE player_id='" + playerId + "';"
                    , PlayerCommConfig.class)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void insertPlayerCommConfig(PlayerCommConfig playerCommConfig) {
        this.getHibernateTemplate().save(playerCommConfig);
    }

}
