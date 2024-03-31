package com.crow.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.crow.crow.Crow;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Parrot;

import com.crow.CrowMail;

public class ConfigLoader {

//Reestruturei as configurações para o MainConfig e MessageManager, deixando essa classe somente como um Loader - Super

    private static CrowMail plugin = CrowMail.getInstance();


    private static ConfigLoader main = new ConfigLoader("config.yml");

    private static ConfigLoader message = new ConfigLoader("messages.yml");


    public static ConfigLoader getMainConfig() { return main; }

    public static ConfigLoader getMessageConfig() { return message; }


    private File configFile;

    private YamlConfiguration YMLConfig;

    private final String filename;


    public ConfigLoader(String filename) {
        this.filename = filename;
        configFile = new File(plugin.getDataFolder(), filename);
    }

    /**
     * Reloads all configuration files.
     * @author Super
     */

    //TODO: adicionar comando de /reload
    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), filename);
        }

        YMLConfig = YamlConfiguration.loadConfiguration(configFile);
        Reader reader = new InputStreamReader(
                plugin.getResource(filename), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(reader);
        YMLConfig.setDefaults(defConfig);
        YMLConfig.options().copyDefaults(true);
        saveConfigs();
        MessageManager.reloadMessages();
    }

    /**
     * Deve ser usado depois do getMainConfig, ou getMessageConfig
     *
     * @return {@code YamlConfiguration}
     * @author Super
     */
    public YamlConfiguration getConfig() {
        if (YMLConfig == null) {
            reloadConfig();
        }
        return YMLConfig;
    }

    public void saveConfigs() {
        if (YMLConfig == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Houve um erro ao salvar uma configuração", e);
        }
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), filename);
        }
        if (!configFile.exists()) {
            plugin.saveResource(filename, false);
            YMLConfig = YamlConfiguration.loadConfiguration(configFile);
        }
    }


}
