package top.friendcraft.alloy.core.registry;

import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import top.friendcraft.alloy.Alloy;
import top.friendcraft.alloy.common.item.ModularItemComponent;
import top.friendcraft.alloy.core.services.FluidService;
import top.friendcraft.alloy.core.services.RegisterService;
import top.friendcraft.alloy.core.services.ServicesManager;
import top.friendcraft.alloy.core.registry.DeferredRegister;

public class RegistryHolder {
    public static class Registries {
        public static final ResourceKey<Registry<ModularItemComponent>> MODULAR_ITEM_COMPONENT = Alloy.createRegistryKey("modular_item_component");
    }
    public static class BuiltinRegistries {
        public static final Registry<ModularItemComponent> MODULAR_ITEM_COMPONENT = ServicesManager.REGISTER_SERVICE.registerRegistryByPlatform(
                new RegisterService.RegistryProperties<>(Registries.MODULAR_ITEM_COMPONENT)
                        .maxId(1024)
                        .defaultKey(Alloy.getIdentifier("empty"))
        );

    }

    public static final DeferredRegister<Item> Items = DeferredRegister.create(Alloy.MOD_ID, net.minecraft.core.registries.Registries.ITEM);
    public static final DeferredRegister<Block> Blocks = DeferredRegister.create(Alloy.MOD_ID, net.minecraft.core.registries.Registries.BLOCK);
    public static final DeferredRegister<Potion> Potions = DeferredRegister.create(Alloy.MOD_ID, net.minecraft.core.registries.Registries.POTION);
    public static final DeferredRegister<MobEffect> Effects = DeferredRegister.create(Alloy.MOD_ID, net.minecraft.core.registries.Registries.MOB_EFFECT);
    public static final DeferredRegister<BlockEntityType<?>> Tiles = DeferredRegister.create(Alloy.MOD_ID, net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<RecipeType<?>> Recipes = DeferredRegister.create(Alloy.MOD_ID, net.minecraft.core.registries.Registries.RECIPE_TYPE);
    public static final DeferredRegister<RecipeSerializer<?>> Serializers = DeferredRegister.create(Alloy.MOD_ID, net.minecraft.core.registries.Registries.RECIPE_SERIALIZER);
    public static final DeferredRegister<MenuType<?>> Menus = DeferredRegister.create(Alloy.MOD_ID, net.minecraft.core.registries.Registries.MENU);
    public static final DeferredRegister<CreativeModeTab> Tabs = DeferredRegister.create(Alloy.MOD_ID, net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB);
    public static final DeferredRegister<EntityType<?>> Entities = DeferredRegister.create(Alloy.MOD_ID, net.minecraft.core.registries.Registries.ENTITY_TYPE);
    public static final DeferredRegister<Fluid> Fluids = DeferredRegister.create(Alloy.MOD_ID, net.minecraft.core.registries.Registries.FLUID);
    public static final DeferredRegister<ModularItemComponent> ModularComponents = DeferredRegister.create(Alloy.MOD_ID, Registries.MODULAR_ITEM_COMPONENT);

    public static void register() {
        Fluids.register();
        Blocks.register();
        Items.register();
        ModularComponents.register();
        ServicesManager.FLUID_SERVICE.callBucketRegister();
        Potions.register();
        Effects.register();
        Tiles.register();
        Recipes.register();
        Serializers.register();
        Menus.register();
        Tabs.register();
        Entities.register();
    }

    @FunctionalInterface
    public interface BlockEntitySupplier<T extends BlockEntity> {
        T create(BlockPos blockPos, BlockState blockState);
    }
}
