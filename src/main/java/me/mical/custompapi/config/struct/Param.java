package me.mical.custompapi.config.struct;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.configuration.ConfigurationSection;
import org.serverct.parrot.parrotx.data.PID;
import org.serverct.parrot.parrotx.data.PStruct;
import org.serverct.parrot.parrotx.data.autoload.annotations.PAutoload;
import org.serverct.parrot.parrotx.data.autoload.annotations.PAutoloadGroup;

@Getter
@ToString
@PAutoloadGroup
public class Param extends PStruct {

    @PAutoload("DefaultValue")
    private double defaultValue;
    @PAutoload("EnableRefresh")
    private boolean enableRefresh;
    @PAutoload("RefreshTime")
    private int refreshTime;

    private String placeholderParam;

    public Param(PID id, ConfigurationSection section, double defaultValue, boolean enableRefresh, int refreshTime) {
        super(id, section, "变量");

        this.defaultValue = defaultValue;
        this.enableRefresh = enableRefresh;
        this.refreshTime = refreshTime;
        this.placeholderParam = section.getName();

        save();
    }

    public Param(PID id, ConfigurationSection section) {
        super(id, section, "变量");
        load();
    }
}
