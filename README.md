# Input4J

Input4J is a [Godot](https://github.com/godotengine/godot)-like Input System for Java.

## Description

Input4J aims to bring an easy to use input-system to Java.
It is inspired by the input-system the Godot game engine is using.
Input4J lets you add unified input events using easy identifiers (like Strings) and add input actions to them, so you don't have to listen to them separately.

## Features

### Planned

- receive input events based on easy to use names
- keyboard/mouse input from AWT/swing
- controller input using [libSDL](https://www.libsdl.org/)
- save/load input configs
- *swing-based input change GUI* (not certain)

## Branches

| Branch name                | branch from       | merge into                | description                             |
| -------------------------- | ----------------- | ------------------------- | --------------------------------------- |
| **master**                 |                   |                           | current live-version                    |
| **release/`version`**      | master            |                           | released versions                       |
| **fix/`issue`-`name`**     | master            | master                    | fix for a bug                           |
| **hotfix/`issue`-`name`**  | release/`current` | release/`current`, master | hotfix for a currently released version |
| **feature/`issue`-`name`** | master            | master                    | new feature                             |

With:
- `version`: a release version of the project
- `current`: the current version ot the project
- `issue`: a issue reference that tracks that bug/hotfix/feature
- `name`: a short descriptive name of the bug/hotfix/feature