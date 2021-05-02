package me.mical.custompapi.task;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import me.mical.custompapi.config.DataManager;
import me.mical.custompapi.config.ParamManager;
import me.mical.custompapi.config.struct.Param;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.utils.i18n.I18n;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class RefreshTask {

    private PPlugin plugin;
    private I18n lang;
    private Map<String, Map<String, Integer>> dataMap = new HashMap<>();

    public RefreshTask(@NonNull PPlugin plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLang();
    }

    public void run() {
        DataManager.getInstance().getDataMap().forEach((pid, playerData) -> playerData.getParams().forEach((name, data) -> {
            final Param param = ParamManager.getInstance().getParams().get(name);
            if (param.isEnableRefresh()) {
                final BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        final long current = System.currentTimeMillis();
                        if ((current - data.getTimestamp()) >= param.getRefreshTime() * 1000L) {
                            data.setValue(param.getDefaultValue());
                            data.setTimestamp(current);
                            lang.log.debug("已成功为玩家 &c{0} &f刷新 &c{1} &f变量.", playerData.getUuid(), name);
                        }
                    }
                };
                runnable.runTaskTimer(plugin, 0L, 20L);
                final Map<String, Integer> current = dataMap.getOrDefault(playerData.getUuid(), new HashMap<>());
                current.put(name, runnable.getTaskId());
                dataMap.put(playerData.getUuid(), current);
            }
        }));
    }

    public void clearAllTasks() {
        dataMap.forEach((string, map) -> map.forEach((name, id) -> {
            plugin.getServer().getScheduler().cancelTask(id);
            lang.log.debug("已成功为玩家 &c{0} &f取消 &c{1} &f变量的刷新任务(TaskID: &c{2}&f).", string, name, id);
        }));
    }
}
