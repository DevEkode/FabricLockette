# FabricLockette
![Build](https://github.com/DevEkode/FabricLockette/workflows/Build/badge.svg)  

Port of the famous bukkit/spigot plugin "Lockette" for Fabric

* Re-made plugin :  [LockettePro](https://www.spigotmc.org/resources/lockettepro-for-1-14-1-15-1-16.74354/)
* Original plugin : [Lockette](https://www.spigotmc.org/resources/lockettepro-uuid-support-abandoned.20427/)

# Dependencies
* Fabric (server) : [Installation tutorial](https://fabricmc.net/use/)
* Fabric API : [Curseforge page](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

# How to install
Go to the last release page and grab the corresponding version.  
Then copy your `Fabric API` and `FabricLockette` jar into the `mods` folder of your server.

Start your server, the config file should appear into the mods folder.  
Chest the wiki page for more details :

# Features
* Prevent ant protected block to be used by another player
* Protect from TNT
* Support UUID
* Server-side Fabric installation (no client mod required)
* No database needed (everything is stored in the world data as nbt tags)
* Enable and disable protection for each supported blocks.
* Support multi-language (en_US, fr_FR)

# Blocks supported
* Chest (and double chest)
* Doors (only single door for now)
* Chulker box