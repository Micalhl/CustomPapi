package me.mical.custompapi.config;

import lombok.Getter;
import lombok.ToString;
import me.mical.custompapi.CustomPapi;
import me.mical.custompapi.config.struct.Param;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.serverct.parrot.parrotx.api.ParrotXAPI;
import org.serverct.parrot.parrotx.config.PConfig;
import org.serverct.parrot.parrotx.data.PID;
import org.serverct.parrot.parrotx.data.PStruct;
import org.serverct.parrot.parrotx.data.autoload.annotations.PAutoload;
import org.serverct.parrot.parrotx.data.autoload.annotations.PAutoloadGroup;
import org.serverct.parrot.parrotx.utils.MapUtil;

import java.util.Map;

@Getter
@ToString
@PAutoload
@PAutoloadGroup
public class ParamManager extends PConfig {

    private Map<String, Param> params;

    @PAutoload("Params")
    private Map<String, ConfigurationSection> rawParams;

    public ParamManager() {
        super(ParrotXAPI.getPlugin(CustomPapi.class), "param", "变量配置文件");
    }

    @Override
    public void load() {
        if (config.getConfigurationSection("Params") == null) {
            config.createSection("Params");
        }

        super.load();

        this.params = MapUtil.transformValue(this.rawParams,
                section -> new Param(new PID(plugin, "param", section.getName()), section));
    }

    @Override
    public void save() {
        params.values().forEach(PStruct::save);
        super.save();
    }

    public void addParam(@NotNull final String name, final double defaultValue, final boolean enableRefresh, final int refreshTime) {
        if (config.getConfigurationSection("Params." + name) == null) {
            config.createSection("Params." + name);
        }
        final ConfigurationSection section = config.getConfigurationSection("Params." + name);

        params.put(name, new Param(
                new PID(plugin, "param", name),
                section,
                defaultValue,
                enableRefresh,
                refreshTime
        ));
        rawParams.put(name, section);
    }

    public static ParamManager getInstance() {
        return ParrotXAPI.getConfigManager(ParamManager.class);
    }
}
