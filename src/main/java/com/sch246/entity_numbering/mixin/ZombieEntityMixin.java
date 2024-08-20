package com.sch246.entity_numbering.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.sch246.entity_numbering.EntityNumbering;


@Mixin(ZombieEntity.class)
public class ZombieEntityMixin {

    @Inject(method = "onKilledOther", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onOnKilledOtherReturn(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir, boolean bl) {

        if (!bl && other instanceof VillagerEntity) {
            LivingEntityMixin villager = ((LivingEntityMixin) (Object) other);

            // 总得有个死亡消息吧
            if (villager.deathMessage == null) return;

            Text deathMessage = Text.translatable("entity_numbering.zombie_infects_villager", other.getDisplayName(), ((ZombieEntity) (Object) this).getDisplayName())
            .formatted(Formatting.DARK_RED);

            EntityNumbering.broadcastDeathMessage(deathMessage, other);

            // 重置死亡消息
            villager.deathMessage = null;
        }
    }
}
