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
        return new DataIterator(width, height, xPos, yPos, base);
    }

    private static class DataIterator implements Iterator<Function<Container, Slot>> {

        int width, height, xPos, yPos, base;
        int i = 0, j = 0;

        public DataIterator(int width, int height, int xPos, int yPos, int base) {
            this.width = width;
            this.height = height;
            this.xPos = xPos;
            this.yPos = yPos;
            this.base = base;
        }

        @Override
        public boolean hasNext() {
            return j < height;
        }

        @Override
        public Function<Container, Slot> next() {
            int id = j * width + i + this.base;
            int x = xPos + i * 18;
            int y = yPos + j * 18;
            Function<Container, Slot> factory = container -> new Slot(container, id, x, y);
            LOGGER.info(String.valueOf(id));
            i++;
            if (i >= width) {
                i = 0;
                j++;
            }
            LOGGER.info("i: {}, j: {}", i, j);
            return factory;
        }
    }
}
