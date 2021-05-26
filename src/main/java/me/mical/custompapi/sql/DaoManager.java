package me.mical.custompapi.sql;

import me.mical.custompapi.config.DatabaseManager;
import me.mical.custompapi.sql.impl.MySQL;
import me.mical.custompapi.sql.impl.SQLite;

public class DaoManager {

    /**
     * 获取数据库处理类.
     */
    public static Dao getDao() {
        if (DatabaseManager.enableMysql) {
            return new MySQL();
        }
        return new SQLite();
    }
}
