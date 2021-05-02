package me.mical.custompapi.command.impl;

import me.mical.custompapi.config.DataManager;
import me.mical.custompapi.config.ParamManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.command.BaseCommand;

import java.util.ArrayList;

public class SetCommand extends BaseCommand {

    public SetCommand(@NotNull PPlugin plugin) {
        super(plugin, "set", 3);

        perm(".set");
        describe("为玩家的某变量设定数值.");

        addParam(CommandParam.player(
                0,
                "玩家名",
                args -> warn("玩家 &c{0} &f不在线或不存在.", args[0])
        ));
        addParam(CommandParam.builder()
                .name("变量名")
                .description("变量名")
                .position(1)
                .suggest(() -> new ArrayList<>(ParamManager.getInstance().getParams()
                        .keySet())
                        .toArray(new String[0])
                )
                .converter(strings -> strings[1])
                .build());
        addParam(CommandParam.aDouble(
                2,
                "添加值",
                "要给变量设定的值(可为小数)",
                args -> warn("您输入的选项(变量设定值)为 &c{0}&f, 该值只能为数字.", args[1]),
                null
        ));
    }

    @Override
    protected void call(String[] strings) {
        DataManager.getInstance().get(convert(0, strings, Player.class).getUniqueId().toString()).getParams().get(convert(1, strings, String.class)).setValue(convert(2, strings, Double.class));
        sender.sendMessage(info("已成功为玩家 &c{0} &f的 &c{1} &f变量设定了数值 &c{2}&f.", convert(0, strings, Player.class).getName(), convert(1, strings, String.class), convert(2, strings, Double.class)));
    }
}