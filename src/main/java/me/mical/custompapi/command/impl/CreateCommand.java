package me.mical.custompapi.command.impl;

import me.mical.custompapi.config.ParamManager;
import me.mical.custompapi.sql.DaoManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.command.BaseCommand;
import org.serverct.parrot.parrotx.utils.TimeUtil;
import org.serverct.parrot.parrotx.utils.i18n.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class CreateCommand extends BaseCommand {

    public CreateCommand(@NotNull PPlugin plugin) {
        super(plugin, "create", 4);

        describe("创建一个变量");
        perm(".create");

        addParam(CommandParam.builder()
                .name("变量 ID")
                .description("变量名, 用于配置文件中标记.")
                .position(0)
                .validate(id -> !ParamManager.getInstance().getParams().containsKey(id))
                .advancedValidateMessage(args -> warn("您要创建的变量 &c{0} &f已存在.", args[0]))
                .converter(strings -> strings[0])
                .build());

        addParam(CommandParam.aDouble(
                1,
                "默认值",
                "变量的默认值(可为小数)",
                args -> warn("您输入的选项(变量默认值)为 &c{0}&f, 该值只能为数字.", args[1]),
                null
        ));

        addParam(CommandParam.builder()
                .name("是否自动刷新")
                .description("是否开启自动刷新")
                .position(2)
                .suggest(() -> new String[]{"true", "false"})
                .validate((input) -> (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")))
                .advancedValidateMessage(args -> warn("您输入的选项(是否自动刷新)为 &c{0}&f, 该值只能为 &ctrue &f或 &cfalse&f."))
                .converter((args) -> Boolean.parseBoolean(args[2]))
                .build());

        addParam(aInt(
                3,
                "刷新间隔",
                "刷新间隔(秒)(整数)",
                args -> warn("您输入的选项(刷新间隔)为 &c{0}&f, 该值只能为大于0的数字.", args[3]),
                (aInt) -> aInt > 0
        ));
    }

    private static CommandParam aInt(int position, @NotNull String name, @Nullable String description, @Nullable Function<String[], String> validateMessage, @Nullable Predicate<Integer> check) {
        return CommandParam.builder().name(name).description(description).position(position).validate((input) -> {
            try {
                int value = Integer.parseInt(input);
                return !Objects.nonNull(check) || check.test(value);
            } catch (NumberFormatException var3) {
                return false;
            }
        }).advancedValidateMessage(validateMessage).converter((args) -> Integer.parseInt(args[position])).build();
    }

    @Override
    protected void call(String[] strings) {
        ParamManager.getInstance().addParam(convert(0, strings, String.class), convert(1, strings, Double.class), convert(2, strings, Boolean.class), convert(3, strings, Integer.class));
        final List<String> message = new ArrayList<>();
        message.add(I18n.color("您已成功创建变量 &c{0}&f, 详情信息如下:", convert(0, strings, String.class)));
        message.add(I18n.color("默认值: &a{0}&f.", convert(1, strings, Double.class)));
        message.add(I18n.color("开启自动刷新: {0}&f.", convert(2, strings, Boolean.class) ? "&a&l是" : "&c&l否"));
        //DataManager.getInstance().getAll().forEach(playerData -> playerData.addParam(ParamManager.getInstance().getParams().get(convert(0, strings, String.class))));
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            DaoManager.getDao().create(offlinePlayer.getUniqueId().toString(), convert(0, strings, String.class));
        }
        if (convert(2, strings, Boolean.class)) {
            message.add(I18n.color("自动刷新间隔: &a{0}&f.", TimeUtil.getTimeLong(convert(3, strings, Integer.class), "{0}{1}")));
        }
        message.forEach(msg -> sender.sendMessage(info(msg)));
    }
}
