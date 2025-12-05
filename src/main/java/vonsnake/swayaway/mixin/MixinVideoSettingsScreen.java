package vonsnake.swayaway.mixin;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vonsnake.swayaway.SwayAwayConfig;

@Mixin(VideoSettingsScreen.class)
@OnlyIn(Dist.CLIENT)
public class MixinVideoSettingsScreen {

    @Inject(at = @At("RETURN"), method = "options", cancellable = true)
    private static void options(Options options, CallbackInfoReturnable<OptionInstance<?>[]> cir){
        OptionInstance<?>[] vanillaOptions = cir.getReturnValue();

        OptionInstance<?>[] modifiedOptions = new OptionInstance<?>[vanillaOptions.length];
        for (int i = 0; i < vanillaOptions.length; ++i){
            if (vanillaOptions[i] == options.bobView()){
                modifiedOptions[i] = SwayAwayConfig.vanillaBobCamOption;
            } else {
                modifiedOptions[i] = vanillaOptions[i];
            }
        }

        cir.setReturnValue(modifiedOptions);
    }
}
