package com.katok.languageapi.server;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigManager {
    public static final String MESSAGE_ERROR = "&cERROR";

    private final JavaPlugin instance;

    private final HashMap<String, YamlConfiguration> configs = new HashMap<>();

    public ConfigManager(JavaPlugin instance) {
        this.instance = instance;
    }

    public static String color(String message) {
        if (message == null) {
            return "";
        }

        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String color = matcher.group(1);
            String replacement = ChatColor.of("#" + color).toString();
            message = message.replace("&#" + color, replacement);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static Component colorComponent(String message) {
        Component result = Component.empty();

        if (message == null) {
            return result;
        }

        {
            Pattern pattern = Pattern.compile("&([A-Za-z0-9])");
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String color = matcher.group(1);
                message = message.replaceAll("&" + color, "§" + color);
            }
        }

        List<String> colors_text = new ArrayList<>(Arrays.asList(message.split("&#")));

        for(int i = 1; i < colors_text.size(); i++) {
            colors_text.set(i, "#" + colors_text.get(i));
        }

        for(String text: colors_text) {
            Pattern pattern = Pattern.compile("#([A-Fa-f0-9]{6})");
            Matcher matcher = pattern.matcher(text);
            if(matcher.find()) {
                String color = matcher.group(1);
                result = result.append(Component.text(text.replaceAll("#" + color, "")).color(TextColor.fromHexString("#" + color)));
            } else {
                result = result.append(Component.text(text));
            }
        }
        return result;
    }

    public void clear() {
        configs.clear();
    }

    /**
     * Выдает конфигурационный файл, по пути, от папки самого плагина, например - getConfiguration("config.yml")
     * @return конфигурацию
     */
    @Nullable
    public YamlConfiguration getConfiguration(String path) {
        if (!configs.containsKey(path)) {
            return null;
        }

        return configs.get(path);
    }

    /**
     * Выдает конфигурационный файл локализации
     * @param lang - язык локализации
     * @return конфигурацию
     */
    @Nullable
    public YamlConfiguration getLanguageConfiguration(String lang) {
        return getConfiguration(String.format("languages%s%s.yml", File.separator, lang.toUpperCase()));
    }

    /**
     * Сканирует конфигурации, и выдает список конфигураций путь которых начинаеться с startWithPath
     * @param startWithPath - путь
     * @return список конфигураций
     */
    public List<Map.Entry<String, YamlConfiguration>> getConfigurations(String startWithPath) {
        List<Map.Entry<String, YamlConfiguration>> result = new ArrayList<>();
        for(Map.Entry<String, YamlConfiguration> configurationEntry: configs.entrySet()) {
            if(!configurationEntry.getKey().startsWith(startWithPath)) continue;

            result.add(configurationEntry);
        }

        return result;
    }

    /**
     * Загружает определенный файл из resources в папку плагина
     * @param path путь к файлу
     */
    public void loadConfigFile(String path) throws IOException, InvalidConfigurationException {
        File configFile = new File(instance.getDataFolder().getAbsolutePath() + File.separator + path);

        if(!configFile.exists()) instance.saveResource(path, false);

        YamlConfiguration configCfg = new YamlConfiguration();
        configCfg.load(configFile);

        path = path.replace(File.separator, "/");

        if(configs.containsKey(path)) {
            configs.replace(path, configCfg);
        } else {
            configs.put(path, configCfg);
        }
    }

    /**
     * Сохраняет конфигурационный файл по определенному пути
     * @param path - путь где будет сохранена конфигурация
     * @param configuration - сам конфигурационный файл
     */
    public void saveConfiguration(String path, FileConfiguration configuration) throws IOException {
        File configFile = new File(instance.getDataFolder().toPath().toAbsolutePath() + File.separator + path);

        if(!configFile.exists()) {
            configFile.createNewFile();
        }
        if(configs.containsKey(path)) configs.remove(path);

        configuration.save(configFile);

        path = path.replace(File.separator, "/");

        configs.put(path, YamlConfiguration.loadConfiguration(configFile));
    }

    /**
     * Сохраняет определенную конфигурацию с оперативной памяти в папку плагина
     * @param path - путь конфигурации
     */
    public void saveConfiguration(String path) throws IOException {
        File configFile = new File(instance.getDataFolder().toPath().toAbsolutePath() + File.separator + path);
        if(!configFile.exists()) {
            configFile.createNewFile();
        }
        YamlConfiguration configuration = new YamlConfiguration();
        if(configs.containsKey(path.replace(File.separator, "/"))) configuration = configs.get(path);

        configuration.save(configFile);
    }

    /**
     * Сохраняет все конфигурационные файлы в папку плагина
     */
    public void saveAll() {
        for(String path: configs.keySet()) {
            File configFile = new File(instance.getDataFolder().toPath().toAbsolutePath() + File.separator + path.replace('/', File.separatorChar));
            try {
                configs.get(path).save(configFile);
            } catch (IOException e) {
                throw new RuntimeException("Не удалось сохранить конфиг " + path, e);
            }
        }
    }

    /**
     * Сканирует папку плагина, и сохраняет все файлы с расширением .yml
     */
    public void scanPluginFolder() throws IOException, InvalidConfigurationException {
        readFiles(new File(instance.getDataFolder().toPath().toAbsolutePath().toString()));
    }

    private void readFiles(File baseDirectory) throws IOException, InvalidConfigurationException {
        if (baseDirectory.isDirectory()){
            for (File file : baseDirectory.listFiles()) {
                if(file.isFile() && file.getAbsolutePath().endsWith(".yml")) {
                    String path = file.getAbsolutePath().substring((instance.getDataFolder().toPath().toAbsolutePath() + File.separator).length());
                    loadConfigFile(path);
                } else {
                    readFiles(file);
                }
            }
        }
    }
}