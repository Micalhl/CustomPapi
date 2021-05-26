package me.mical.custompapi.sql;

public interface Dao {

    /**
     * 连接数据库.
     */
    void connect();

    /**
     * 创建数据.
     *
     * @param uuid      玩家 UUID
     * @param name      Param 名称
     */
    void create(String uuid, String name);

    /**
     * 修改数据.
     *
     * @param uuid      玩家 UUID
     * @param name      Param 名称
     * @param timestamp 更改后的刷新时间
     * @param value     更改后的数值
     */
    void edit(String uuid, String name, long timestamp, double value);

    /**
     * 获取时间戳.
     *
     * @param uuid 玩家 UUID
     * @param name Param 名称
     * @return 刷新时间戳
     */
    Long getTimestamp(String uuid, String name);

    /**
     * 获取数值.
     *
     * @param uuid 玩家 UUID
     * @param name Param 名称
     * @return 变量数值
     */
    Double getValue(String uuid, String name);

    /**
     * 是否存在变量
     *
     * @param uuid 玩家 UUID
     * @param name Param 名称
     * @return 是否存在
     */
    Boolean has(String uuid, String name);

}
