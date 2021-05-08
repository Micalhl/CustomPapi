package me.mical.custompapi.command;

import me.mical.custompapi.CustomPapi;
import me.mical.custompapi.command.impl.*;
import org.serverct.parrot.parrotx.api.ParrotXAPI;
import org.serverct.parrot.parrotx.command.subcommands.DebugCommand;
import org.serverct.parrot.parrotx.command.subcommands.HelpCommand;
import org.serverct.parrot.parrotx.command.subcommands.ReloadCommand;
import org.serverct.parrot.parrotx.command.subcommands.VersionCommand;
import org.serverct.parrot.parrotx.data.autoload.annotations.PAutoload;

@PAutoload
public class CommandHandler extends org.serverct.parrot.parrotx.command.CommandHandler {

    public CommandHandler() {
        super(ParrotXAPI.getPlugin(CustomPapi.class), "custompapi");
        register(new VersionCommand(plugin));
        register(new HelpCommand(plugin));
        register(new DebugCommand(plugin, ".debug"));
        register(new ReloadCommand(plugin, ".reload"));
        register(new AddCommand(plugin));
        register(new SetCommand(plugin));
        register(new TakeCommand(plugin));
        register(new CreateCommand(plugin));
        register(new APICommand(plugin));
    }
}
