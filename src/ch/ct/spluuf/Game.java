package ch.ct.spluuf;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Game implements Listener {
    private static final Material TOOL = Material.STICK;
    private static final Material DESTROYABLE_BLOCK = Material.CLAY;

    private static final World WORLD = Bukkit.getWorlds().get(0);

    private static final Location CORNER_1 = new Location(WORLD, -605, 4, -698);
    private static final Location CORNER_2 = new Location(WORLD, -584, 4, -678);

    private final List<Player> players;

    public Game(List<Player> players) {

        this.players = players;
        initGamefiled();
        teleportPlayers();
    }

    private void teleportPlayers() {
        for (Player p : players) {
            p.teleport(CORNER_1);
            p.setGameMode(GameMode.SURVIVAL);
            p.getInventory().setItem(0, new ItemStack(TOOL));
        }
    }

    private void initGamefiled() {
        for (int x = CORNER_1.getBlockX(); x < CORNER_2.getBlockX(); x++) {
            for (int y = CORNER_1.getBlockZ(); y < CORNER_2.getBlockZ(); y++) {
                Block block = WORLD.getBlockAt(x, CORNER_1.getBlockY(), y);
                block.setType(DESTROYABLE_BLOCK);
            }
        }
    }

    @EventHandler
    public void onPlayerIntreactEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!players.contains(p)) {
            return;
        }

        if (TOOL.equals(p.getInventory().getItemInMainHand().getType())) {
            Block clickedBlock = e.getClickedBlock();
            if (clickedBlock != null && DESTROYABLE_BLOCK.equals(clickedBlock.getState().getType())) {
                clickedBlock.setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (players.contains(p)) {
            players.remove(p);
            for (Player pl : players) {
                pl.sendMessage(p.getName() + " died! " + players.size() + " left.");
            }
        }
    }


}
