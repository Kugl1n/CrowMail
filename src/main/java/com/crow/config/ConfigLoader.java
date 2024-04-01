package com.crow.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

import com.crow.CrowMail;

/**
 * Manager for all configuration files
 *
 * @author Super
 */

public class ConfigLoader {

    private static CrowMail plugin = CrowMail.getInstance();


    private static ConfigLoader main = new ConfigLoader("config.yml");

    private static ConfigLoader message = new ConfigLoader("messages.yml");

    private static ConfigLoader outgoing = new ConfigLoader("outgoing.yml");


    public static ConfigLoader getMainConfig() { return main; }

    public static ConfigLoader getMessageConfig() { return message; }

    public static ConfigLoader getOutgoingConfig() { return outgoing; }


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

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), filename);
        }
        System.out.println("configFile: " + configFile);
        YMLConfig = YamlConfiguration.loadConfiguration(configFile);
        Reader reader = new InputStreamReader(
                plugin.getResource(filename), StandardCharsets.UTF_8);
        System.out.println("reader: ");
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(reader);
        YMLConfig.setDefaults(defConfig);
        YMLConfig.options().copyDefaults(true);
        saveConfigs();
        System.out.println("YMLConfig: " + YMLConfig);

        if (filename.equals("messages.yml")) MessageManager.reloadMessages();
    }

    /**
     * Should be used after the getMainConfig, or getMessageConfig methods
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
