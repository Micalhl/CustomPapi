package me.mical.custompapi;

import lombok.Getter;
import me.mical.custompapi.config.ConfigManager;
import me.mical.custompapi.expansion.ParamExpansion;
import me.mical.custompapi.sql.DaoManager;
import me.mical.custompapi.task.RefreshTask;
import me.mical.custompapi.utils.StorageUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.serverct.parrot.parrotx.PPlugin;

import java.util.ArrayList;
import java.util.List;

public final class CustomPapi extends PPlugin {

    @Getter
    private static List<String> addons = new ArrayList<>();

    @Getter
    private static RefreshTask refreshTask;

    @Override
    protected void preload() {
        pConfig = ConfigManager.getInstance();
    }

    @Override
    protected void load() {
        DaoManager.getDao().connect();
        refreshTask = new RefreshTask(this);
        refreshTask.run();
        registerExpansion(new ParamExpansion());
        registerStats(8623, null);
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            StorageUtil.initPlayerData(offlinePlayer.getUniqueId());
        }
    }
}
