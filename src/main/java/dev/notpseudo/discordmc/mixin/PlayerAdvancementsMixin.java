package dev.notpseudo.discordmc.mixin;

import dev.notpseudo.discordmc.minecraft.events.PlayerAdvancementCallback;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {

    @Shadow
    private ServerPlayer player;

    @Inject(
            method = "award",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/advancements/AdvancementRewards;grant(Lnet/minecraft/server/level/ServerPlayer;)V"
            )
    )
    private void onAdvancementCompleted(AdvancementHolder holder, String criterion, CallbackInfoReturnable<Boolean> cir) {
        PlayerAdvancementCallback.Companion.getEVENT().invoker()
                .onAdvancementCompleted(this.player, holder);
    }

}
