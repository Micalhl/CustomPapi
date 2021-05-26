package me.mical.custompapi.sql.impl;

import me.mical.custompapi.CustomPapi;
import me.mical.custompapi.config.ParamManager;
import me.mical.custompapi.sql.Dao;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.api.ParrotXAPI;
import org.serverct.parrot.parrotx.utils.i18n.I18n;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Function;

public class SQLite implements Dao {

    private PPlugin plugin;
    private I18n lang;

    /**
     * 构造 PPlugin 和 I18n.
     */
    public SQLite() {
        this.plugin = ParrotXAPI.getPlugin(CustomPapi.class);
        this.lang = plugin.getLang();
    }

    @Nullable
    private Connection getConnection() {
        try {
            final SQLiteConfig config = new SQLiteConfig();
            config.setSharedCache(true);
            config.enableRecursiveTriggers(true);
            final SQLiteDataSource dataSource = new SQLiteDataSource(config);
            final String url = System.getProperty("user.dir");
            dataSource.setUrl("jdbc:sqlite:" + url + "/plugins/CustomPapi/" + "userdata.db");
            return dataSource.getConnection();
        } catch (SQLException e) {
            lang.log.error(I18n.GET, "数据库链接", e, plugin.getPackageName());
            return null;
        }
    }

    /**
     * 连接数据库.
     */
    @Override
    public void connect() {
        execute(
                "CREATE TABLE IF NOT EXISTS `cp_user_data` (`uuid` VARCHAR(255) NOT NULL,`param` TEXT NOT NULL,`timestamp` LONG,`value` DOUBLE);",
                null
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
            execute(
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
        execute(
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
        return query(
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
        return query(
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
        return query(
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

    public void execute(@NotNull final String sql, @Nullable final Function<PreparedStatement, PreparedStatement> args) {
        final Connection connection = getConnection();

        if (Objects.isNull(connection)) {
            return;
        }

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            if (Objects.nonNull(args)) {
                statement = args.apply(statement);
            }
            if (Objects.isNull(statement)) {
                return;
            }
            statement.execute();
        } catch (SQLException e) {
            lang.log.error(I18n.EXECUTE, "SQLite 语句", e, plugin.getPackageName());
        } finally {
            close(connection, statement, null);
        }
    }

    @Nullable
    public <T> T query(@NotNull final String sql,
                       @NotNull final Function<ResultSet, T> getter,
                       @Nullable final Function<PreparedStatement, PreparedStatement> args) {
        final Connection connection = getConnection();
        if (Objects.isNull(connection)) {
            return null;
        }

        PreparedStatement statement = null;
        ResultSet result = null;
        T value = null;
        try {
            statement = connection.prepareStatement(sql);
            if (Objects.nonNull(args)) {
                statement = args.apply(statement);
            }
            if (Objects.isNull(statement)) {
                return null;
            }
            result = statement.executeQuery();
            value = getter.apply(result);
        } catch (SQLException exception) {
            plugin.getLang().log.error(I18n.EXECUTE, "SQLite 语句", exception, plugin.getPackageName());
        } finally {
            close(connection, statement, result);
        }
        return value;
    }

    public void close(@Nullable final Connection connection,
                      @Nullable final PreparedStatement statement,
                      @Nullable final ResultSet result) {
        try {
            if (Objects.nonNull(connection)) {
                connection.close();
            }
            if (Objects.nonNull(statement)) {
                statement.close();
            }
            if (Objects.nonNull(result)) {
                result.close();
            }
        } catch (final SQLException e) {
            lang.log.error("关闭", "数据库链接", e, "serverct");
        }
    }
}
