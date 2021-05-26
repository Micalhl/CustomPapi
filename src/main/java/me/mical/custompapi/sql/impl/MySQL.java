package me.mical.custompapi.sql.impl;

import me.mical.custompapi.CustomPapi;
import me.mical.custompapi.config.DatabaseManager;
import me.mical.custompapi.config.ParamManager;
import me.mical.custompapi.sql.Dao;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.api.ParrotXAPI;
import org.serverct.parrot.parrotx.utils.HikariCPUtil;
import org.serverct.parrot.parrotx.utils.i18n.I18n;

import java.sql.SQLException;

public class MySQL implements Dao {

    private PPlugin plugin;
    private I18n lang;

    /**
     * 构造 PPlugin 和 I18n.
     */
    public MySQL() {
        this.plugin = ParrotXAPI.getPlugin(CustomPapi.class);
        this.lang = plugin.getLang();
    }

    /**
     * 连接数据库.
     */
    @Override
    public void connect() {
        HikariCPUtil.setSqlConnectionPool(
                plugin,
                DatabaseManager.sqlAddress,
                String.valueOf(DatabaseManager.sqlPort),
                DatabaseManager.sqlDatabase,
                DatabaseManager.sqlName,
                DatabaseManager.sqlPassword
        );
        HikariCPUtil.execute(
                plugin,
                "CREATE TABLE IF NOT EXISTS `cp_user_data` (`uuid` VARCHAR(255) NOT NULL,`param` TEXT NOT NULL,`timestamp` LONG,`value` DOUBLE);"
                , null
        );
    }

    /**
     * 创建数据.
     *
     * @param uuid 玩家 UUID
     * @param name Param 名称
     */
    @Override
    public void create(String uuid, String name) {
        if (!has(uuid, name)) {
            HikariCPUtil.execute(
                    plugin,
                    "INSERT INTO cp_user_data (uuid,param,timestamp,value) VALUES ('" + uuid + "','" + name + "'," + System.currentTimeMillis() + "," + ParamManager.getInstance().getParams().get(name).getDefaultValue() + ");",
                    null
            );
        }
    }

    /**
     * 修改数据.
     *
     * @param uuid      玩家 UUID
     * @param name      Param 名称
     * @param timestamp 更改后的刷新时间
     * @param value     更改后的数值
     */
    @Override
    public void edit(String uuid, String name, long timestamp, double value) {
        HikariCPUtil.execute(
                plugin,
                "UPDATE cp_user_data SET timestamp = " + timestamp + ", value = " + value + " WHERE uuid = '" + uuid + "' AND param = '" + name + "';",
                null
        );
    }

    /**
     * 获取时间戳.
     *
     * @param uuid 玩家 UUID
     * @param name Param 名称
     * @return 刷新时间戳
     */
    @Override
    public Long getTimestamp(String uuid, String name) {
        return HikariCPUtil.query(
                plugin,
                "SELECT `timestamp` FROM `cp_user_data` WHERE `uuid` = '" + uuid + "' AND param = '" + name + "';",
                resultSet -> {
                    if (resultSet == null) {
                        return 0L;
                    }
                    try {
                        if (resultSet.next()) {
                            return resultSet.getLong(1);
                        }
                    } catch (SQLException e) {
                        lang.log.error(I18n.EXECUTE, "MySQL 语句", e, plugin.getPackageName());
                    }
                    return 0L;
                },
                null
        );
    }

    /**
     * 获取数值.
     *
     * @param uuid 玩家 UUID
     * @param name Param 名称
     * @return 变量数值
     */
    @Override
    public Double getValue(String uuid, String name) {
        return HikariCPUtil.query(
                plugin,
                "SELECT `value` FROM `cp_user_data` WHERE `uuid` = '" + uuid + "' AND param = '" + name + "';",
                resultSet -> {
                    if (resultSet == null) {
                        return 0d;
                    }
                    try {
                        if (resultSet.next()) {
                            return resultSet.getDouble(1);
                        }
                    } catch (SQLException e) {
                        lang.log.error(I18n.EXECUTE, "MySQL 语句", e, plugin.getPackageName());
                    }
                    return 0d;
                },
                null
        );
    }

    /**
     * 是否存在变量
     *
     * @param uuid 玩家 UUID
     * @param name Param 名称
     * @return 是否存在
     */
    @Override
    public Boolean has(String uuid, String name) {
        return HikariCPUtil.query(
                plugin,
                "SELECT * FROM cp_user_data WHERE uuid = '" + uuid + "' AND param = '" + name + "';",
                resultSet -> {
                    if (resultSet == null) {
                        return false;
                    }
                    try {
                        return resultSet.next();
                    } catch (SQLException e) {
                        lang.log.error(I18n.EXECUTE, "MySQL 语句", e, plugin.getPackageName());
                    }
                    return false;
                },
                null
        );
    }
}
