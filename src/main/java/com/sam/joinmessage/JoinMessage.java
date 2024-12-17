package com.sam.joinmessage;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class JoinMessage implements ModInitializer {
	public static final String MOD_ID = "join-message";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		Config.Messages messages = Config.loadConfig();
		if (messages == null) {
			LOGGER.error("Join Message failed to initialize.");
			return;
		}

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			if (messages.welcome != null) {
				player.sendMessage(Text.literal(" " + messages.welcome).formatted(Formatting.GREEN));
			}
			if (!messages.random.isEmpty()) {
				Random random = new Random();
				int randInt = random.nextInt(messages.random.size());
				player.sendMessage(Text.literal(" " + messages.random.get(randInt)).formatted(Formatting.DARK_GREEN));
			}
			LOGGER.info("Sent join message to {}", handler.getPlayer().getName().asTruncatedString(99));
		});

		LOGGER.info("Join Message initialized.");
	}
}