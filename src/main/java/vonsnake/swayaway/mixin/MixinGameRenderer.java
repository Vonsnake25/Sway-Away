package vonsnake.swayaway.mixin;

import net.minecraft.client.renderer.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vonsnake.swayaway.SwayAwayConfig;

@Mixin(GameRenderer.class)
@OnlyIn(Dist.CLIENT)
public class MixinGameRenderer {

    /**
     * Whether the current bobView method is being called by the RenderLevel method.
     */
    @Unique
    public boolean noCamBob$applyingBobViewForRenderLevel = false;

    @Inject(method = "renderLevel(FJLcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;bobView(Lcom/mojang/blaze3d/vertex/PoseStack;F)V"))
    public void beforeRenderLevelBobView(CallbackInfo ci) {
        if (!SwayAwayConfig.bobCam) noCamBob$applyingBobViewForRenderLevel = true;
    }

    @Inject(method = "renderLevel(FJLcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;bobView(Lcom/mojang/blaze3d/vertex/PoseStack;F)V", shift = At.Shift.AFTER))
    public void afterRenderLevelBobView(CallbackInfo ci) {
        noCamBob$applyingBobViewForRenderLevel = false;
    }

    @Inject(method = "bobView(Lcom/mojang/blaze3d/vertex/PoseStack;F)V",
            at = @At("HEAD"),
            cancellable = true)
    public void bobView(CallbackInfo ci){
        if (noCamBob$applyingBobViewForRenderLevel) ci.cancel();
    }
}