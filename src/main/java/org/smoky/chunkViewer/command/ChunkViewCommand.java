package org.smoky.chunkViewer.command;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class ChunkViewCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final HashMap<UUID, BukkitTask> tasks = new HashMap<>();

    public ChunkViewCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cプレイヤー専用コマンドです");
            return true;
        }

        if (!player.hasPermission("chunkviewer.use")) {
            player.sendMessage("§cあなたはこのコマンドを使う権限がありません");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§e使い方: /chunkview on | off");
            return true;
        }

        UUID uuid = player.getUniqueId();

        switch (args[0].toLowerCase()) {
            case "on" -> {
                if (tasks.containsKey(uuid)) {
                    player.sendMessage("§eすでにONです");
                    return true;
                }

                BukkitTask task = plugin.getServer().getScheduler().runTaskTimer(
                        plugin,
                        () -> showChunkBorder(player),
                        0L,
                        40L
                );

                tasks.put(uuid, task);
                player.sendMessage("§aチャンク表示をONにしました");
            }

            case "off" -> {
                if (!tasks.containsKey(uuid)) {
                    player.sendMessage("§eすでにOFFです");
                    return true;
                }

                tasks.get(uuid).cancel();
                tasks.remove(uuid);
                player.sendMessage("§cチャンク表示をOFFにしました");
            }

            default -> player.sendMessage("§e使い方: /chunkview on | off");
        }

        return true;
    }

    private void showChunkBorder(Player player) {
        Location loc = player.getLocation();

        int chunkX = loc.getChunk().getX() << 4;
        int chunkZ = loc.getChunk().getZ() << 4;

        double y = loc.getBlockY() + 0.1;

        for (int i = 0; i <= 16; i += 2) {
            spawn(player, chunkX + i, y, chunkZ);
            spawn(player, chunkX + i, y, chunkZ + 16);

            spawn(player, chunkX, y, chunkZ + i);
            spawn(player, chunkX + 16, y, chunkZ + i);
        }
    }

    private void spawn(Player player, double x, double y, double z) {
        player.spawnParticle(
                Particle.END_ROD,
                x, y, z,
                1,
                0, 0, 0,
                0
        );
    }
}
