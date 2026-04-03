package com.horizonpack.horizoncore.commands;

import com.horizonpack.horizoncore.data.WorldAgeData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class AdminCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("horizon")
                .requires(source -> source.hasPermission(2)) // Level 2 = Op/Admin
                .then(Commands.literal("setday")
                        .then(Commands.argument("days", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    int days = IntegerArgumentType.getInteger(context, "days");
                                    var level = context.getSource().getLevel();

                                    WorldAgeData.get(level).setDay(days, level);

                                    context.getSource().sendSuccess(() ->
                                            Component.literal("§6[Horizon] §fWorld set to Day " + days), true);
                                    return 1;
                                })
                        )
                )
        );
    }
}