package vonsnake.swayaway;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = SwayAwayMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SwayAwayConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue BOB_CAM = BUILDER
            .comment(" Whether the camera bob is enabled. This works in conjunction with the game's bobView setting to determine the mods behaviour.")
            .define("bobCam", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    /**
     * The current value of BOB_CAM. As this setting is being referenced in a hot path, we don't want to be dealing
     * with the additional overhead of using BOB_CAM.get().
     */
    public static boolean bobCam;

    /**
     * The option to replace the vanilla View Bobbing option with.
     */
    public static OptionInstance<CamBobOptions> vanillaBobCamOption;

    /**
     * Sets the mod bob config value and then immediately saves it.
     */
    public static void setBobCam(boolean value){
        bobCam = value;
        BOB_CAM.set(value);
        BOB_CAM.save();
    }

    /**
     * Determines what the current bob option is based on the games and mods configuration.
     */
    public static CamBobOptions getCurrentCamBobOption(){
        Minecraft instance = Minecraft.getInstance();
        boolean bobView = instance.options.bobView().get();
        boolean camBob = bobCam;

        if (!bobView && !camBob) return CamBobOptions.DISABLED;
        if (bobView && !camBob) return CamBobOptions.MODELONLY;
        if (bobView && camBob) return CamBobOptions.FULL;

        return CamBobOptions.FULL;
    }

    /**
     * Uses the provided CamBobOptions to apply configuration changes to the game and mod.
     */
    public static void applyCamBobOption(CamBobOptions option){
        Minecraft instance = Minecraft.getInstance();

        instance.options.bobView().set(option.getId() > 0);
        setBobCam(option == CamBobOptions.FULL);
    }

    /**
     * Creates the option instance for the vanilla game's option menu. This will replace the game's bob view option instance.
     */
    private static void createCamBobOptionInstance(){
        if (vanillaBobCamOption != null) return;

        vanillaBobCamOption = new OptionInstance<CamBobOptions>(
                "options.viewBobbing",
                OptionInstance.noTooltip(),
                OptionInstance.forOptionEnum(),
                new OptionInstance.Enum(
                        Arrays.asList(CamBobOptions.values()),
                        Codec.INT.xmap(CamBobOptions::byId, CamBobOptions::getId)
                ), getCurrentCamBobOption(), (val) -> applyCamBobOption((CamBobOptions) val));
    }

    @SubscribeEvent
    protected static void onLoad(final ModConfigEvent event){
        bobCam = BOB_CAM.get();

        createCamBobOptionInstance();
    }
}
