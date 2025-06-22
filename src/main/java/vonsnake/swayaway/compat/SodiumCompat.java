package vonsnake.swayaway.compat;

import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.embeddedt.embeddium.api.OptionGroupConstructionEvent;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;
import vonsnake.swayaway.CamBobOptions;
import vonsnake.swayaway.SwayAwayMod;
import vonsnake.swayaway.SwayAwayConfig;

import java.util.List;

public class SodiumCompat {

    public static final OptionStorage<?> STORAGE = new OptionStorage<Object>() {
        @Override
        public Object getData() {
            return SwayAwayConfig.getCurrentCamBobOption();
        }

        @Override
        public void save() {
            SwayAwayConfig.BOB_CAM.save();
        }
    };

    @SubscribeEvent
    public static void editGeneralGroup(final OptionGroupConstructionEvent e){

        List<Option<?>> options = e.getOptions();
        for (int i = 0; i < options.size(); ++i){
            Option<?> option = options.get(i);

            OptionIdentifier<?> id = option.getId();
            if (id == null) continue;

            String path = id.getPath();
            if (path == null) continue;

            if (path.equals("view_bobbing")){
                updateViewBobbingOption(options, i);
                return;
            }
        }
    }

    private static void updateViewBobbingOption(List<Option<?>> options, int index){
        Option<?> newOption = OptionImpl.createBuilder(CamBobOptions.class, STORAGE)
                .setId(ResourceLocation.tryBuild(SwayAwayMod.MODID, "view_bobbing"))
                .setName(Component.translatable("options.viewBobbing"))
                .setTooltip(Component.translatable("swayaway.option.tooltip"))
                .setControl((opt) -> new CyclingControl<>(opt, CamBobOptions.class, new Component[] {
                        Component.translatable("swayaway.option.disabled"),
                        Component.translatable("swayaway.option.modelonly"),
                        Component.translatable("swayaway.option.full")
                }))
                .setBinding(
                        (s, g) -> SwayAwayConfig.applyCamBobOption(g),
                        (opts) -> SwayAwayConfig.getCurrentCamBobOption()
                        ).build();

        options.set(index, newOption);
    }
}
