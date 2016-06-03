package com.github.jamies1211.kittools.Commands;

import com.github.jamies1211.kittools.KitTools;
import com.github.jamies1211.kittools.Messages;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jamie on 02-Jun-16.
 */
public class KitCreateCommand implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		ConfigurationNode config = KitTools.plugin.getConfig();

		final String kit = args.<String>getOne("kit").get();
		ArrayList<String> kitList = new ArrayList<String>(Arrays.asList(config.getNode("1 - kitlist").getString().split(", ")));

		if (kitList.contains(kit)) {
			src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(Messages.kitPrefix + Messages.kitAlreadyExists));
		} else if (config.getNode("1 - kitlist").getString().equalsIgnoreCase("")) {
			config.getNode("1 - kitlist").setValue(kit);
			KitTools.plugin.save();
			src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(Messages.kitPrefix + Messages.kitCreated + kit));
		} else {
			config.getNode("1 - kitlist").setValue(config.getNode("1 - kitlist").getString() + ", " + kit);
			KitTools.plugin.save();
			src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(Messages.kitPrefix + Messages.kitCreated + kit));
		}

		return CommandResult.success();
	}
}