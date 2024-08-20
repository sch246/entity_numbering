package com.sch246.entity_numbering.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sch246.entity_numbering.EntityNumbering;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    // 添加一个新的属性
    @Unique
    public Text deathMessage;

	private boolean shouldSkipDeathMessage(LivingEntity entity) {
		return !EntityNumbering.CONFIG.enableDeathMessages  // 死亡信息关闭
			|| entity.getWorld().isClient			// 客户端
			|| (EntityNumbering.CONFIG.boardcastNeedName && !entity.hasCustomName()) // 需要名字且没有名字
			|| entity instanceof ServerPlayerEntity 	// 是玩家
			|| entity.writeNbt(new NbtCompound()).contains("Owner");// 有Owner
	}


	@Inject(at = @At("HEAD"), method = "onDeath")
    private void onDeath(DamageSource source, CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if (shouldSkipDeathMessage(entity)) {
			return;
		}

		// 获取死亡信息，并设置为灰色
		DamageTracker damageTracker = entity.getDamageTracker();
		Text deathMessage = damageTracker.getDeathMessage().copy().formatted(Formatting.GRAY);

		// 存储死亡信息
		this.deathMessage = deathMessage;
    }

	@Inject(at = @At("TAIL"), method = "onDeath")
    private void onDeath2(DamageSource source, CallbackInfo ci) {
		// 广播死亡信息，由于broadcastDeathMessage内有null检查，所以可以不用再检查
		EntityNumbering.broadcastDeathMessage(this.deathMessage, (LivingEntity) (Object) this);
	}
}