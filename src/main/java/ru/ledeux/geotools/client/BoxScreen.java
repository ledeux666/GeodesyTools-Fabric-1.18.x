package ru.ledeux.geotools.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.ledeux.geotools.screen.BoxScreenHandler;

public class BoxScreen extends HandledScreen<BoxScreenHandler> {

    // Путь до текстуры gui. В этом примере мы использует текстуру раздатчика.
    private static final Identifier TEXTURE =
            new Identifier("minecraft", "textures/gui/container/dispenser.png");

    public BoxScreen(BoxScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    // Отрисовывает фон инвентаря.
    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        // width - ширина экрана, backgroundWidth - ширина фона.
        // height - высота экрана, backgroundHeight - высота фона.
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {

        super.init();
        // Центрирует текст заголовка.
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}
