package ch.ct.spluuf;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onDisable() {
        System.out.println("The Plugin is disabled.");
    }

    @Override
    public void onEnable() {
        System.out.println("The Plugin is enabled on Version " + getServer().getVersion() + ".");
        getServer().getPluginManager().registerEvents(new Lobby(this), this);
    }
}
