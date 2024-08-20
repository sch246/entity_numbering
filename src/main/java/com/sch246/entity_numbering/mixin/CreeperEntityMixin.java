package com.sch246.entity_numbering.mixin;

import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sch246.entity_numbering.EntityNumbering;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin {

    @Inject(method = "explode", at = @At("HEAD"))
    private void onExplode(CallbackInfo ci) {
        CreeperEntity creeper = (CreeperEntity) (Object) this;
        World world = creeper.getWorld();

        if (!world.isClient) {
            // 在这里处理苦力怕爆炸事件
            handleCreeperExplosion(creeper);
        }
    }

    private void handleCreeperExplosion(CreeperEntity creeper) {
        // 创建爆炸消息
        Text explosionMessage = Text.translatable("death.attack.explosion", creeper.getDisplayName()).formatted(Formatting.GRAY);

        EntityNumbering.broadcastDeathMessage(explosionMessage, creeper);
    }
}