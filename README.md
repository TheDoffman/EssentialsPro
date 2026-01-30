# EssentialsPro Plugin

EssentialsPro is a highly versatile and efficient Minecraft plugin designed to offer a variety of essential commands and features that enhance the gameplay experience. With EssentialsPro, server administrators and players have access to a range of commands for teleportation, moderation, inventory management, and more.

## Features
- **Moderation Tools**: Kick, ban, mute, freeze, warnings, and vanish.
- **Teleportation**: `/tp`, `/tpa`, `/tpaccept`, `/tpdeny`, `/tphere`, `/tpall`, and `/tpaall`.
- **Gamemode and Weather Control**: Change gamemodes and adjust the weather.
- **Economy and Utility**: `/feed`, `/heal`, `/repair`, `/fly`, `/hat`, `/enderchest`.
- **Custom MOTD**: Update the server’s MOTD directly from in-game.
- **Chat Management**: Broadcast messages and clear the chat.
- **Inventory Viewing**: Inspect and clear other players' inventories.
- **Teleportation to Spawn**: Set and teleport to spawn points, plus `/spawnall`.
- **Homes and Warps**: Set homes, list them, and create server warps.
- **Random Teleport**: `/rtp` for safe random teleportation.
- **Back History**: `/back [steps]` for recent locations.
- **On-the-go Crafting**: Open a crafting table anywhere with `/workbench`.
- **Messaging**: Configurable prefix and locale support.
- And many more!

## Installation

1. Download the EssentialsPro plugin `.jar` file from the [releases page](#).
2. Place the `.jar` file in your server’s `plugins` folder.
3. Restart or reload your server.

## Configuration

After the plugin is installed, a configuration file (`config.yml`) will be generated in the `plugins/EssentialsPro/` directory. You can customize the MOTD, set default spawn points, and more from this file.

To apply changes to the configuration without restarting the server, you can use the command:

## Commands and Permissions

### Admin Commands
| Command                | Description                                  | Permission                   |
|------------------------|----------------------------------------------|------------------------------|
| `/kick <player>`       | Kicks a player from the server.              | `essentialspro.kick`         |
| `/ban <player>`        | Bans a player from the server.               | `essentialspro.ban`          |
| `/unban <player>`      | Unbans a player from the server.             | `essentialspro.unban`        |
| `/broadcast <msg>`     | Broadcasts a message to the server.          | `essentialspro.broadcast`    |
| `/smite <player>`      | Strikes a player with lightning.             | `essentialspro.smite`        |
| `/clearchat`           | Clears the chat for all players.             | `essentialspro.clearchat`    |
| `/spawnall`            | Teleports all players to spawn.              | `essentialspro.spawnall`     |
| `/warn <player> <msg>` | Warns a player with a reason.                | `essentialspro.warn`         |
| `/warnings [player]`   | Lists warnings (self or other).              | `essentialspro.warnings`     |
| `/clearwarnings <p>`   | Clears warnings for a player.                | `essentialspro.clearwarnings`|
| `/vanish`              | Toggle vanish mode.                          | `essentialspro.vanish`       |

### Player Commands
| Command                 | Description                                      | Permission                   |
|-------------------------|--------------------------------------------------|------------------------------|
| `/spawn`                | Teleports the player to the server's spawn.       | `essentialspro.spawn`        |
| `/setspawn`             | Sets the server's spawn point.                    | `essentialspro.setspawn`     |
| `/tp <player>`          | Teleports to another player.                      | `essentialspro.teleport`     |
| `/tpa <player>`         | Sends a teleport request to another player.       | `essentialspro.tpa`          |
| `/tpaccept`             | Accepts a pending teleport request.               | `essentialspro.tpaccept`     |
| `/tpdeny`               | Denies a pending teleport request.                | `essentialspro.tpdeny`       |
| `/tpall`                | Teleports all players to you.                     | `essentialspro.tpall`        |
| `/tpaall`               | Sends a teleport request to everyone.             | `essentialspro.tpaall`       |
| `/tphere <player>`      | Teleports a player to your location.              | `essentialspro.tphere`       |
| `/workbench`            | Opens a crafting table GUI from anywhere.         | `essentialspro.workbench`    |
| `/enderchest`           | Opens your ender chest.                           | `essentialspro.enderchest`   |
| `/hat`                  | Wear the item in your hand as a hat.              | `essentialspro.hat`          |
| `/heal [player]`        | Heals yourself or another player.                 | `essentialspro.heal`         |
| `/feed [player]`        | Feeds yourself or another player.                 | `essentialspro.feed`         |
| `/gamemode <mode>`      | Changes your gamemode (creative, survival, etc.). | `essentialspro.gamemode`     |
| `/weather <type>`       | Changes the weather (clear, rain, storm).         | `essentialspro.weather`      |
| `/time <day|night>`     | Changes the time of day.                          | `essentialspro.time`         |
| `/fly [player]`         | Toggles flight mode for yourself or another.      | `essentialspro.fly`          |
| `/back [steps]`         | Teleport to a previous location.                  | `essentialspro.back`         |
| `/sethome [name]`       | Set a home location.                              | `essentialspro.sethome`      |
| `/home [name]`          | Teleport to a home location.                      | `essentialspro.home`         |
| `/homes`                | List your homes.                                  | `essentialspro.homes`        |
| `/delhome <name>`       | Delete a home.                                    | `essentialspro.delhome`      |
| `/renamehome <o> <n>`   | Rename a home.                                    | `essentialspro.renamehome`   |
| `/warp <name>`          | Teleport to a warp.                               | `essentialspro.warp`         |
| `/setwarp <name>`       | Set a warp.                                       | `essentialspro.setwarp`      |
| `/warps`                | List warps.                                       | `essentialspro.warps`        |
| `/rtp`                  | Random teleport to a safe location.               | `essentialspro.rtp`          |
| `/seen <player>`        | Show first/last seen times.                       | `essentialspro.seen`         |

### Additional Permissions
| Permission                    | Description                                            |
|--------------------------------|--------------------------------------------------------|
| `essentialspro.*`              | Grants access to all EssentialsPro commands.           |
| `essentialspro.homes.<n>`      | Home limit override (1..max).                          |
| `essentialspro.freeze`         | Allows a player to freeze or unfreeze another player.  |
| `essentialspro.repair`         | Allows a player to repair items in their hand.         |
| `essentialspro.listplayers`    | Allows a player to list all online players.            |
| `essentialspro.motd`           | Allows a player to update the MOTD.                    |
| `essentialspro.inventorysee`   | Allows a player to view another player's inventory.    |
| `essentialspro.warnings.others`| Allows viewing other players' warnings.                |

## Configuration Options

The `config.yml` contains options for customizing various aspects of EssentialsPro, including messaging, RTP, back history, and home limits.

### Example Configuration (`config.yml`):

```yaml
messages:
  locale: "en"
  prefix: "<gray>[EssentialsPro]</gray>"

rtp:
  minRadius: 1000
  maxRadius: 5000
  attempts: 10
  worlds: []

back:
  maxHistory: 5

homes:
  defaultLimit: 1
  maxLimit: 10
```
