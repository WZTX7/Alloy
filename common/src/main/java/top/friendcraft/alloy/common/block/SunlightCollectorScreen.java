package top.friendcraft.alloy.common.block;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import top.friendcraft.alloy.Alloy;

public class SunlightCollectorScreen extends AbstractContainerScreen<SunlightCollectorMenu> {
    private final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(Alloy.MOD_ID, "textures/gui/container/sunlight_collector.png");
    private final ResourceLocation burnProgressSprite = ResourceLocation.fromNamespaceAndPath(Alloy.MOD_ID, "container/sunlight_collector/burn_progress");
    private final ResourceLocation collectingProgressSprite = ResourceLocation.fromNamespaceAndPath(Alloy.MOD_ID, "container/sunlight_collector/collect_progress");
    public SunlightCollectorScreen(SunlightCollectorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(RenderType::guiTextured, this.texture, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        int l = Mth.ceil(this.menu.getBurnProgress() * 24.0F);
        guiGraphics.blitSprite(RenderType::guiTextured, this.burnProgressSprite, 24, 16, 0, 0, i + 82, j + 34, l, 16);
        int k = Mth.ceil(this.menu.getCollectingProgress() * 54.0F);
        guiGraphics.blitSprite(RenderType::guiTextured, this.collectingProgressSprite, 18, 54, 0, 54-k, i + 55, j + 16 + 54 - k, 18, k);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        super.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
