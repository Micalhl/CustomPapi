package me.mical.custompapi.command.impl;

import me.mical.custompapi.config.ParamManager;
import me.mical.custompapi.utils.ParamUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.command.BaseCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TakeCommand extends BaseCommand {

    public TakeCommand(@NotNull PPlugin plugin) {
        super(plugin, "take", 3);

        perm(".take");
        describe("为玩家的某变量减少数值.");

        addParam(CommandParam.builder()
                .name("玩家 ID")
                .description("玩家名")
                .position(0)
                .suggest(() -> Arrays.stream(Bukkit.getOfflinePlayers())
                        .map(OfflinePlayer::getName)
                        .collect(Collectors.toList())
                        .toArray(new String[0]))
                .validate(s -> Bukkit.getOfflinePlayer(s) != null)
                .advancedValidateMessage(args -> warn("玩家 &c{0} &f不存在.", args[0]))
                .converter(strings -> Bukkit.getOfflinePlayer(strings[0]))
                .build());
        addParam(CommandParam.builder()
                .name("变量名")
                .description("变量名")
                .position(1)
                .suggest(() -> new ArrayList<>(ParamManager.getInstance().getParams()
                        .keySet())
                        .toArray(new String[0])
                )
                .validate(s -> ParamManager.getInstance().getParams().containsKey(s))
                .validateMessage(warn("您输入的变量不存在."))
                .converter(strings -> strings[1])
                .build());
        addParam(CommandParam.aDouble(
                2,
                "添加值",
                "要给变量减少的值(可为小数)",
                args -> warn("您输入的选项(变量减少值)为 &c{0}&f, 该值只能为数字.", args[1]),
                null
        ));
    }

    @Override
    protected void call(String[] strings) {
        ParamUtil.take(convert(1, strings, String.class), convert(0, strings, OfflinePlayer.class).getUniqueId(), convert(2, strings, Double.class));
        sender.sendMessage(info("已成功为玩家 &c{0} &f的 &c{1} &f变量减少了数值 &c{2}&f.", convert(0, strings, OfflinePlayer.class).getName(), convert(1, strings, String.class), convert(2, strings, Double.class)));
    }
}
