package me.mical.custompapi.expansion;

import me.mical.custompapi.CustomPapi;
import me.mical.custompapi.config.ConfigManager;
import me.mical.custompapi.config.DataManager;
import org.serverct.parrot.parrotx.api.ParrotXAPI;
import org.serverct.parrot.parrotx.hooks.BaseExpansion;

import java.util.Arrays;

public class ParamExpansion extends BaseExpansion {
    public ParamExpansion() {
        super(
                ParrotXAPI.getPlugin(CustomPapi.class),
                ConfigManager.prefix,
                "Mical",
                ParrotXAPI.getPlugin(CustomPapi.class).getDescription().getVersion()
        );

        addParam(PlaceholderParam.builder()
                .name("param")
                .parse((offlinePlayer, strings) -> {
                    System.out.println(Arrays.toString(strings));
                    System.out.println(DataManager.getInstance().get(offlinePlayer.getUniqueId().toString()).getParams().containsKey(strings));
                    if (DataManager.getInstance().get(offlinePlayer.getUniqueId().toString()).getParams().containsKey(strings[0])) {
                        return String.valueOf(DataManager.getInstance().get(offlinePlayer.getUniqueId().toString()).getParams().get(strings[0]).getValue());
                    }
                    return "";
                })
                .build());
    }
}
