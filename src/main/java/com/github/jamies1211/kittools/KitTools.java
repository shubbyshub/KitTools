package com.github.jamies1211.kittools;

/**
 * Created by Jamie on 02-Jun-16.
 */

import com.github.jamies1211.kittools.Commands.*;
import com.google.inject.Inject;

import java.io.File;
import java.io.IOException;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.text.Text;

import java.util.*;
import java.util.logging.Logger;

import static com.github.jamies1211.kittools.Messages.*;

@Plugin(id = "kittools", name = "Kit Tools", version = "1.0.0",
		description = "Adds kits",
		authors = {"JamieS1211"},
		url = "http://pixelmonweb.officialtjp.com")
public class KitTools {

	Scheduler scheduler = Sponge.getScheduler();
	Task.Builder taskBuilder = scheduler.createTaskBuilder();

	@Inject
	private Logger logger;
	private ConfigurationNode config;
	public static KitTools plugin;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private File defaultConfig;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;

	public Logger getLogger() {
		return this.logger;
	}

	public ConfigurationNode getConfig() {
		return this.config;
	}

	public File getDefaultConfig() {
		return this.defaultConfig;
	}

	public ConfigurationLoader<CommentedConfigurationNode> getConfigManager() {
		return this.configManager;
	}

	@Listener
	public void onServerStart(GameInitializationEvent event) {
		getLogger().info(startup);

		final HashMap<List<String>, CommandSpec> subcommands = new HashMap<List<String>, CommandSpec>();

		subcommands.put(Arrays.asList("create"), CommandSpec.builder()
				.permission("kit.create")
				.description(Text.of("Create a new empty kit with the name"))
				.extendedDescription(Text.of("/kit create [kit]"))
				.arguments(
						GenericArguments.onlyOne(GenericArguments.string(Text.of("kit"))))
				.executor(new KitCreateCommand())
				.build());

		subcommands.put(Arrays.asList("add"), CommandSpec.builder()
				.permission("kit.add")
				.description(Text.of("Add the curent held item to the kit in the quantity as held"))
				.extendedDescription(Text.of("/kit add [kit] [quantity]"))
				.arguments(
						GenericArguments.onlyOne(GenericArguments.string(Text.of("kit"))))
				.executor(new KitAddItemCommand())
				.build());

		subcommands.put(Arrays.asList("reload"), CommandSpec.builder()
				.permission("kit.reload")
				.description(Text.of("Reloads config"))
				.extendedDescription(Text.of("/kit reload"))
				.executor(new ReloadCommand())
				.build());

		subcommands.put(Arrays.asList("give"), CommandSpec.builder()
				.description(Text.of("Gives the specified kit to the specified player"))
				.extendedDescription(Text.of("Gives the specified kit to the specified player"))
				.arguments(
						GenericArguments.onlyOne(GenericArguments.string(Text.of("kit"))),
						GenericArguments.onlyOne(GenericArguments.player(Text.of("targetPlayer"))))
				.executor(new KitGiveCommand())
				.build());

		subcommands.put(Arrays.asList("help"), CommandSpec.builder()
				.description(Text.of("Shows kit help"))
				.extendedDescription(Text.of("Use this command to list all the kit commands"))
				.executor(new KitHelpCommand())
				.build());

		final CommandSpec kitCommand = CommandSpec.builder()
				.description(Text.of("Gives the user the kit"))
				.extendedDescription(Text.of("Use this command to get a kit"))
				.arguments(
						GenericArguments.onlyOne(GenericArguments.string(Text.of("kit"))))
				.executor(new KitGetCommand())
				.children(subcommands)
				.build();

		Sponge.getCommandManager().register(this, kitCommand, "kit");

		try {
			if (!defaultConfig.exists()) {
				defaultConfig.createNewFile();
				config = configManager.load();
				configManager.save(config);
				setupconfig();
				save();
				getLogger().info(newConfigFile);
			} else {
				getLogger().info(loadedConfigFile);
			}
			config = configManager.load();

		} catch (IOException exception) {
			getLogger().info(configLoadError);
		}
	}

	@Listener
	public void onServerLoadComplete(GameLoadCompleteEvent event) {
		plugin = this;
		reload();
	}

	private void setupconfig() {
		this.config.getNode("1 - kitlist").setValue("");
		save();
	}

	public void save() {
		try {
			getConfigManager().save(this.config);
		} catch (final IOException e) {
			getLogger().info(configSaveError);
		}
	}

	public void reload() {
		try {
			this.config = getConfigManager().load();
			getLogger().info(configReloaded);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}