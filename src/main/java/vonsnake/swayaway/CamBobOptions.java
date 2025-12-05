package vonsnake.swayaway;

import net.minecraft.util.ByIdMap;
import net.minecraft.util.OptionEnum;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.IntFunction;

@OnlyIn(Dist.CLIENT)
public enum CamBobOptions implements OptionEnum {
    DISABLED(0, "swayaway.option.disabled"),
    MODELONLY(1, "swayaway.option.modelonly"),
    FULL(2, "swayaway.option.full");

    private static final IntFunction<CamBobOptions> BY_ID = ByIdMap.continuous(CamBobOptions::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    private final int id;
    private final String key;

    private CamBobOptions(int id, String key) {
        this.id = id;
        this.key = key;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getKey() {
        return key;
    }

    public static CamBobOptions byId(int id){
        return BY_ID.apply(id);
    }
}
