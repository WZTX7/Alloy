package top.friendcraft.alloy.core.modules;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import top.friendcraft.alloy.common.block.*;
import top.friendcraft.alloy.common.item.crafting.MeltingRecipe;
import top.friendcraft.alloy.common.item.crafting.SunlightRecipe;
import top.friendcraft.alloy.core.RegisterModule;
import top.friendcraft.alloy.core.registry.RegistrySupplier;

public class BlockModule extends RegisterModule {

    // Melt Core
    public BlockWithItem<LeveledMeltFurnaceCore> base_core, steel_core;
    public RegistrySupplier<BlockEntityType<?>, BlockEntityType<LeveledMeltingEntity>> base_entity, steel_entity;
    public RegistrySupplier<MenuType<?>, MenuType<LeveledMeltingControlMenu>> base_menu, steel_menu;
    public RegistrySupplier<RecipeType<?>, RecipeType<MeltingRecipe>> recipe;
    public RegistrySupplier<RecipeSerializer<?>, RecipeSerializer<MeltingRecipe>> serializer;
    // Sunlight Collector
    public BlockWithItem<SunlightCollector> sunlight_collector;
    public RegistrySupplier<BlockEntityType<?>, BlockEntityType<SunlightCollectorEntity>> sunlight_collector_entity;
    public RegistrySupplier<MenuType<?>, MenuType<SunlightCollectorMenu>> sunlight_collector_menu;
    public RegistrySupplier<RecipeType<?>, RecipeType<SunlightRecipe>> sunlight_recipe;
    public RegistrySupplier<RecipeSerializer<?>, RecipeSerializer<SunlightRecipe>> sunlight_serializer;
    // Ladders
    public BlockWithItem<LadderScaffold> deepslate_ladder;

    public BlockModule() {
        super("blocks", true);
        base_core = registerBlockWithItem("base_melt_furnace_core",
                properties -> new LeveledMeltFurnaceCore(properties, 1),
                BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.BLAST_FURNACE),
                new Item.Properties()
        );
        base_entity = registerTile("base_melting", (blockPos, blockState) -> new LeveledMeltingEntity(blockPos, blockState, 1), base_core.block);
        base_menu = registerMenu("base_melting", (containerId, inventory) -> new LeveledMeltingControlMenu(containerId, inventory, 1), LeveledMeltingControlScreen::new);
        recipe = registerRecipeType("melting");
        serializer = registerSerializer("melting", MeltingRecipe.Serializer::new);
        sunlight_collector = registerBlockWithItem("sunlight_collector", SunlightCollector::new);
        sunlight_collector_entity = registerTile("sunlight_collector", SunlightCollectorEntity::new, sunlight_collector.block);
        sunlight_collector_menu = registerMenu("sunlight_collector", SunlightCollectorMenu::new, SunlightCollectorScreen::new);
        sunlight_recipe = registerRecipeType("sunlight_recipe");
        sunlight_serializer = registerSerializer("sunlight_recipe", SunlightRecipe.Serializer::new);
        deepslate_ladder = registerBlockWithItem("deepslate_ladder_block", LadderScaffold::new,
                BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.LADDER)
                        .sound(SoundType.DEEPSLATE_BRICKS)
                        .pushReaction(PushReaction.BLOCK)
                        .noCollission(),
                new Item.Properties()
        );
    }
}
