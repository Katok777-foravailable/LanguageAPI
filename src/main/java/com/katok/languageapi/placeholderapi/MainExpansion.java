package com.katok.languageapi.placeholderapi;

import com.katok.languageapi.translations.TranslationService;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class MainExpansion extends PlaceholderExpansion {
    private final JavaPlugin plugin;
    private final TranslationService translationService;

    public MainExpansion(JavaPlugin plugin,
                         TranslationService translationService) {
        this.plugin = plugin;
        this.translationService = translationService;
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getName();
    }

    @Override
    public @NotNull String getAuthor() {
        return "Katok777";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        String[] args = params.split("_");

        Map<String, YamlConfiguration> configurationMap = translationService.getLanguage(player);
        if (configurationMap == null) {
            configurationMap = Objects.requireNonNull(translationService.getLanguages().get(translationService.getDefaultLanguage()));
        }

        YamlConfiguration configuration = configurationMap.get(args[0]);
        return configuration.getString(args[1]);
    }
}
