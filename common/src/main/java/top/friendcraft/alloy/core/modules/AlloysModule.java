package top.friendcraft.alloy.core.modules;

import net.minecraft.world.item.Item;
import top.friendcraft.alloy.core.RegisterModule;
import top.friendcraft.alloy.core.registry.RegistrySupplier;

public class AlloysModule extends RegisterModule {
    public RegistrySupplier<Item, Item> obsidianite_ingot, rose_gold_ingot, enhanced_netherite_ingot, steel_ingot;
    public RegistrySupplier<Item, Item> sunlight_coal, active_sunlight_coal, clean_coal, anthracite;
    public AlloysModule() {
        super("alloys", true);
        obsidianite_ingot = registerSimpleItem("obsidianite_ingot");
        rose_gold_ingot = registerSimpleItem("rose_gold_ingot");
        enhanced_netherite_ingot = registerSimpleItem("enhanced_netherite_ingot");
        steel_ingot = registerSimpleItem("steel_ingot");
        sunlight_coal = registerSimpleItem("sunlight_coal");
        active_sunlight_coal = registerSimpleItem("active_sunlight_coal");
        clean_coal = registerSimpleItem("clean_coal");
        anthracite = registerSimpleItem("anthracite");
        try {
            registerTab(obsidianite_ingot);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
