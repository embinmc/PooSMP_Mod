package embin.poosmp.client.screen.upgrade;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

public class UpgradesScreen extends OptionsSubScreen {
    private UpgradesListWidget upgradesList;

    public UpgradesScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.literal("Upgrades"));
    }

    @Override
    protected void init() {
        this.addTitle();
        this.addContents();
        this.addFooter();
        this.layout.visitWidgets(element -> {
            if (element instanceof StringWidget || element instanceof Button) {
                this.addRenderableWidget(element);
            }
        });
        this.addRenderableWidget(this.upgradesList);
    }

    @Override
    protected void addContents() {
        this.upgradesList = this.layout.addToContents(new UpgradesListWidget(this.minecraft, this));
        this.upgradesList.update();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void addOptions() {}

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.upgradesList.update();
        this.layout.arrangeElements();
    }
}
