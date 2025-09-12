package top.friendcraft.alloy.common.item;

import com.mojang.logging.LogUtils;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.function.Function;

public record InputData(int width, int height, int xPos, int yPos,
                        int base) implements Iterable<Function<Container, Slot>> {
    private static final Logger LOGGER = LogUtils.getLogger();

    public InputData(int width, int height, int xPos, int yPos) {
        this(width, height, xPos, yPos, 0);
    }

    public int size() {
        return width * height;
    }

    @NotNull
    @Override
    public Iterator<Function<Container, Slot>> iterator() {
        return new DataIterator();
    }

    private class DataIterator implements Iterator<Function<Container, Slot>> {

        int i = 0, j = 0;

        @Override
        public boolean hasNext() {
            return this.j < InputData.this.height;
        }

        @Override
        public Function<Container, Slot> next() {
            int id = j * InputData.this.width + i + InputData.this.base;
            int x = InputData.this.xPos + i * 18;
            int y = InputData.this.yPos + j * 18;
            Function<Container, Slot> factory = container -> new Slot(container, id, x, y);
            LOGGER.info(String.valueOf(id));
            this.i++;
            if (this.i >= InputData.this.width) {
                this.i = 0;
                this.j++;
            }
            LOGGER.info("i: {}, j: {}", this.i, this.j);
            return factory;
        }
    }
}
