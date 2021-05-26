package me.mical.custompapi.task;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import me.mical.custompapi.config.ParamManager;
import me.mical.custompapi.sql.DaoManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.utils.i18n.I18n;

@Getter
@ToString
public class RefreshTask {

    private PPlugin plugin;
    private I18n lang;
    private BukkitRunnable runnable;
    private int taskID = -1;

    public RefreshTask(@NonNull PPlugin plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLang();
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                    ParamManager.getInstance().getParams().forEach((name, param) -> {
                        if (param.isEnableRefresh()) {
                            final long current = System.currentTimeMillis();
                            if ((current - DaoManager.getDao().getTimestamp(offlinePlayer.getUniqueId().toString(), name) >= (param.getRefreshTime() * 1000L))) {
                                DaoManager.getDao().edit(offlinePlayer.getUniqueId().toString(), name, current, param.getDefaultValue());
                                lang.log.debug("已成功为玩家 &c{0}&f(&c{1}&f) 刷新 &c{2} &f变量.", offlinePlayer.getName(), offlinePlayer.getUniqueId().toString(), name);
                            }
                        }
                    });
                }
            }
        };
    }

    public void run() {
        if (taskID != -1) {
            Bukkit.getScheduler().cancelTask(taskID);
        }
        runnable.runTaskTimerAsynchronously(plugin, 0L, 20L);
        this.taskID = runnable.getTaskId();
    }

}
