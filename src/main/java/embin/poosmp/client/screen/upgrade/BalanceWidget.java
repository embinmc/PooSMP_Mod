package embin.poosmp.client.screen.upgrade;

import embin.poosmp.world.PooSMPSavedData;

import java.text.NumberFormat;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import org.jspecify.annotations.NonNull;

public class BalanceWidget extends AbstractWidget {
    public BalanceWidget(int x, int y) {
        super(x, y, 4, 4, Component.empty());
    }

    @Override
    protected void renderWidget(@NonNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        Minecraft minecraft = Minecraft.getInstance();
        PooSMPSavedData savedData = PooSMPSavedData.Client.INSTANCE;
        if (minecraft.player != null) {
            String balance = "$" + NumberFormat.getNumberInstance(Locale.US).format(savedData.getBalance(minecraft.player));
            context.drawString(minecraft.font, balance, this.getX(), this.getY(), CommonColors.WHITE);
        }
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput builder) {
    }
}
