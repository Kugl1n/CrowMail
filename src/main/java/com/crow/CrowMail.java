package com.crow;
import com.crow.config.MainConfig;
import com.crow.config.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.crow.config.ConfigLoader;
import com.crow.events.CommandE;
import com.crow.events.PlayerE;
import sun.security.krb5.Config;

public class CrowMail extends JavaPlugin {

    // Plugin Instance
    public static CrowMail plugin;
    
    // Returns Plugin Instance
    public static CrowMail getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {

        plugin = this;

        //Saves and creates the config.yml and messages.yml, if they don't exist
        ConfigLoader.getMainConfig().saveDefaultConfig();
        ConfigLoader.getMessageConfig().saveDefaultConfig();


        //Loads the config.yml and messages.yml files' content
        MainConfig.loadConfigs();
        MessageManager.reloadMessages();

        getLogger().info("CrowMail Enabled");
        
        // Plugin Manager
        PluginManager pluginManager = Bukkit.getPluginManager();

        // Commands Atribution
        CommandE commandE = new CommandE();

        getCommand("carta").setExecutor(commandE);
        getCommand("cartaanonima").setExecutor(commandE);
        getCommand("enviar").setExecutor(commandE);
        getCommand("infocarta").setExecutor(commandE);
        getCommand("bloquearcartas").setExecutor(commandE);

        // EventHandler Atribution
        pluginManager.registerEvents(new PlayerE(), plugin);

    }

    @Override
    public void onDisable() {
        getLogger().info("CrowMail Disabled");

        plugin = null;
    }
}