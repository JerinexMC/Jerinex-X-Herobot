# Jerinex X Herobot

A server-side Fabric mod for Minecraft Java 1.21.11 that adds configurable automation around HeroBot.

## Requirements

- Minecraft Java 1.21.11
- Fabric Loader 0.18.1 or newer
- Fabric API for 1.21.11
- HeroBot 1.4.3+v260315
- Java 21

Install the built JAR into the server's `mods` folder alongside HeroBot.

## First milestone commands

All commands require permission level 2 (operator).

- `/jx status` — checks whether this mod and HeroBot are loaded.
- `/jx use <bot>` — sends HeroBot `/player <bot> use once`.
- `/jx attack <bot>` — sends HeroBot `/player <bot> attack once`.
- `/jx attackstop <bot>` — sends HeroBot `/player <bot> attack` to stop continuous attacking.
- `/jx stop <bot>` — sends HeroBot `/player <bot> stop`.

This first milestone is only the command bridge. Mole Mode and web-combat automation are not implemented yet.

## Build

The GitHub Actions workflow builds with Java 21 and Gradle 9.2. Download the JAR from the workflow run's **Artifacts** section.

For a local build with Gradle 9.2 installed:

```bash
gradle clean build
```

The remapped mod JAR is produced in `build/libs/`.
