package me.mical.custompapi.config;

import me.mical.custompapi.CustomPapi;
import me.mical.custompapi.data.PlayerData;
import org.serverct.parrot.parrotx.api.ParrotXAPI;
import org.serverct.parrot.parrotx.config.PFolder;
import org.serverct.parrot.parrotx.data.autoload.annotations.PAutoload;
import org.serverct.parrot.parrotx.utils.FileUtil;

import java.io.File;

@PAutoload
public class DataManager extends PFolder<PlayerData> {

    public DataManager() {
        super(
                ParrotXAPI.getPlugin(CustomPapi.class),
                "data",
                "玩家数据文件夹",
                "userdata"
        );
    }

    public static DataManager getInstance() {
        return ParrotXAPI.getConfigManager(DataManager.class);
    }

    @Override
    public PlayerData loadFromDataFile(File file) {
        String uuid = FileUtil.getNoExFilename(file);
        return new PlayerData(buildId(uuid), file, uuid);
    }
}
