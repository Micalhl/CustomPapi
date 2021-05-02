package me.mical.custompapi.utils;

import lombok.NonNull;
import me.mical.custompapi.config.DataManager;
import me.mical.custompapi.config.ParamManager;
import me.mical.custompapi.data.PlayerData;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class StorageUtil {

    public static void initPlayerData(@NonNull final Player player) {
        final UUID uuid = player.getUniqueId();
        final DataManager dataManager = DataManager.getInstance();
        if (!(dataManager.has(uuid.toString()))) {
            final PlayerData data = new PlayerData(
                    dataManager.buildId(uuid.toString()),
                    new File(
                            dataManager.getFile(),
                            uuid + ".yml"
                    ),
                    uuid.toString()
            );
            ParamManager.getInstance().getParams().values().forEach(data::addParam);
            dataManager.put(data);
        }
    }
}
