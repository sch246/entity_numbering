package com.sch246.entity_numbering;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

public class ResetNameCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("resetname")
            .requires(source -> source.hasPermissionLevel(2)) // 需要权限等级2
            .then(CommandManager.argument("targets", EntityArgumentType.entities())
                .executes(ResetNameCommand::execute)));
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, "targets");
        int count = 0;

        for (Entity entity : targets) {
            if (entity.hasCustomName()) {
                entity.setCustomName(null);
                count++;
            }
        }

        if (count > 0) {
            int count2 = count;
            context.getSource().sendFeedback(
                () -> Text.translatable("command.entity_numbering.resetname.success", count2), 
                true
            );
        } else {
            context.getSource().sendFeedback(
                () -> Text.translatable("command.entity_numbering.resetname.failure"), 
                false
            );
        }

        return count;
    }
}