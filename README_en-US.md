# Entity Numbering Mod

Repository: [https://github.com/sch246/entity_numbering](https://github.com/sch246/entity_numbering)

## Overview

Entity Numbering Mod is a Fabric mod that adds cumulative numbering to entities in Minecraft by species and provides death message broadcasting functionality.

## Features

- Adds unique numbering to entities in the game (can be disabled)
  - Named entities will have the tag: `entity_numbering.named`, which can also be used to prevent entities from being named
- Broadcasts entity death messages (can be disabled)
- Customizable death message broadcast range
- Customizable separator between entity name and number
- Special handling for zombies infecting villagers and creeper explosions
- Added the `resetname` command to clear names


## Known Issues

- It affects all unnamed living entities in the world, as I currently don't know how to distinguish between pre-existing entities and newly spawned ones

## Installation

1. Make sure you have fabric-api and Fabric loader installed, with loader version >=0.16.0
2. Download the .jar file of this mod
3. Place the downloaded .jar file into your Minecraft `mods` folder
4. Launch the game!

## Configuration

The mod's configuration file should be located at `.minecraft/config/entity_numbering.json`. You can modify the following settings as needed:

```json
{
  "enableNumbering": true,
  "enableDeathMessages": true,
  "boardcastNeedName": true,
  "boardcastDistance": 128,
  "nameSeparator": " #"
}
```

- `enableNumbering`: Whether to enable entity numbering feature
- `enableDeathMessages`: Whether to enable death message broadcasting
- `boardcastNeedName`: Whether to broadcast death messages only for named entities
- `boardcastDistance`: The range of death message broadcasting (in blocks)
- `nameSeparator`: The separator between entity name and number

For debugging purposes, you can open `/saves/<world_name>/data/entity_counter.json` to modify the current number count for each type of entity

## Uninstallation

- Modify `.minecraft/config/entity_numbering.json` and set `enableNumbering` to `false`
- Run `/resetname @e[tag=entity_numbering.named]`, make sure all named entities are affected (some entities might be in unloaded chunks)
- Run `/execute as @e[tag=entity_numbering.named] unless data entity @s CustomName run tag @s remove entity_numbering.named` to remove the tag from entities without a custom name
- Close the game
- Delete the `entity_numbering-x.x.x.jar` file from the `mods` folder

## Compatibility

This mod is compatible with Minecraft version 1.21-1.21.1.

Other versions have not been tested.

## Contributions

Issues and pull requests are welcome.

## License

This project is licensed under the [MIT](./LICENSE) License.

## Contact

For any questions or suggestions, please contact QQ 980001119