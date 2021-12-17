# FabricLockette
![FabricLockette banner](./ressources/FabricLockette_banner.png)

![Build](https://github.com/DevEkode/FabricLockette/workflows/Build/badge.svg)

Port of the famous bukkit/spigot plugin "Lockette" for Fabric

* Re-made plugin :  [LockettePro](https://www.spigotmc.org/resources/lockettepro-for-1-14-1-15-1-16.74354/)
* Original plugin : [Lockette](https://www.spigotmc.org/resources/lockettepro-uuid-support-abandoned.20427/)

## Dependencies
* Fabric (server) : [Installation tutorial](https://fabricmc.net/use/)
* Fabric API : [Curseforge page](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

## Supported Minecraft version

| MC Version |     Supported      | Status                                                                                                                                                                                                                 |
|------------|:------------------:|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1.18       | :heavy_check_mark: | [![Release](https://github.com/DevEkode/FabricLockette/actions/workflows/release.yml/badge.svg?branch=ver%2F1.18.1&event=workflow_dispatch)](https://github.com/DevEkode/FabricLockette/actions/workflows/release.yml) |
| 1.17       | :heavy_check_mark: | [![Release](https://github.com/DevEkode/FabricLockette/actions/workflows/release.yml/badge.svg?branch=ver%2F1.17.1&event=workflow_dispatch)](https://github.com/DevEkode/FabricLockette/actions/workflows/release.yml) |
| 1.16.5     | :heavy_check_mark: | [![Release](https://github.com/DevEkode/FabricLockette/actions/workflows/release.yml/badge.svg?branch=ver%2F1.16.5&event=workflow_dispatch)](https://github.com/DevEkode/FabricLockette/actions/workflows/release.yml) |

:heavy_check_mark: : Supported.  
:x: : Not supported.  
:construction: : Work being done to be soon supported.

## Features
* Prevent any protected block to be used by another player
* Protect from TNT
* Support UUID (only with `online-mode=true`, it will use usernames instead)
* Server-side Fabric installation (no client mod required)
* No database needed (everything is stored in the world data as nbt tags)
* Enable and disable protection for each supported blocks.
* Support multi-language (en_US, fr_FR)

## Blocks supported
* Chest (and double chest)
* Door (and double doors with a single sign)
* Chulker box

## How to install
Go to the last release page and grab the corresponding version.  
Then copy your `Fabric API` and `FabricLockette` jar into the `mods` folder of your server.

Start your server, the config file should appear into the mods folder.  
Check the wiki page for more details : [Config wiki](https://github.com/DevEkode/FabricLockette/wiki/Config)

## How to use
1. Choose a container or door to protect.
2. Place a sign with the keyboard `[Private]` on the first line.
3. Add your and friends username below.
4. Close the sign and it should be protected.

### More tips
* You can place multiple sign on one container / door by using the `[More Users]` tag.
* Every server operator can edit, open and break any protected blocks.
