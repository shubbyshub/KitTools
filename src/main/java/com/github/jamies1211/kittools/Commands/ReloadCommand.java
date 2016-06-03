package com.github.jamies1211.kittools.Commands;

import com.github.jamies1211.kittools.KitTools;
import com.github.jamies1211.kittools.Messages;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.serializer.TextSerializers;

/**
 * Created by Jamie on 02-Jun-16.
 */
public class ReloadCommand implements CommandExecutor{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		KitTools.plugin.reload();
		src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(Messages.kitPrefix + Messages.configReloaded));
		return CommandResult.success();
	}
}
