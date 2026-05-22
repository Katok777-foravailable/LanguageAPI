package com.katok.languageapi;

import com.katok.languageapi.placeholderapi.MainExpansion;
import com.katok.languageapi.server.ConfigManager;
import com.katok.languageapi.server.ConfigurationService;
import com.katok.languageapi.translations.TranslationService;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;

public class LanguageAPI extends JavaPlugin {
    private final ConfigManager configManager = new ConfigManager(this);
    private final TranslationService translationService = new TranslationService();

    @Override
    public void onEnable() {
        try {
            ConfigurationService.init(configManager);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

        translationService.load(configManager);

        Objects.requireNonNull(getCommand("languageapi")).setExecutor(new com.katok.languageapi.commands.LanguageAPI(configManager, translationService));

        new MainExpansion(this, translationService).register();
    }

    public TranslationService getTranslationService() {
        return translationService;
    }
}