/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lambdaurora.spruceui.accessor.DrawableHelperAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Represents an object which can show a tooltip.
 *
 * @author LambdAurora
 * @version 1.4.1
 * @since 1.0.0
 */
public interface Tooltipable
{
    /**
     * Gets the tooltip.
     *
     * @return The tooltip to show.
     */
    @NotNull Optional<Text> getTooltip();

    /**
     * Sets the tooltip.
     *
     * @param tooltip The tooltip to show.
     */
    void setTooltip(@Nullable Text tooltip);

    /**
     * Renders a tooltip.
     * <p>
     * X and Y tooltips are often mouse coordinates.
     *
     * @param client The client instance.
     * @param helper The draw helper.
     * @param text   The tooltip text to render.
     * @param x      The X coordinate of the tooltip.
     * @param y      The Y coordinate of the tooltip.
     */
    static void render(@NotNull MinecraftClient client, @NotNull DrawableHelper helper, List<String> text, int x, int y)
    {
        if (!text.isEmpty()) {
            DrawableHelperAccessor helperAccessor = (DrawableHelperAccessor) helper;

            RenderSystem.disableRescaleNormal();
            RenderSystem.disableDepthTest();
            int i = 0;

            for (String string : text) {
                int j = client.textRenderer.getStringWidth(string);
                if (j > i) {
                    i = j;
                }
            }

            int textX = x + 12;
            int textY = y - 12;
            int n = 8;
            if (text.size() > 1) {
                n += 2 + (text.size() - 1) * 10;
            }

            if (textX + i > client.getWindow().getScaledWidth()) {
                textX -= 28 + i;
            }

            if (textY + n + 6 > client.getWindow().getScaledHeight()) {
                textY = client.getWindow().getScaledHeight() - n - 6;
            }

            helper.setBlitOffset(300);
            client.getItemRenderer().zOffset = 300.0F;
            helperAccessor.spruceui_fillGradient(textX - 3, textY - 4, textX + i + 3, textY - 3, -267386864, -267386864);
            helperAccessor.spruceui_fillGradient(textX - 3, textY + n + 3, textX + i + 3, textY + n + 4, -267386864, -267386864);
            helperAccessor.spruceui_fillGradient(textX - 3, textY - 3, textX + i + 3, textY + n + 3, -267386864, -267386864);
            helperAccessor.spruceui_fillGradient(textX - 4, textY - 3, textX - 3, textY + n + 3, -267386864, -267386864);
            helperAccessor.spruceui_fillGradient(textX + i + 3, textY - 3, textX + i + 4, textY + n + 3, -267386864, -267386864);
            helperAccessor.spruceui_fillGradient(textX - 3, textY - 3 + 1, textX - 3 + 1, textY + n + 3 - 1, 1347420415, 1344798847);
            helperAccessor.spruceui_fillGradient(textX + i + 2, textY - 3 + 1, textX + i + 3, textY + n + 3 - 1, 1347420415, 1344798847);
            helperAccessor.spruceui_fillGradient(textX - 3, textY - 3, textX + i + 3, textY - 3 + 1, 1347420415, 1347420415);
            helperAccessor.spruceui_fillGradient(textX - 3, textY + n + 2, textX + i + 3, textY + n + 3, 1344798847, 1344798847);
            MatrixStack matrices = new MatrixStack();
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            matrices.translate(0.0D, 0.0D, client.getItemRenderer().zOffset);
            Matrix4f matrix4f = matrices.peek().getModel();

            for (int lineIndex = 0; lineIndex < text.size(); ++lineIndex) {
                String line = text.get(lineIndex);
                if (line != null) {
                    client.textRenderer.draw(line, (float) textX, (float) textY, -1, true, matrix4f, immediate, false, 0, 15728880);
                }

                if (lineIndex == 0) {
                    textY += 2;
                }

                textY += 10;
            }

            immediate.draw();
            helper.setBlitOffset(0);
            client.getItemRenderer().zOffset = 0.0F;
            RenderSystem.enableDepthTest();
            RenderSystem.enableRescaleNormal();
        }
    }
}
