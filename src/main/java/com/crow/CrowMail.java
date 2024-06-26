package com.crow;
import com.crow.config.MainConfig;
import com.crow.config.MessageManager;
import com.crow.config.OutgoingManager;
import com.crow.crow.Crow;
import com.crow.events.CommandTabComplete;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.crow.config.ConfigLoader;
import com.crow.events.CommandE;
import com.crow.events.PlayerE;

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
        ConfigLoader.getOutgoingConfig().saveDefaultConfig();
        ConfigLoader.getMessageConfig().saveDefaultConfig();
        ConfigLoader.getMainConfig().saveDefaultConfig();

        //Loads the config.yml and messages.yml files' content
        MainConfig.loadConfigs();
        MessageManager.reloadMessages();
        OutgoingManager.sendSavedLetters();


        getLogger().info("CrowMail Enabled");

        // Plugin Manager
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.addPermission(new Permission("crowmail.carta"));
        pluginManager.addPermission(new Permission("crowmail.cartaanonima"));
        pluginManager.addPermission(new Permission("crowmail.enviar"));
        pluginManager.addPermission(new Permission("crowmail.infocarta"));
        pluginManager.addPermission(new Permission("crowmail.bloquear"));
        pluginManager.addPermission(new Permission("crowmail.rasgar"));
        pluginManager.addPermission(new Permission("crowmail.reload"));


        // Commands Atribution
        CommandE commandE = new CommandE();

        getCommand("carta").setExecutor(commandE);
        getCommand("cartaanonima").setExecutor(commandE);
        getCommand("enviar").setExecutor(commandE);
        getCommand("infocarta").setExecutor(commandE);
        getCommand("rasgar").setExecutor(commandE);
        getCommand("bloquearcartas").setExecutor(commandE);
        getCommand("crowmail").setExecutor(commandE);

        getCommand("crowmail").setTabCompleter(new CommandTabComplete());
        getCommand("rasgar").setTabCompleter(new CommandTabComplete());
        getCommand("enviar").setTabCompleter(new CommandTabComplete());

        // EventHandler Atribution
        pluginManager.registerEvents(new PlayerE(), plugin);

    }

    @Override
    public void onDisable() {
        getLogger().info("CrowMail Disabled");
        OutgoingManager.saveLetters();
        Crow.removeAllCrows();

        plugin = null;
    }
}