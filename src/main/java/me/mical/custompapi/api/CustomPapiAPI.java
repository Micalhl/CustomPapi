package me.mical.custompapi.api;

import me.mical.custompapi.CustomPapi;
import me.mical.custompapi.config.DataManager;
import me.mical.custompapi.config.ParamManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.serverct.parrot.parrotx.api.ParrotXAPI;

import java.util.UUID;

public class CustomPapiAPI {

    private final JavaPlugin plugin;

    /**
     * 关联 CustomPapi.
     * @param plugin 你的插件的主类实例.
     */
    public CustomPapiAPI(JavaPlugin plugin) {
        this.plugin = plugin;
        CustomPapi.getAddons().add(plugin.getName());
        ParrotXAPI.getPlugin(CustomPapi.class).getLang().log.info("已挂钩插件 &c" + plugin.getName() + "&f.");
    }

    /**
     * 获取玩家的变量.
     * @param param 变量
     * @param player 玩家
     * @return 变量值, 不存在返回0
     */
    public double get(String param, UUID player) {
        if (DataManager.getInstance().get(player.toString()).getParams().containsKey(param)) {
            return DataManager.getInstance().get(player.toString()).getParams().get(param).getValue();
        }
        return 0;
    }

    /**
     * 为玩家添加变量
     * @param param 变量
     * @param player 玩家
     * @param value 添加值
     * @return 是否成功（false则为不存在)
     */
    public boolean add(String param, UUID player, double value) {
        if (ParamManager.getInstance().getParams().containsKey(param)) {
            DataManager.getInstance().get(player.toString()).getParams().get(param).addValue(value);
            return true;
        }
        return false;
    }

    /**
     * 为玩家减少变量
     * @param param 变量
     * @param player 玩家
     * @param value 添加值
     * @return 是否成功（false则为不存在)
     */
    public boolean take(String param, UUID player, double value) {
        if (ParamManager.getInstance().getParams().containsKey(param)) {
            DataManager.getInstance().get(player.toString()).getParams().get(param).takeValue(value);
            return true;
        }
        return false;
    }

    /**
     * 为玩家设置变量
     * @param param 变量
     * @param player 玩家
     * @param value 添加值
     * @return 是否成功（false则为不存在)
     */
    public boolean set(String param, UUID player, double value) {
        if (ParamManager.getInstance().getParams().containsKey(param)) {
            DataManager.getInstance().get(player.toString()).getParams().get(param).setValue(value);
            return true;
        }
        return false;
    }
}
