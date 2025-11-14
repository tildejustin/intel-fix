package dev.tildejustin.intelfix.mixin;

import dev.tildejustin.intelfix.IntelFix;
import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.invoke.*;

@SuppressWarnings("StatementWithEmptyBody")
@Pseudo
// ornithe: BufferBuilder
@Mixin(value = Tessellator.class, targets = {"net.minecraft.unmapped.C_5786166", "net.minecraft.unmapped.C_2334550"})
public class TessellatorMixin {
    @Dynamic
    @Inject(
            method = {
                    "end",
                    "m_7459300()I" // ornithe: end
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glTexCoordPointer(IILjava/nio/FloatBuffer;)V",
                    ordinal = 0,
                    remap = false
            )
    )
    private void setClientActiveTextureToDefault(CallbackInfoReturnable<Integer> cir) throws Throwable {
        IntelFix.setClientActiveTexture.invoke(IntelFix.defaultTextureUnit);
    }

    static {
        // @formatter:off
        // legacy fabric v1: GLX, textureUnit, gl13ClientActiveTexture
        if (setWithMappings("class_629", "field_2300", "method_1764")) ;
        // ornithe v1: GLX, GL_TEXTURE0, clientActiveTexture
        else if (setWithMappings("unmapped.C_5585855", "f_0448360", "m_5489718")) ;
        // ornithe v2
        else if (setWithMappings("unmapped.C_7567310", "f_4681669", "m_2178803")) ;
        else throw new RuntimeException("unknown mappings");
        // @formatter:on
    }

    @Unique
    private static boolean setWithMappings(String glxName, String defaultTextureUnitName, String setClientActiveTextureName) {
        try {
            System.out.println("trying");
            Class<?> glx = Class.forName("net.minecraft." + glxName);
            System.out.println("found class");
            IntelFix.defaultTextureUnit = glx.getField(defaultTextureUnitName).getInt(null);
            System.out.println("found field");
            IntelFix.setClientActiveTexture = MethodHandles.publicLookup().findStatic(glx, setClientActiveTextureName, MethodType.methodType(void.class, int.class));
            System.out.println("found method");
            return true;
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
            return false;
        }
    }
}
