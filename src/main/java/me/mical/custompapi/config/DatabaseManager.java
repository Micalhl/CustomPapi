package me.mical.custompapi.config;

import me.mical.custompapi.CustomPapi;
import org.serverct.parrot.parrotx.api.ParrotXAPI;
import org.serverct.parrot.parrotx.config.PConfig;
import org.serverct.parrot.parrotx.data.autoload.annotations.PAutoload;
import org.serverct.parrot.parrotx.data.autoload.annotations.PAutoloadGroup;

@PAutoload
@PAutoloadGroup
public class DatabaseManager extends PConfig {

    @PAutoload("EnableMySQL")
    public static boolean enableMysql;
    @PAutoload("SQLAddress")
    public static String sqlAddress;
    @PAutoload("SQLPort")
    public static int sqlPort;
    @PAutoload("SQLDatabase")
    public static String sqlDatabase;
    @PAutoload("SQLName")
    public static String sqlName;
    @PAutoload("SQLPassword")
    public static String sqlPassword;

    public DatabaseManager() {
        super(ParrotXAPI.getPlugin(CustomPapi.class), "database", "MySQL 配置文件");
        readOnly(true);
    }
}
