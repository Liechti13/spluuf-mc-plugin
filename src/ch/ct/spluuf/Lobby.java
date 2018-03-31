package ch.ct.spluuf;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class Lobby implements Listener {

    public static final int MAX_PLAYERS = 2;

    public final Location lobby = new Location(Bukkit.getWorlds().get(0), -600.5, 4, -640.5);
    public List<Player> playersInLobby = new ArrayList<>();
    private final Main main;

    public Lobby(Main main) {

        this.main = main;
    }

    @EventHandler
    public void onPlayerIntreactEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        Block block = e.getClickedBlock();

        if (block == null) {
            return;
        }

        if (block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN)) {
            Sign sign = (Sign) block.getState();
            String text = sign.getLine(1);

            if (text.equalsIgnoreCase("[Lobby]")) {
                if (playersInLobby.size() >= MAX_PLAYERS) {
                    p.sendMessage("Lobby is already full :(");
                } else if (playersInLobby.contains(p)) {
                    p.sendMessage("You are already in this lobby.");
                } else {
                    playersInLobby.add(p);
                    sendPlayerToLobby(p);
                }

                if (playersInLobby.size() == MAX_PLAYERS) {
                    for (Player pl : playersInLobby) {
                        pl.sendMessage("Lobby is complete, game starts now...");
                    }
                    Game g = new Game(playersInLobby);
                    main.getServer().getPluginManager().registerEvents(g, main);
                }
            }
        }
    }

    private void sendPlayerToLobby(Player p) {
        p.sendMessage("Teleported to lobby!");
        p.sendMessage("Other players in the lobby are:");
        for (Player op : playersInLobby) {
            p.sendMessage("  * " + op.getName());
        }
        p.teleport(lobby);
    }
}
