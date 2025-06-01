package top.friendcraft.alloy;

import com.mojang.logging.LogUtils;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import org.slf4j.Logger;

import java.util.function.Function;

public final class Alloy {
    // Mod Default Settings
    public static final String MOD_ID = "alloy";

    // Class Logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // Deferred Registers
    private static final DeferredRegister<Item> Items = DeferredRegister.create(MOD_ID, Registries.ITEM);
    private static final DeferredRegister<Block> Blocks = DeferredRegister.create(MOD_ID, Registries.BLOCK);
    private static final DeferredRegister<Potion> Potions = DeferredRegister.create(MOD_ID, Registries.POTION);
    private static final DeferredRegister<MobEffect> Effects = DeferredRegister.create(MOD_ID, Registries.MOB_EFFECT);
    private static final DeferredRegister<BlockEntityType<?>> Tiles = DeferredRegister.create(MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    private static final DeferredRegister<RecipeType<?>> Recipes = DeferredRegister.create(MOD_ID, Registries.RECIPE_TYPE);
    private static final DeferredRegister<RecipeSerializer<?>> Serializers = DeferredRegister.create(MOD_ID, Registries.RECIPE_SERIALIZER);
    private static final DeferredRegister<MenuType<?>> Menus = DeferredRegister.create(MOD_ID, Registries.MENU);
    private static final DeferredRegister<CreativeModeTab> Tabs = DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB);
    private static final DeferredRegister<EntityType<?>> Entities = DeferredRegister.create(MOD_ID, Registries.ENTITY_TYPE);
    private static final DeferredRegister<Fluid> Fluids = DeferredRegister.create(MOD_ID, Registries.FLUID);

    // Holders
    public static RegistrySupplier<Item> obsidianite_ingot, rose_gold_ingot, advanced_netherite_ingot, steel_ingot;

    public static void init() {
        obsidianite_ingot = registerSimpleItem("obsidianite_ingot");
        rose_gold_ingot = registerSimpleItem("rose_gold_ingot");
        advanced_netherite_ingot = registerSimpleItem("advanced_netherite_ingot");
        steel_ingot = registerSimpleItem("steel_ingot");
        register();
    }

    private static void register() {
        Items.register();
        Blocks.register();
        Potions.register();
        Effects.register();
        Tiles.register();
        Recipes.register();
        Serializers.register();
        Menus.register();
        Tabs.register();
        Entities.register();
        Fluids.register();
    }

    private static <I extends Item> RegistrySupplier<I> registerItem(String id, Function<Item.Properties, I> factory, Item.Properties properties) {
        return Items.register(id, ()->factory.apply(
                properties.setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, id)))
        ));
    }

    private static <I extends Item> RegistrySupplier<I> registerItem(String id, Function<Item.Properties, I> factory) {
        return registerItem(id, factory, new Item.Properties());
    }

    private static RegistrySupplier<Item> registerSimpleItem(String id, Item.Properties properties) {
        return registerItem(id, Item::new, properties);
    }

    private static RegistrySupplier<Item> registerSimpleItem(String id) {
        return registerItem(id, Item::new, new Item.Properties());
    }



}
