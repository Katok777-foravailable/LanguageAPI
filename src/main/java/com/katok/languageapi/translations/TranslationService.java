package com.katok.languageapi.translations;

import com.katok.languageapi.server.ConfigManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TranslationService {
    private final Map<String, Map<String, YamlConfiguration>> languages = new HashMap<>();

    private String defaultLanguage = "us";

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public Map<String, Map<String, YamlConfiguration>> getLanguages() {
        return languages;
    }

    public Map<String, YamlConfiguration> getLanguage(Player player) {
        Map<String, YamlConfiguration> configurationMap = languages.get(player.locale().getLanguage());
        if (configurationMap == null) {
            configurationMap = Objects.requireNonNull(languages.get(getDefaultLanguage()));
        }

        return configurationMap;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public void load(ConfigManager configManager) {
        YamlConfiguration configuration = Objects.requireNonNull(configManager.getConfiguration("config.yml"));

        setDefaultLanguage(configuration.getString("default-languages"));

        for (Map.Entry<String, YamlConfiguration> configurationEntry : configManager.getConfigurations("languages")) {
            String[] args = configurationEntry.getKey().split("/");

            String language = args[1];
            Map<String, YamlConfiguration> languageMap = languages.computeIfAbsent(language, k -> new HashMap<>());

            StringBuilder fileNameBuilder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                fileNameBuilder.append(args[i]);
                if (i < args.length - 1) {
                    fileNameBuilder.append("/");
                }
            }

            languageMap.put(fileNameBuilder.toString(), configurationEntry.getValue());
        }
    }

    public void clear() {
        languages.clear();
    }
}
