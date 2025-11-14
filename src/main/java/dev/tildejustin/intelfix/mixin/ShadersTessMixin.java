package dev.tildejustin.intelfix.mixin;

import dev.tildejustin.intelfix.IntelFix;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "shadersmod.client.ShadersTess", remap = false)
public class ShadersTessMixin {
    @Dynamic
    @Inject(
            method = {
                    "draw",
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glTexCoordPointer(IILjava/nio/FloatBuffer;)V",
                    ordinal = 0
            )
    )
    private static void setClientActiveTextureToDefault(CallbackInfoReturnable<Integer> cir) throws Throwable {
        IntelFix.setClientActiveTexture.invoke(IntelFix.defaultTextureUnit);
    }
}
