package com.sch246.entity_numbering;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Unique;

public class EntityNumbering implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("entity_numbering");

	public static final EntityNumberingConfig CONFIG = EntityNumberingConfig.load();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

        CONFIG.save(); // 保存默认配置或更新现有配置
        ServerLifecycleEvents.SERVER_STARTING.register(EntityCounter::loadData);
        ServerLifecycleEvents.SERVER_STOPPED.register(EntityCounter::saveData);


        ServerEntityEvents.ENTITY_LOAD.register(this::onEntityLoad);

        // 注册命令
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ResetNameCommand.register(dispatcher);
        });
	}

    private void onEntityLoad(Entity entity, ServerWorld world) {
        processEntity(entity);
    }

    @SuppressWarnings("resource")
	private void processEntity(Entity entity) {
        if (!CONFIG.enableNumbering
        || !(entity instanceof LivingEntity)
        || entity instanceof PlayerEntity
        || entity.getWorld().isClient
		|| entity.hasCustomName()
        || entity.getCommandTags().contains("entity_numbering.named")) {
            return;
        }

        LivingEntity livingEntity = (LivingEntity) entity;

		int count = EntityCounter.getNextCount(livingEntity.getType());
		Text currentName = livingEntity.getName();
		livingEntity.setCustomName(Text.literal(currentName.getString() + CONFIG.nameSeparator + count));
        livingEntity.addCommandTag("entity_numbering.named");
    }

	// 广播死亡信息
	@Unique
	public static void broadcastDeathMessage(Text deathMessage, Entity entity) {
		// 如果为null，则不广播
		if (deathMessage == null) {
			return;
		}

		int distance = EntityNumbering.CONFIG.boardcastDistance;

		// 向半径内的玩家广播死亡信息
		if (distance >0){
			double squaredDistance = distance * distance;
			for (PlayerEntity player : entity.getWorld().getPlayers()) {
				if (player.squaredDistanceTo(entity) <= squaredDistance) {
					player.sendMessage(deathMessage, false);
				}
			}
		} else if (distance == 0) {
			for (PlayerEntity player : entity.getWorld().getPlayers()) {
				player.sendMessage(deathMessage, false);
			}
		}
	}
}