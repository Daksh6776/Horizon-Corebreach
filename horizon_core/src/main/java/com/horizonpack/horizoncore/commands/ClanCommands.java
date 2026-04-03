package com.horizonpack.horizoncore.commands;

import com.horizonpack.horizoncore.data.WorldAgeData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import java.util.UUID;

public class ClanCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("clan")
                .then(Commands.literal("create")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(context -> {
                                    ServerPlayer p = context.getSource().getPlayerOrException();
                                    WorldAgeData.get(p.serverLevel()).createClan(p.getUUID(), StringArgumentType.getString(context, "name"));
                                    return 1;
                                })))
                .then(Commands.literal("invite")
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> {
                                    ServerPlayer sender = context.getSource().getPlayerOrException();
                                    ServerPlayer target = EntityArgument.getPlayer(context, "target");
                                    target.sendSystemMessage(Component.literal("Invite from " + sender.getName().getString())
                                            .withStyle(s -> s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan accept " + sender.getUUID()))));
                                    return 1;
                                })))
                .then(Commands.literal("accept")
                        .then(Commands.argument("leaderUUID", StringArgumentType.string())
                                .executes(context -> {
                                    ServerPlayer p = context.getSource().getPlayerOrException();
                                    UUID leaderId = UUID.fromString(StringArgumentType.getString(context, "leaderUUID"));
                                    WorldAgeData data = WorldAgeData.get(p.serverLevel());
                                    UUID clanId = data.getClanIdFromLeader(leaderId);
                                    if (clanId != null) data.joinClan(p.getUUID(), clanId);
                                    return 1;
                                }))));
    }
}