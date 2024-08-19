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



	// 通过nbt判断是否有Owner
	@Unique
	public boolean hasOwner() {
		return ((LivingEntity) (Object) this)
				.writeNbt(new NbtCompound())
				.contains("Owner");
	}

	// 广播死亡信息
	@Unique
	public void broadcastDeathMessage(Text deathMessage) {
		// 如果为null，则不广播
		if (deathMessage == null) {
			return;
		}

		int distance = EntityNumbering.CONFIG.boardcastDistance;

		LivingEntity entity = (LivingEntity) (Object) this;
		// 向半径内的玩家广播死亡信息
		if (distance >0){
			entity.getWorld().getPlayers().stream()
					.filter(player -> player.squaredDistanceTo(entity) <= distance * distance)
					.forEach(player -> player.sendMessage(deathMessage, false));
		} else if (distance == 0) {
			entity.getWorld().getPlayers().stream()
					.forEach(player -> player.sendMessage(deathMessage, false));
		}
	}











	@SuppressWarnings("resource")
	@Inject(at = @At("HEAD"), method = "onDeath")
    private void onDeath(DamageSource source, CallbackInfo ci) {
		// 如果计数未开启
		if (!EntityNumbering.CONFIG.enableDeathMessages) {
			return;
		}

		LivingEntity entity = (LivingEntity) (Object) this;

		// 如果客户端未开启
		if (entity.getWorld().isClient) {
			return;
		}

		// 如果没开启设置且正好没有名字
		if (EntityNumbering.CONFIG.boardcastNeedName && !entity.hasCustomName()){
			return;
		}

		// 如果是玩家或有主人
		if (entity instanceof ServerPlayerEntity || hasOwner()) {
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
		broadcastDeathMessage(this.deathMessage);
	}
}