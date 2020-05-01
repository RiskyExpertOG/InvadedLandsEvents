package me.nicbo.InvadedLandsEvents;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.nicbo.InvadedLandsEvents.commands.EventCommand;
import me.nicbo.InvadedLandsEvents.commands.EventConfigCommand;
import me.nicbo.InvadedLandsEvents.messages.EventMessage;
import me.nicbo.InvadedLandsEvents.utils.ConfigFile;
import me.nicbo.InvadedLandsEvents.listeners.GeneralEventListener;
import me.nicbo.InvadedLandsEvents.handlers.ManagerHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Main class
 *
 * @author Nicbo
 * @author StarZorroww
 * @since 2020-03-12
 */

public class EventsMain extends JavaPlugin {
    private Logger log;
    private ManagerHandler managerHandler;
    private WorldGuardPlugin worldGuardPlugin;

    private static ConfigFile messages;

    @Override
    public void onEnable() {
        log = getLogger();
        worldGuardPlugin = getWorldGuard();
        saveDefaultConfig();
        messages = new ConfigFile("messages.yml", this);
        EventMessage.reload();

        this.managerHandler = new ManagerHandler(this);
        registerCommands();
        getServer().getPluginManager().registerEvents(new GeneralEventListener(this), this);
        log.info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
        messages.save();

        if (managerHandler.getEventManager().isEventRunning())
            managerHandler.getEventManager().getCurrentEvent().forceEndEvent();

        log.info("Plugin disabled!");
    }

    private void registerCommands() {
        EventConfigCommand eventConfigCommand = new EventConfigCommand(this);
        EventCommand eventCommand = new EventCommand(this);
        getCommand("event").setExecutor(eventCommand);
        getCommand("eventconfig").setExecutor(eventConfigCommand);
        getCommand("eventconfig").setTabCompleter(eventConfigCommand);
        getCommand("event").setTabCompleter(eventCommand);
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (!(plugin instanceof WorldGuardPlugin)) {
            log.severe("WorldGuard not found!");
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    public WorldGuardPlugin getWorldGuardPlugin() {
        return worldGuardPlugin;
    }

    public ManagerHandler getManagerHandler() {
        return this.managerHandler;
    }

    public static ConfigFile getMessages() {
        return messages;
    }
}
