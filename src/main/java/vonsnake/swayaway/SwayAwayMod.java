package vonsnake.swayaway;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import vonsnake.swayaway.compat.EmbeddiumCompat;

@Mod(value = SwayAwayMod.MODID, dist = Dist.CLIENT)
public class SwayAwayMod {
    public static final String MODID = "swayaway";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SwayAwayMod(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("sex");
        modContainer.registerConfig(ModConfig.Type.CLIENT, SwayAwayConfig.SPEC);

        HandleCompat();
    }

    private void HandleCompat() {
        if (ModList.get().isLoaded("embeddium")) NeoForge.EVENT_BUS.register(EmbeddiumCompat.class);
    }

}
