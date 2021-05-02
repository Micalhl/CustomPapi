package me.mical.custompapi.data;

import lombok.Getter;
import lombok.ToString;
import me.mical.custompapi.config.ParamManager;
import me.mical.custompapi.config.struct.Param;
import me.mical.custompapi.data.struct.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.serverct.parrot.parrotx.data.PData;
import org.serverct.parrot.parrotx.data.PID;
import org.serverct.parrot.parrotx.data.PStruct;
import org.serverct.parrot.parrotx.data.autoload.annotations.PAutoload;
import org.serverct.parrot.parrotx.data.autoload.annotations.PAutoloadGroup;
import org.serverct.parrot.parrotx.utils.MapUtil;

import java.io.File;
import java.util.Map;

@Getter
@ToString
@PAutoloadGroup
public class PlayerData extends PData {

    private Map<String, Data> params;
    private String uuid;

    @PAutoload("Params")
    private Map<String, ConfigurationSection> rawParams;

    public PlayerData(PID id, File file, String uuid) {
        super(id, file, "玩家数据");
        this.uuid = uuid;
        load();
    }

    @Override
    public void load() {
        if (data.getConfigurationSection("Params") == null) {
            data.createSection("Params");
        }

        super.load();

        this.params = MapUtil.transformValue(this.rawParams,
                section -> new Data(new PID(plugin, "param", section.getName()), section));
    }

    @Override
    public void save() {
        params.values().forEach(PStruct::save);
        super.save();
    }

    public void addParam(@NotNull final Param param) {
        if (data.getConfigurationSection("Params." + param.getID().getId()) == null) {
            data.createSection("Params." + param.getID().getId());
        }

        final ConfigurationSection section = data.getConfigurationSection("Params." + param.getID().getId());

        final Data data = new Data(new PID(plugin, "data", section.getName()), section, System.currentTimeMillis(), ParamManager.getInstance().getParams().get(section.getName()).getDefaultValue());

        params.put(section.getName(), data);
        rawParams.put(section.getName(), section);
    }
}
