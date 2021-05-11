# FabricLockette
![FabricLockette banner](./ressources/FabricLockette_banner.png)

![Build](https://github.com/DevEkode/FabricLockette/workflows/Build/badge.svg)
[![GitHub Super-Linter](https://github.com/DevEkode/FabricLockette/workflows/Lint%20Code%20Base/badge.svg)](https://github.com/marketplace/actions/super-linter)

Port of the famous bukkit/spigot plugin "Lockette" for Fabric

* Re-made plugin :  [LockettePro](https://www.spigotmc.org/resources/lockettepro-for-1-14-1-15-1-16.74354/)
* Original plugin : [Lockette](https://www.spigotmc.org/resources/lockettepro-uuid-support-abandoned.20427/)

## Dependencies
* Fabric (server) : [Installation tutorial](https://fabricmc.net/use/)
* Fabric API : [Curseforge page](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

## How to install
Go to the last release page and grab the corresponding version.  
Then copy your `Fabric API` and `FabricLockette` jar into the `mods` folder of your server.

Start your server, the config file should appear into the mods folder.  
Check the wiki page for more details : [Config wiki](https://github.com/DevEkode/FabricLockette/wiki/Config)

## Features
* Prevent any protected block to be used by another player
* Protect from TNT
* Support UUID
* Server-side Fabric installation (no client mod required)
* No database needed (everything is stored in the world data as nbt tags)
* Enable and disable protection for each supported blocks.
* Support multi-language (en_US, fr_FR)

## Blocks supported
* Chest (and double chest)
* Door (and double doors with a single sign)
* Chulker box
