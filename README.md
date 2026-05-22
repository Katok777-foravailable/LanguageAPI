# 🌍 LanguageAPI

Stop forcing your players to use a language they don't understand! **LanguageAPI** is the ultimate, lightweight, and efficient solution for localizing your Minecraft server. Whether you run a global network or just want to offer a premium experience, LanguageAPI bridges the language gap effortlessly.

## 🚀 Why choose LanguageAPI?

* **Universal Language Support:** Not just English and Russian! The plugin supports **all languages** available in Minecraft. If a language exists in the game client, LanguageAPI can translate your content into it.
* **Automatic Detection:** No need for complex menus. The plugin automatically detects the player's client language and serves the right content.
* **PlaceholderAPI Integration:** Powered by PAPI, it works with *any* plugin that supports placeholders.
* **Easy Management:** Clean, folder-based structure. Just drop in a `yml` file for any language, and you're ready to go.
* **Smart Fallback:** Missing a translation for a specific language? LanguageAPI automatically defaults to your server’s primary language to ensure no text ever breaks.

## ⚙️ How it works

Your translations are kept organized in the `LanguageAPI/languages/` folder.

**Example File (`RU/items.yml`):**
```YAML
weapons:
  magic-sword: "&aМагический меч"
```

**Example File (`US/items.yml`):**

```YAML
weapons:
  magic-sword: "&aMagic sword"
```

Simply replace your hardcoded messages with our smart placeholders:
%LanguageAPI_items.yml_weapons.magic-sword%

That’s it! If a Russian player joins, they see "Магический меч". If an American player joins, they see "Magic sword".

📌 Placeholders Syntax
Structure: %LanguageAPI_[path_to_file]_[key_path]%
Example: %LanguageAPI_items.yml_weapons.magic-sword%

🛠 Commands & Permissions
/languageapi reload — Reloads all language configurations instantly.

Permission: languageapi.reload

💡 Pro Tip for Server Owners
Using LanguageAPI is the most professional way to scale your server. It cleans up your configs, makes management easier for your staff, and significantly increases player retention by making your server accessible to everyone worldwide.
