package vonsnake.swayaway;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkConstants;
import org.slf4j.Logger;
import vonsnake.swayaway.compat.SodiumCompat;

@Mod(SwayAwayMod.MODID)
public class SwayAwayMod {
    public SwayAwayMod(){
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        if (!FMLLoader.getDist().isClient()) return;

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SwayAwayConfig.SPEC);

        HandleCompat();
    }

    private void HandleCompat() {
        String[] sodiumBasedIds = {"embeddium", "rubidium"};
        for (String modId : sodiumBasedIds){
            if (ModList.get().isLoaded(modId)){
                MinecraftForge.EVENT_BUS.register(SodiumCompat.class);
                break;
            }
        }
    }

    public static final String MODID = "swayaway";
    private static final Logger LOGGER = LogUtils.getLogger();
}
