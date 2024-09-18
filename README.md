# EssentialsPro Plugin

EssentialsPro is a highly versatile and efficient Minecraft plugin designed to offer a variety of essential commands and features that enhance the gameplay experience. With EssentialsPro, server administrators and players have access to a range of commands for teleportation, moderation, inventory management, and more.

## Features
- **Moderation Tools**: Kick, ban, mute, and freeze players.
- **Teleportation**: Use commands like `/tp`, `/tpa`, `/tpaccept`, and `/tpdeny`.
- **Gamemode and Weather Control**: Change gamemodes and adjust the weather.
- **Economy and Utility**: Commands like `/feed`, `/heal`, `/repair`, and `/fly`.
- **Custom MOTD**: Update the server’s MOTD directly from in-game.
- **Chat Management**: Broadcast messages and clear the chat.
- **Inventory Viewing**: Inspect and clear other players' inventories.
- **Teleportation to Spawn**: Set and teleport to spawn points.
- **On-the-go Crafting**: Open a crafting table anywhere with `/workbench`.
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
| Command            | Description                               | Permission               |
|--------------------|-------------------------------------------|--------------------------|
| `/kick <player>`    | Kicks a player from the server.            | `essentialspro.kick`      |
| `/ban <player>`     | Bans a player from the server.             | `essentialspro.ban`       |
| `/unban <player>`   | Unbans a player from the server.           | `essentialspro.unban`     |
| `/broadcast <msg>`  | Broadcasts a message to the server.        | `essentialspro.broadcast` |
| `/smite <player>`   | Strikes a player with lightning.           | `essentialspro.smite`     |
| `/clearchat`        | Clears the chat for all players.           | `essentialspro.clearchat` |

### Player Commands
| Command             | Description                                      | Permission               |
|---------------------|--------------------------------------------------|--------------------------|
| `/spawn`            | Teleports the player to the server's spawn.       | `essentialspro.spawn`     |
| `/setspawn`         | Sets the server's spawn point.                    | `essentialspro.setspawn`  |
| `/tp <player>`      | Teleports to another player.                      | `essentialspro.teleport`  |
| `/tpa <player>`     | Sends a teleport request to another player.       | `essentialspro.tpa`       |
| `/tpaccept`         | Accepts a pending teleport request.               | `essentialspro.tpaccept`  |
| `/tpdeny`           | Denies a pending teleport request.                | `essentialspro.tpdeny`    |
| `/workbench`        | Opens a crafting table GUI from anywhere.         | `essentialspro.workbench` |
| `/heal [player]`    | Heals yourself or another player.                 | `essentialspro.heal`      |
| `/feed [player]`    | Feeds yourself or another player.                 | `essentialspro.feed`      |
| `/gamemode <mode>`  | Changes your gamemode (creative, survival, etc.). | `essentialspro.gamemode`  |
| `/weather <type>`   | Changes the weather (clear, rain, storm).         | `essentialspro.weather`   |
| `/time <day|night>` | Changes the time of day.                          | `essentialspro.time`      |
| `/fly [player]`     | Toggles flight mode for yourself or another.      | `essentialspro.fly`       |

### Additional Permissions
| Permission                    | Description                                            |
|--------------------------------|--------------------------------------------------------|
| `essentialspro.*`              | Grants access to all EssentialsPro commands.           |
| `essentialspro.freeze`         | Allows a player to freeze or unfreeze another player.  |
| `essentialspro.repair`         | Allows a player to repair items in their hand.         |
| `essentialspro.listplayers`    | Allows a player to list all online players.            |
| `essentialspro.motd`           | Allows a player to update the MOTD.                    |
| `essentialspro.inventorysee`   | Allows a player to view another player's inventory.    |

## Configuration Options

The `config.yml` contains options for customizing various aspects of EssentialsPro. You can configure the MOTD, default permissions, spawn locations, and more.

### Example Configuration (`config.yml`):

```yaml
motd:
  default: "&aWelcome to the server!"
  custom: "&6Enjoy your stay, &e%player%&6!"
  
homeLimit:
  default: 3
  multiple: 10

spawn:
  world: "world"
  x: 100
  y: 65
  z: 200
