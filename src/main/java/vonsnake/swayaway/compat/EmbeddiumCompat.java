package vonsnake.swayaway.compat;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import org.embeddedt.embeddium.api.OptionGroupConstructionEvent;
import org.embeddedt.embeddium.api.options.OptionIdentifier;
import org.embeddedt.embeddium.api.options.control.CyclingControl;
import org.embeddedt.embeddium.api.options.structure.Option;
import org.embeddedt.embeddium.api.options.structure.OptionImpl;
import org.embeddedt.embeddium.api.options.structure.OptionStorage;
import vonsnake.swayaway.CamBobOptions;
import vonsnake.swayaway.SwayAwayMod;
import vonsnake.swayaway.SwayAwayConfig;

import java.util.List;

public class EmbeddiumCompat {

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
