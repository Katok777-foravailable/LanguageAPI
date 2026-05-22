package com.katok.languageapi.server;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ConfigurationService {
    public static void init(ConfigManager configManager) throws IOException, InvalidConfigurationException {
        configManager.loadConfigFile("config.yml");

        YamlConfiguration configuration = Objects.requireNonNull(configManager.getConfiguration("config.yml"));

        if (configuration.getBoolean("generate-default-directories")) {
            configManager.loadConfigFile(String.format("languages%sru%sserver.yml", File.separator, File.separator));

            configManager.loadConfigFile(String.format("languages%sru%ssome.yml", File.separator, File.separator));
            configManager.loadConfigFile(String.format("languages%sru%smore-directories%sanother.yml", File.separator, File.separator, File.separator));

            configManager.loadConfigFile(String.format("languages%sus%sserver.yml", File.separator, File.separator));

            configManager.loadConfigFile(String.format("languages%sus%ssome.yml", File.separator, File.separator));
            configManager.loadConfigFile(String.format("languages%sus%smore-directories%sanother.yml", File.separator, File.separator, File.separator));
        }

        configManager.scanPluginFolder();
    }
}
