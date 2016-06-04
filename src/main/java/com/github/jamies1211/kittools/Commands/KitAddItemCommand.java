package com.github.jamies1211.kittools.Commands;

import com.github.jamies1211.kittools.KitTools;
import com.github.jamies1211.kittools.Messages;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.translator.ConfigurateTranslator;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.*;

import static co.aikar.timings.Timings.of;

/**
 * Created by Jamie on 02-Jun-16.
 */
public class KitAddItemCommand implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		if (src instanceof Player) {
			Player player = (Player) src;

			ConfigurationNode config = KitTools.plugin.getConfig();

			final String kit = args.<String>getOne("kit").get();

			ArrayList<String> kitList = new ArrayList<String>(Arrays.asList(config.getNode("1 - kitlist").getString().split(", ")));

			if (kitList.contains(kit)) {

				int currentSize = config.getNode("2 - kits", kit).getChildrenMap().keySet().size();

				ItemStack stack = player.getItemInHand().get();
				ConfigurateTranslator.instance().translateContainerToData(config.getNode("2 - kits", kit, "item" + (currentSize + 1)), stack.toContainer());
				KitTools.plugin.save();
				src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(Messages.kitPrefix + Messages.kitItemAdded + " " + stack.getItem().getName() + " to kit " + kit));
			} else {
				src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(Messages.kitPrefix + Messages.kitNotExist));
			}

		} else {
			src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(Messages.playerCommandError));
		}
		return CommandResult.success();
	}
}