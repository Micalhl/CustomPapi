package me.mical.custompapi;

import me.mical.custompapi.config.ConfigManager;
import me.mical.custompapi.expansion.ParamExpansion;
import me.mical.custompapi.task.RefreshTask;
import me.mical.custompapi.utils.StorageUtil;
import org.bukkit.Bukkit;
import org.serverct.parrot.parrotx.PPlugin;

public final class CustomPapi extends PPlugin {

    @Override
    protected void preload() {
        pConfig = ConfigManager.getInstance();
    }

    @Override
    protected void load() {
        final RefreshTask refreshTask = new RefreshTask(this);
        refreshTask.clearAllTasks();
        refreshTask.run();
        registerExpansion(new ParamExpansion());
        registerStats(8623, null);
        Bukkit.getOnlinePlayers().forEach(StorageUtil::initPlayerData);
    }
}
