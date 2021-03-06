/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of SpruceUI.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.spruceui.hud;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import org.aperlambda.lambdacommon.Identifier;
import org.aperlambda.lambdacommon.utils.Identifiable;
import org.aperlambda.lambdacommon.utils.function.Predicates;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a HUD.
 *
 * @author LambdAurora
 * @version 1.5.0
 * @since 1.2.0
 */
public abstract class Hud extends DrawableHelper implements Identifiable
{
    protected final Identifier         identifier;
    protected final List<HudComponent> components = new ArrayList<>();
    private         boolean            enabled    = true;
    protected       boolean            visible    = true;

    public Hud(@NotNull Identifier identifier)
    {
        this.identifier = identifier;
    }

    public Hud(@NotNull net.minecraft.util.Identifier identifier)
    {
        this(new Identifier(identifier.toString()));
    }

    /**
     * Returns the translation key of this HUD.
     *
     * @return The translation key.
     */
    public String getTranslationKey()
    {
        return this.identifier.getNamespace() + ".hud." + this.identifier.getName();
    }

    /**
     * Returns whether the HUD is enabled or not.
     *
     * @return True if the HUD is enabled, else false.
     */
    public boolean isEnabled()
    {
        return this.enabled;
    }

    /**
     * Sets whether the HUD is enabled or not.
     *
     * @param enabled True if the HUD is enabled, else false.
     */
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
        if (enabled) {
            MinecraftClient client = MinecraftClient.getInstance();
            this.init(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
        }
    }

    /**
     * Returns whether the HUD is visible or not.
     *
     * @return True if the HUD is visible, else false.
     */
    public boolean isVisible()
    {
        return this.visible;
    }

    /**
     * Sets whether the HUD is visible or not.
     *
     * @param visible True if the HUD is visible, else false.
     */
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public void init(@NotNull MinecraftClient client, int screenWidth, int screenHeight)
    {
        this.components.clear();
    }

    /**
     * Renders the HUD if enabled.
     *
     * @param tickDelta Progress for linearly interpolating between the previous and current game state.
     * @see #isEnabled()
     */
    public void render(MatrixStack matrices, float tickDelta)
    {
        this.components.stream().filter(HudComponent::isEnabled).forEach(component -> component.render(matrices, tickDelta));
    }

    /**
     * Updates the HUD each tick if enabled and has tick updates.
     *
     * @see #isEnabled()
     * @see #hasTicks()
     */
    public void tick()
    {
        this.components.stream().filter(Predicates.and(HudComponent::hasTicks, HudComponent::isEnabled)).forEach(HudComponent::tick);
    }

    /**
     * Returns whether this HUD has tick updates.
     *
     * @return True if this HUD has tick updates, else false.
     * @see #tick()
     */
    public boolean hasTicks()
    {
        return false;
    }

    /**
     * Returns a list of this HUD's components.
     *
     * @return The HUD's components.
     */
    public @NotNull List<HudComponent> getComponents()
    {
        return ImmutableList.copyOf(this.components);
    }

    @Override
    public @NotNull Identifier getIdentifier()
    {
        return this.identifier;
    }
}
