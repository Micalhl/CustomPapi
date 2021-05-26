package me.mical.custompapi.expansion;

import com.google.common.base.Joiner;
import me.mical.custompapi.CustomPapi;
import me.mical.custompapi.config.ConfigManager;
import me.mical.custompapi.utils.ParamUtil;
import org.serverct.parrot.parrotx.api.ParrotXAPI;
import org.serverct.parrot.parrotx.hooks.BaseExpansion;

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
                .parse((offlinePlayer, strings) -> String.valueOf(ParamUtil.get(Joiner.on('_').join(strings), offlinePlayer.getUniqueId())))
                .build());
    }
}
