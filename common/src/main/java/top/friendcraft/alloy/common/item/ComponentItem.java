package top.friendcraft.alloy.common.item;

import net.minecraft.world.item.Item;

public class ComponentItem extends Item {

    private final String identifier;
    public ComponentItem(Properties properties) {
        super(properties);
        this.identifier = this.getDescriptionId().split("\\.")[1];
    }

    public static class PropertiesBuilder {

    }
}
