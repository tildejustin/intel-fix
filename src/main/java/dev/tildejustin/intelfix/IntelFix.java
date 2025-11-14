package dev.tildejustin.intelfix;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.spongepowered.asm.mixin.Mixins;

import java.lang.invoke.MethodHandle;

public class IntelFix implements PreLaunchEntrypoint {
    public static MethodHandle setClientActiveTexture;
    public static int defaultTextureUnit;

    @Override
    public void onPreLaunch() {
        Mixins.addConfiguration("intelfix.optifine.mixins.json");
    }
}
