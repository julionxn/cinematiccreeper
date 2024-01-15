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

public class ScrollWidget extends ExtendedWidget {

    protected static final Identifier SCROLLBAR_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/scrollbar.png");
    protected final int x;
    protected final int y;
    protected final int itemsWidth;
    protected final int itemsHeight;
    protected final int itemsPerPage;
    protected final List<ScrollItem> scrollItems;
    protected final int size;
    protected final boolean isScroll;
    protected int showingFrom = 0;

    public ScrollWidget(ExtendedScreen screen, int x, int y, int itemsWidth, int itemsHeight, int itemsPerPage, List<ScrollItem> scrollItems) {
        super(screen);
        this.x = x;
        this.y = y;
        this.itemsWidth = itemsWidth;
        this.itemsHeight = itemsHeight;
        this.itemsPerPage = itemsPerPage;
        this.scrollItems = scrollItems;
        this.size = scrollItems.size();
        this.isScroll = size > itemsPerPage;
    }

    @Override
    public void init() {
        addItems();
        if (!isScroll) return;
        ButtonWidget upList = ButtonWidget.builder(Text.of("⮝"), button -> changePage(-1))
                .dimensions(x + itemsWidth, y, 20, 20).build();
        ButtonWidget downList = ButtonWidget.builder(Text.of("⮟"), button -> changePage(1))
                .dimensions(x + itemsWidth, y + ((itemsPerPage - 1) * itemsHeight), 20, 20).build();
        addDrawableChild(upList);
        addDrawableChild(downList);
    }

    protected void addItems() {
        int current = 0;
        for (int i = showingFrom; i - showingFrom < Math.min(itemsPerPage, size); i++) {
            ScrollItem scrollItem = scrollItems.get(i);
            ButtonWidget option = ButtonWidget.builder(
                            Text.of(scrollItem.text()),
                            button -> scrollItem.runnable().accept(button))
                    .dimensions(x, y + (current++ * itemsHeight), itemsWidth, itemsHeight).build();
            addDrawableChild(option);
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
        context.drawTexture(SCROLLBAR_TEXTURE, x + itemsWidth, actualY,
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
        ScrollWidget that = (ScrollWidget) o;
        return itemsWidth == that.itemsWidth && itemsHeight == that.itemsHeight && Objects.equals(scrollItems, that.scrollItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, itemsPerPage, itemsWidth, itemsHeight, scrollItems);
    }

    public record ScrollItem(String text, Consumer<ButtonWidget> runnable) {
    }

    public static Builder builder(ExtendedScreen extendedScreen, List<ScrollItem> items){
        return new Builder(extendedScreen, items);
    }

    public static class Builder {

        private final ExtendedScreen extendedScreen;
        private final List<ScrollItem> itemsSupplier;
        private int x;
        private int y;
        private int itemsWidth = 150;
        private int itemsHeight = 20;
        private int itemsPerPage = 5;

        public Builder(ExtendedScreen extendedScreen, List<ScrollItem> itemsSupplier) {
            this.extendedScreen = extendedScreen;
            this.itemsSupplier = itemsSupplier;
        }

        public Builder pos(int x, int y){
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder itemsDimensions(int width, int height){
            this.itemsHeight = height;
            this.itemsWidth = width;
            return this;
        }

        public Builder itemsPerPage(int amount){
            this.itemsPerPage = amount;
            return this;
        }

        public ScrollWidget build(){
            return new ScrollWidget(extendedScreen, x, y, itemsWidth, itemsHeight, itemsPerPage, itemsSupplier);
        }

    }

}
