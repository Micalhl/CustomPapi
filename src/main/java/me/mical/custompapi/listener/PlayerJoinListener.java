package me.mical.custompapi.listener;

import me.mical.custompapi.utils.StorageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.serverct.parrot.parrotx.data.autoload.annotations.PAutoload;

@PAutoload
public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        StorageUtil.initPlayerData(event.getPlayer().getUniqueId());
    }
}
