package top.friendcraft.alloy;

import com.mojang.logging.LogUtils;
import dev.architectury.hooks.item.ItemStackHooks;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.slf4j.Logger;
import top.friendcraft.alloy.common.block.*;
import top.friendcraft.alloy.common.item.ArmorSet;
import top.friendcraft.alloy.common.item.InputData;
import top.friendcraft.alloy.common.item.crafting.MeltingRecipe;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

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
    // Creative Mode Tabs
    public static RegistrySupplier<CreativeModeTab> alloys, blocks, armors;

    // Basic Items
    public static RegistrySupplier<Item> obsidianite_ingot, rose_gold_ingot, enhanced_netherite_ingot, steel_ingot;
    public static RegistrySupplier<Item> sunlight_coal, active_sunlight_coal, clean_coal, anthracite;

    //Armors
    public static ArmorSet obsidanites, rose_golds, enhanced_netherites, steels;

    // Blocks
    // Melt Core
    public static BlockWithItem<LeveledMeltFurnaceCore> base_core, steel_core;
    public static RegistrySupplier<BlockEntityType<LeveledMeltingEntity>> base_entity, steel_entity;
    public static RegistrySupplier<MenuType<LeveledMeltingControlMenu>> base_menu, steel_menu;
    public static RegistrySupplier<RecipeType<MeltingRecipe>> recipe;
    public static RegistrySupplier<RecipeSerializer<MeltingRecipe>> serializer;
    // Sunlight Collector
    public static BlockWithItem<SunlightCollector> sunlight_collector;
    public static RegistrySupplier<BlockEntityType<SunlightCollectorEntity>> sunlight_collector_entity;
    public static RegistrySupplier<MenuType<SunlightCollectorMenu>> sunlight_collector_menu;


    // Melting Settings
    public static List<InputData[]> inputData = new ArrayList<>();


    public static void init() {
        log("Input Data");
        inputData.addAll(List.of(new InputData[0], new InputData[]{
                new InputData(4, 1, 20, 16),
                new InputData(1, 1, 47, 53, 32)
        }));
        log("Items");
        obsidianite_ingot = registerSimpleItem("obsidianite_ingot");
        rose_gold_ingot = registerSimpleItem("rose_gold_ingot");
        enhanced_netherite_ingot = registerSimpleItem("advanced_netherite_ingot");
        steel_ingot = registerSimpleItem("steel_ingot");
        sunlight_coal = registerSimpleItem("sunlight_coal");
        active_sunlight_coal = registerSimpleItem("active_sunlight_coal");
        obsidanites = registerArmors("obsidianite", ArmorSet.OBSIDIANITE);
        rose_golds = registerArmors("rose_gold", ArmorSet.ROSE_GOLD);
        enhanced_netherites = registerArmors("enhanced_netherite", ArmorSet.ENHANCED_NETHERITE);
        log("Creative Mode Tabs");
        alloys = registerTab("alloys", obsidianite_ingot);
        armors = registerTab("alloy_armors", obsidanites.chest());
        log("Melting Blocks");
        base_core = registerBlockWithItem("base_melt_furnace_core", properties -> new LeveledMeltFurnaceCore(properties, 1));
        base_entity = registerTile("base_melting", (blockPos, blockState) -> new LeveledMeltingEntity(blockPos, blockState, 1), base_core.block);
        base_menu = registerMenu("base_melting", (containerId, inventory) -> new LeveledMeltingControlMenu(containerId, inventory, 1), LeveledMeltingControlScreen::new);
        recipe = registerRecipeType("melting");
        serializer = registerSerializer("melting", MeltingRecipe.Serializer::new);
        sunlight_collector = registerBlockWithItem("sunlight_collector", SunlightCollector::new);
        sunlight_collector_entity = registerTile("sunlight_collector", SunlightCollectorEntity::new, sunlight_collector.block);
        sunlight_collector_menu = registerMenu("sunlight_collector", SunlightCollectorMenu::new, SunlightCollectorScreen::new);
        register();
    }

    private static void register() {
        Blocks.register();
        Items.register();
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

    public static void registerFuels() {
        FuelRegistry.register(2400, sunlight_coal.value());
        FuelRegistry.register(4800, active_sunlight_coal.value());
    }

    private static void log(String message) {
        LOGGER.info("Register "+message+";");
    }

    private static RegistrySupplier<CreativeModeTab> registerTab(String id, Holder<? extends Item> icon) {
        return Tabs.register(id, () -> CreativeTabRegistry.create(Component.translatable("itemGroup."+id), () -> new ItemStack(icon.value(), 1)));
    }

    private static <I extends Item> RegistrySupplier<I> registerItem(String id, Function<Item.Properties, I> factory, Item.Properties properties) {
        return Items.register(id, () -> factory.apply(
                properties.setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, id)))
        ));
    }

    private static <I extends Item> RegistrySupplier<I> registerItem(String id, Function<Item.Properties, I> factory) {
        return registerItem(id, factory, new Item.Properties());
    }

    private static RegistrySupplier<Item> registerSimpleItem(String id, Item.Properties properties) {
        return registerItem(id, property -> new Item(property.arch$tab(alloys)), properties);
    }

    private static RegistrySupplier<Item> registerSimpleItem(String id) {
        return registerItem(id, property -> new Item(property.arch$tab(alloys)), new Item.Properties());
    }

    private static ArmorSet registerArmors(String id, ArmorMaterial armorMaterial) {
        RegistrySupplier<ArmorItem> head = registerItem(id+"_helmet", properties -> new ArmorItem(armorMaterial, ArmorType.HELMET, properties.arch$tab(armors)));
        RegistrySupplier<ArmorItem> chest = registerItem(id+"_chestplate", properties -> new ArmorItem(armorMaterial, ArmorType.CHESTPLATE, properties.arch$tab(armors)));
        RegistrySupplier<ArmorItem> leg = registerItem(id+"_leggings", properties -> new ArmorItem(armorMaterial, ArmorType.LEGGINGS, properties.arch$tab(armors)));
        RegistrySupplier<ArmorItem> feet = registerItem(id+"_boots", properties -> new ArmorItem(armorMaterial, ArmorType.BOOTS, properties.arch$tab(armors)));
        return new ArmorSet(head, chest, leg, feet);
    }

    private static <B extends Block> RegistrySupplier<B> registerBlock(String id, Function<BlockBehaviour.Properties, B> factory, BlockBehaviour.Properties properties) {
        return Blocks.register(id, () -> factory.apply(
                properties.setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Alloy.MOD_ID, id)))
        ));
    }

    private static <B extends Block> BlockWithItem<B> registerBlockWithItem(String id, Function<BlockBehaviour.Properties, B> factory, BlockBehaviour.Properties properties, Item.Properties itemProperties) {
        RegistrySupplier<B> x = registerBlock(id, factory, properties);
        return new BlockWithItem<>(x, registerItem(id, p -> new BlockItem(x.get(), p), itemProperties));
    }

    private static <B extends Block> BlockWithItem<B> registerBlockWithItem(String id, Function<BlockBehaviour.Properties, B> factory) {
        return registerBlockWithItem(id, factory, BlockBehaviour.Properties.of(), new Item.Properties());
    }

    @SafeVarargs
    private static <T extends BlockEntity> RegistrySupplier<BlockEntityType<T>> registerTile(String id, Alloy.BlockEntitySupplier<T> factory, Supplier<? extends Block>... validBlocks) {
        return Tiles.register(id, () -> new BlockEntityType<>(factory::create, Set.of(Arrays.stream(validBlocks).map(Supplier::get).toArray(Block[]::new))));
    }

    private static <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> RegistrySupplier<MenuType<T>> registerMenu(String id, MenuType.MenuSupplier<T> factory, MenuRegistry.ScreenFactory<T, S> screenFactory) {
        MenuType<T> type = MenuRegistry.ofExtended((a, b, c) -> factory.create(a, b));
        MenuRegistry.registerScreenFactory(type, screenFactory);
        return Menus.register(id, () -> type);
    }

    private static <T extends Recipe<?>> RegistrySupplier<RecipeType<T>> registerRecipeType(String id) {
        return Recipes.register(id, () -> new RecipeType<T>() {
            @Override
            public String toString() {
                return ResourceLocation.fromNamespaceAndPath(Alloy.MOD_ID, id).toString();
            }
        });
    }

    private static <T extends Recipe<?>> RegistrySupplier<RecipeSerializer<T>> registerSerializer(String id, Supplier<? extends RecipeSerializer<T>> factory) {
        return Serializers.register(id, factory);
    }

    @FunctionalInterface
    private interface BlockEntitySupplier<T extends BlockEntity> {
        T create(BlockPos blockPos, BlockState blockState);
    }

}
