package me.mical.custompapi.command.impl;

import com.google.common.base.Joiner;
import me.mical.custompapi.CustomPapi;
import org.jetbrains.annotations.NotNull;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.command.BaseCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class APICommand extends BaseCommand {

    public APICommand(@NotNull PPlugin plugin) {
        super(plugin, "api", 0);

        describe("查询使用 CustomPapi 的附属插件列表");
        perm(".api");
    }

    @Override
    protected void call(String[] args) {
        if (CustomPapi.getAddons().isEmpty()) {
            sender.sendMessage(info("目前没有使用 CustomPapi 的附属插件."));
        } else {
            final List<String> plugins = new ArrayList<>();
            CustomPapi.getAddons().forEach(p -> plugins.add("&c" + p));
            sender.sendMessage(info("当前运行 &c{0} &f个附属: &f" + Joiner.on("&f, ").join(
                    plugins.stream()
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .collect(Collectors.toList())
            ), plugins.size()));
        }
    }
}
