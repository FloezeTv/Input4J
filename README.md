# Input4J

Input4J is a [Godot](https://github.com/godotengine/godot)-like Input System for Java.

## Description

Input4J aims to bring an easy to use input-system to Java.
It is inspired by the input-system the Godot game engine is using.
Input4J lets you add unified input events using easy identifiers (like Strings) and add input actions to them, so you don't have to listen to them separately.

## Features

- receive input events based on easy to use names
- save/load input configs

### Planned

- keyboard/mouse input from AWT/swing
- controller input using [libSDL](https://www.libsdl.org/)
- *swing-based input change GUI* (not certain)

## Folders

### `core`

This is the core library of Input4J.
It contains the code used to interact with Input4J, but no input source of its own.  
To get an input source, you have to define one yourself.

## Branches

| Branch name                | branch from | merge into      | description                               |
| -------------------------- | ----------- | --------------- | ----------------------------------------- |
| **master**                 |             |                 | current released version                  |
| **develop**                |             |                 | current development version               |
| **release/`version`**      | develop     | master, develop | prepares for releases (bump version, ...) |
| **fix/`issue`-`name`**     | develop     | develop         | fix for a bug                             |
| **hotfix/`issue`-`name`**  | master      | master, develop | hotfix for a currently released version   |
| **feature/`issue`-`name`** | develop     | develop         | new feature                               |

With:
- `version`: a release version of the project
- `issue`: a issue reference that tracks that bug/hotfix/feature
- `name`: a short descriptive name of the bug/hotfix/feature