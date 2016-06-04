package com.github.jamies1211.kittools.Commands;

import com.github.jamies1211.kittools.KitTools;
import com.github.jamies1211.kittools.Messages;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.translator.ConfigurateTranslator;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jamie on 02-Jun-16.
 */
public class KitGiveCommand implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		ConfigurationNode config = KitTools.plugin.getConfig();

		final String kit = args.<String>getOne("kit").get();
		final Player player = args.<Player>getOne("targetPlayer").get();

		ArrayList<String> kitList = new ArrayList<String>(Arrays.asList(config.getNode("1 - kitlist").getString().split(", ")));


		if (kitList.contains(kit)) {
			for (Object key : config.getNode("2 - kits", kit).getChildrenMap().keySet()) {
				ItemStack stack;

				stack = ItemStack.builder().fromContainer(ConfigurateTranslator.instance().translateFrom(config.getNode("2 - kits", kit, key))).build();

				player.getInventory().offer(stack);
			}
			player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(Messages.kitPrefix + Messages.kitReceived + kit));
			src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(Messages.kitPrefix + Messages.kitGiven + kit + " to " + player.getName()));
		} else {
			src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(Messages.kitPrefix + Messages.kitNotExist));
		}

		return CommandResult.success();
	}
}