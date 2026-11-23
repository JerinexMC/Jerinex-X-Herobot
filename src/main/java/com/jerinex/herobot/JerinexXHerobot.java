package com.jerinex.herobot;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public final class JerinexXHerobot implements ModInitializer {
    public static final String MOD_ID = "jerinex_x_herobot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final Pattern SAFE_BOT_NAME = Pattern.compile("[A-Za-z0-9_]{1,16}");

    @Override
    public void onInitialize() {
        boolean heroBotLoaded = FabricLoader.getInstance().isModLoaded("herobot");
        LOGGER.info("Jerinex X Herobot v0.1.0 initializing. HeroBot loaded: {}", heroBotLoaded);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            dispatcher.register(Commands.literal("jx")
                .requires(source -> source.getPermissionLevel() >= 2)
                .then(Commands.literal("status")
                    .executes(context -> status(context.getSource())))
                .then(Commands.literal("use")
                    .then(Commands.argument("bot", StringArgumentType.word())
                        .executes(context -> runPlayerCommand(
                            context.getSource(),
                            StringArgumentType.getString(context, "bot"),
                            "use once"))))
                .then(Commands.literal("attack")
                    .then(Commands.argument("bot", StringArgumentType.word())
                        .executes(context -> runPlayerCommand(
                            context.getSource(),
                            StringArgumentType.getString(context, "bot"),
                            "attack once"))))
                .then(Commands.literal("attackstop")
                    .then(Commands.argument("bot", StringArgumentType.word())
                        .executes(context -> runPlayerCommand(
                            context.getSource(),
                            StringArgumentType.getString(context, "bot"),
                            "attack"))))
                .then(Commands.literal("stop")
                    .then(Commands.argument("bot", StringArgumentType.word())
                        .executes(context -> runPlayerCommand(
                            context.getSource(),
                            StringArgumentType.getString(context, "bot"),
                            "stop"))))
            )
        );
    }

    private static int status(CommandSourceStack source) {
        boolean loaded = FabricLoader.getInstance().isModLoaded("herobot");
        source.sendSuccess(() -> Component.literal(
            "[JX] Jerinex X Herobot is running. HeroBot: " + (loaded ? "detected" : "NOT FOUND")
        ), false);
        return loaded ? 1 : 0;
    }

    private static int runPlayerCommand(CommandSourceStack source, String botName, String action) {
        if (!SAFE_BOT_NAME.matcher(botName).matches()) {
            source.sendFailure(Component.literal("[JX] Invalid bot name."));
            return 0;
        }

        MinecraftServer server = source.getServer();
        if (!FabricLoader.getInstance().isModLoaded("herobot")) {
            source.sendFailure(Component.literal("[JX] HeroBot is not installed or was not detected."));
            return 0;
        }

        String command = "player " + botName + " " + action;
        try {
            server.getCommands().performPrefixedCommand(
    server.createCommandSourceStack(),
    command
);
return 1;
        } catch (RuntimeException exception) {
            LOGGER.error("Failed to dispatch HeroBot command: {}", command, exception);
            source.sendFailure(Component.literal("[JX] Could not dispatch HeroBot command. Check the server log."));
            return 0;
        }
    }
}
