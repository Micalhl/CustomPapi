package me.mical.custompapi.data.struct;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.configuration.ConfigurationSection;
import org.serverct.parrot.parrotx.data.PID;
import org.serverct.parrot.parrotx.data.PStruct;
import org.serverct.parrot.parrotx.data.autoload.annotations.PAutoload;
import org.serverct.parrot.parrotx.data.autoload.annotations.PAutoloadGroup;

@Getter
@Setter
@ToString
@PAutoloadGroup
public class Data extends PStruct {

    @PAutoload("Timestamp")
    private long timestamp;
    @PAutoload("Value")
    private double value;

    public Data(PID id, ConfigurationSection section, long timestamp, double value) {
        super(id, section, "玩家刷新变量数据");
        this.timestamp = timestamp;
        this.value = value;
    }

    public Data(PID id, ConfigurationSection section) {
        super(id, section, "玩家刷新变量数据");
        load();
    }

    public void addValue(double add) {
        this.value = value + add;
    }

    public void takeValue(double take) {
        this.value = value - take;
    }
}
