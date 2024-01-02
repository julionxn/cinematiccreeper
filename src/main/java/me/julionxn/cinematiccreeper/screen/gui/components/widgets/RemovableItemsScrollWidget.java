package me.julionxn.cinematiccreeper.screen.gui.components.widgets;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RemovableItemsScrollWidget extends ExtendedWidget {

    protected static final Identifier SCROLLBAR_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/scrollbar.png");
    protected final int x;
    protected final int y;
    protected final int itemsWidth;
    protected final int itemsHeight;
    protected final int itemsPerPage;
    protected final Supplier<List<RemovableScrollItem>> itemsSupplier;
    protected final boolean isScroll;
    protected List<RemovableScrollItem> scrollItems;
    protected int size;
    protected int showingFrom = 0;

    public RemovableItemsScrollWidget(ExtendedScreen screen, int x, int y, int itemsWidth, int itemsHeight, int itemsPerPage, Supplier<List<RemovableScrollItem>> supplier) {
        super(screen);
        this.x = x;
        this.y = y;
        this.itemsWidth = itemsWidth;
        this.itemsHeight = itemsHeight;
        this.itemsPerPage = itemsPerPage;
        this.itemsSupplier = supplier;
        retreiveItems();
        this.isScroll = size > itemsPerPage;
    }

    private void retreiveItems() {
        scrollItems = itemsSupplier.get();
        size = scrollItems.size();
    }

    @Override
    public void init() {
        addItems();
        if (!isScroll) return;
        ButtonWidget upList = ButtonWidget.builder(Text.of("⮝"), button -> changePage(-1))
                .dimensions(x + 20 + itemsWidth, y, 20, 20).build();
        ButtonWidget downList = ButtonWidget.builder(Text.of("⮟"), button -> changePage(1))
                .dimensions(x + 20 + itemsWidth, y + ((itemsPerPage - 1) * itemsHeight), 20, 20).build();
        addDrawableChild(upList);
        addDrawableChild(downList);
    }

    protected void addItems() {
        int current = 0;
        for (int i = showingFrom; i - showingFrom < Math.min(itemsPerPage, size); i++) {
            RemovableScrollItem scrollItem = scrollItems.get(i);
            ButtonWidget option = ButtonWidget.builder(
                            Text.of(scrollItem.text()),
                            scrollItem.runnable::accept
                    )
                    .dimensions(x, y + (current * itemsHeight), itemsWidth, itemsHeight).build();
            addDrawableChild(option);
            ButtonWidget remove = ButtonWidget.builder(
                    Text.of("R"),
                    button -> {
                        scrollItem.onRemove.accept(button);
                        retreiveItems();
                        clear();
                    }
            ).dimensions(x + itemsWidth, y + (current++ * itemsHeight), 20, 20).build();
            addDrawableChild(remove);
        }
    }

    protected void changePage(int in) {
        if (in == 0) return;
        if (showingFrom + in < 0) return;
        if (showingFrom + itemsPerPage + in > scrollItems.size()) return;
        showingFrom += in;
        clear();
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!isScroll) return;
        renderBar(context);
    }

    protected void renderBar(DrawContext context) {
        int totalHeight = 20 * (itemsPerPage - 2);
        int barHeight = (int) ((itemsPerPage / (float) scrollItems.size()) * totalHeight);
        totalHeight -= barHeight;
        float dif = ((float) showingFrom / (scrollItems.size() - itemsPerPage));
        int actualY = (int) (y + 20 + (totalHeight * dif));
        context.drawTexture(SCROLLBAR_TEXTURE, x + 20 + itemsWidth, actualY,
                0, 0, 0, 20, barHeight, 20, barHeight);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        handleMouse(mouseX, mouseY);
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        handleMouse(mouseX, mouseY);
        super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!isScroll) return;
        if (mouseX > x && mouseX < x + itemsWidth && mouseY > y && mouseY < y + (itemsPerPage * itemsHeight)) {
            changePage((int) -verticalAmount);
        }
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    private void handleMouse(double mouseX, double mouseY) {
        if (!isScroll) return;
        int scrollY = y + 20;
        if (mouseX > x + itemsWidth && mouseX < x + itemsWidth + 20 && mouseY > scrollY - 5 && mouseY < scrollY + ((scrollItems.size() - 2) * itemsHeight) + 5) {
            int totalHeight = 20 * (itemsPerPage - 2) - 10;
            float dif = (float) (mouseY - scrollY) / totalHeight;
            showingFrom = Math.max(0, Math.min((int) (scrollItems.size() * dif), scrollItems.size() - itemsPerPage));
            clear();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemovableItemsScrollWidget that = (RemovableItemsScrollWidget) o;
        return itemsWidth == that.itemsWidth && itemsHeight == that.itemsHeight && Objects.equals(scrollItems, that.scrollItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, itemsPerPage, itemsWidth, itemsHeight, scrollItems);
    }

    public record RemovableScrollItem(String text, Consumer<ButtonWidget> runnable, Consumer<ButtonWidget> onRemove) {
    }
}
