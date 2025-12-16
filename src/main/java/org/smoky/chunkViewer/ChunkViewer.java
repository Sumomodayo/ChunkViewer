package org.smoky.chunkViewer;

import org.bukkit.plugin.java.JavaPlugin;
import org.smoky.chunkViewer.command.ChunkViewCommand;

public final class ChunkViewer extends JavaPlugin {

    @Override
    public void onEnable() {
        // コマンド登録
        getCommand("chunkview").setExecutor(new ChunkViewCommand(this));
        getLogger().info("ChunkViewer Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("ChunkViewer Disabled");
    }
}
