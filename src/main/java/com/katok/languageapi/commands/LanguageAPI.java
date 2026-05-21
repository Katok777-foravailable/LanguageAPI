package com.katok.languageapi.commands;

import com.katok.languageapi.server.ConfigManager;
import com.katok.languageapi.server.ConfigurationService;
import com.katok.languageapi.translations.TranslationService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LanguageAPI implements TabExecutor {
    private final ConfigManager configManager;
    private final TranslationService translationService;

    public LanguageAPI(ConfigManager configManager,
                       TranslationService translationService) {
        this.configManager = configManager;
        this.translationService = translationService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Map<String, YamlConfiguration> configurationMap;

        if (sender instanceof Player) {
            configurationMap = translationService.getLanguage((Player) sender);
        } else {
            configurationMap = translationService.getLanguages().get(translationService.getDefaultLanguage());
        }

        YamlConfiguration serverCFG = configurationMap.get("server.yml");

        if (args.length < 1) {
            sender.sendMessage(ConfigManager.colorComponent(help(serverCFG)));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("languageapi.reload")) {
                sender.sendMessage(Bukkit.getPermissionMessage());
                return true;
            }

            configManager.clear();
            try {
                ConfigurationService.init(configManager);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }

            translationService.clear();
            translationService.load(configManager);

            sender.sendMessage(ConfigManager.colorComponent(reloadSuccessful(serverCFG)));
            return true;
        }

        sender.sendMessage(ConfigManager.colorComponent(help(serverCFG)));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> result = new ArrayList<>();
        result.add("reload");

        return result;
    }

    public String help(YamlConfiguration languageCFG) {
        return Objects.requireNonNull(languageCFG.getString("commands.languageapi.help"));
    }

    public String reloadSuccessful(YamlConfiguration languageCFG) {
        return Objects.requireNonNull(languageCFG.getString("commands.languageapi.reload-successful"));
    }
}
