package top.friendcraft.alloy.common.block;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import top.friendcraft.alloy.Alloy;

public class AbstractMeltingControlScreen<T extends AbstractMeltingControlMenu> extends AbstractContainerScreen<T> {
    private final ResourceLocation texture;
    private final ResourceLocation litProgressSprite = ResourceLocation.fromNamespaceAndPath(Alloy.MOD_ID, "container/melting_core/lit_progress");
    private final ResourceLocation burnProgressSprite = ResourceLocation.fromNamespaceAndPath(Alloy.MOD_ID, "container/melting_core/burn_progress");
    public AbstractMeltingControlScreen(T menu, Inventory playerInventory, Component title, ResourceLocation texture) {
        super(menu, playerInventory, title);
        this.texture = texture;
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
        boolean k;
        int l;
        if (this.menu.isLit()) {
            k = true;
            l = Mth.ceil(this.menu.getLitProgress() * 13.0F) + 1;
            guiGraphics.blitSprite(RenderType::guiTextured, this.litProgressSprite, 14, 14, 0, 14 - l, i + 56-9, j + 36 + 14 - l, 14, l);
        }
        k = true;
        l = Mth.ceil(this.menu.getBurnProgress() * 24.0F);
        guiGraphics.blitSprite(RenderType::guiTextured, this.burnProgressSprite, 24, 16, 0, 0, i + 88, j + 34, l, 16);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        super.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
