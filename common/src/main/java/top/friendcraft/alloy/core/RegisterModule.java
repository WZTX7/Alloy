package top.friendcraft.alloy.core;

import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import top.friendcraft.alloy.Alloy;
import top.friendcraft.alloy.common.block.BlockWithItem;
import top.friendcraft.alloy.common.item.ArmorSet;
import top.friendcraft.alloy.common.item.ModularItemComponent;
import top.friendcraft.alloy.core.registry.DeferredSupplier;
import top.friendcraft.alloy.core.registry.RegistryHolder;
import top.friendcraft.alloy.core.services.ServicesManager;
import top.friendcraft.alloy.core.registry.RegistrySupplier;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegisterModule {
    protected RegistrySupplier<CreativeModeTab, CreativeModeTab> tab;
    private final String id;
    private final boolean hasTab;

    public RegisterModule(String id, boolean hasCreativeModeTab) {
        this.id = id;
        hasTab = hasCreativeModeTab;
    }

    @NotNull
    public RegistrySupplier<CreativeModeTab, CreativeModeTab> getTab() throws IllegalAccessException {
        if (!hasTab)
            throw new IllegalAccessException("This Module is not allowed to create a tab");
        else if (tab == null)
            throw new IllegalAccessException("This Module's tab is not registered");
        return tab;
    }

    protected void registerTab(Holder<? extends Item> icon) throws IllegalAccessException {
        if (!hasTab)
            throw new IllegalAccessException("This Module is not allowed to create a tab");
        else if (tab == null)
            tab = RegistryHolder.Tabs.register(id, () -> CreativeTabRegistry.create(Component.translatable("itemGroup."+id), () -> new ItemStack(icon.value(), 1)));
        else
            throw new IllegalAccessException("Illegal Visit Function registerTab: tab is present.");
    }

    protected  <I extends Item> RegistrySupplier<Item, I> registerItem(String id, Function<Item.Properties, I> factory, Item.Properties properties) {
        return RegistryHolder.Items.register(id, () -> factory.apply(
                properties.setId(Alloy.getRegistryKey(Registries.ITEM, id))
        ));
    }

    protected <I extends Item> RegistrySupplier<Item, I> registerItem(String id, Function<Item.Properties, I> factory) {
        return registerItem(id, factory, new Item.Properties());
    }

    protected RegistrySupplier<Item, Item> registerSimpleItem(String id, Item.Properties properties) {
        return registerItem(id, Item::new, properties);
    }

    protected RegistrySupplier<Item, Item> registerSimpleItem(String id) {
        return registerItem(id, Item::new, new Item.Properties());
    }

    protected RegistrySupplier<Item, Item> registerBucketItem(String id, Supplier<? extends Fluid> fluid) {
        return registerItem(id, property -> ServicesManager.FLUID_SERVICE.getBucketItemByPlatform(fluid, property));
    }

    protected ArmorSet registerArmors(String id, ArmorMaterial armorMaterial) {
        RegistrySupplier<Item, ArmorItem> head = registerItem(id+"_helmet", properties -> new ArmorItem(armorMaterial, ArmorType.HELMET, properties));
        RegistrySupplier<Item, ArmorItem> chest = registerItem(id+"_chestplate", properties -> new ArmorItem(armorMaterial, ArmorType.CHESTPLATE, properties));
        RegistrySupplier<Item, ArmorItem> leg = registerItem(id+"_leggings", properties -> new ArmorItem(armorMaterial, ArmorType.LEGGINGS, properties));
        RegistrySupplier<Item, ArmorItem> feet = registerItem(id+"_boots", properties -> new ArmorItem(armorMaterial, ArmorType.BOOTS, properties));
        return new ArmorSet(head, chest, leg, feet);
    }

    protected <B extends Block> RegistrySupplier<Block, B> registerBlock(String id, Function<BlockBehaviour.Properties, B> factory, BlockBehaviour.Properties properties) {
        return RegistryHolder.Blocks.register(id, () -> factory.apply(
                properties.setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Alloy.MOD_ID, id)))
        ));
    }

    protected <B extends Block> BlockWithItem<B> registerBlockWithItem(String id, Function<BlockBehaviour.Properties, B> factory, BlockBehaviour.Properties properties, Item.Properties itemProperties) {
        RegistrySupplier<Block, B> x = registerBlock(id, factory, properties);
        return new BlockWithItem<>(x, registerItem(id, p -> new BlockItem(x.get(), p), itemProperties));
    }

    protected <B extends Block> BlockWithItem<B> registerBlockWithItem(String id, Function<BlockBehaviour.Properties, B> factory) {
        return registerBlockWithItem(id, factory, BlockBehaviour.Properties.of(), new Item.Properties());
    }

    @SafeVarargs
    protected final <T extends BlockEntity> RegistrySupplier<BlockEntityType<?>, BlockEntityType<T>> registerTile(String id, RegistryHolder.BlockEntitySupplier<T> factory, Supplier<? extends Block>... validBlocks) {
        return RegistryHolder.Tiles.register(id, () -> new BlockEntityType<>(factory::create, Set.of(Arrays.stream(validBlocks).map(Supplier::get).toArray(Block[]::new))));
    }

    protected <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> RegistrySupplier<MenuType<?>, MenuType<T>> registerMenu(String id, MenuType.MenuSupplier<T> factory, MenuRegistry.ScreenFactory<T, S> screenFactory) {
        MenuType<T> type = MenuRegistry.ofExtended((a, b, c) -> factory.create(a, b));
        MenuRegistry.registerScreenFactory(type, screenFactory);
        return RegistryHolder.Menus.register(id, () -> type);
    }

    protected <T extends Recipe<?>> RegistrySupplier<RecipeType<?>, RecipeType<T>> registerRecipeType(String id) {
        return RegistryHolder.Recipes.register(id, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return ResourceLocation.fromNamespaceAndPath(Alloy.MOD_ID, id).toString();
            }
        });
    }

    protected <T extends Recipe<?>> RegistrySupplier<RecipeSerializer<?>, RecipeSerializer<T>> registerSerializer(String id, Supplier<? extends RecipeSerializer<T>> factory) {
        return RegistryHolder.Serializers.register(id, factory);
    }

    protected <T extends ArchitecturyFlowingFluid> RegistrySupplier<Fluid, T> registerFluid(String id, ArchitecturyFluidAttributes attributesMemory, Class<T> clazz) throws RuntimeException {
        return RegistryHolder.Fluids.register(id, () -> {
            try {
                return clazz.getConstructor(ArchitecturyFluidAttributes.class).newInstance(attributesMemory);
            } catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RuntimeException(clazz.toString(), e);
            }
        });
    }

    protected RegistrySupplier<Fluid, ArchitecturyFlowingFluid.Flowing> registerFlowing(String id, ArchitecturyFluidAttributes attributesMemory, Function<ArchitecturyFluidAttributes, ArchitecturyFlowingFluid.Flowing> func) {
        return RegistryHolder.Fluids.register(id, () -> func.apply(attributesMemory));
    }

    protected RegistrySupplier<ModularItemComponent, ModularItemComponent> registerComponent(String id, ModularItemComponent component) {
        return RegistryHolder.ModularComponents.register(id, () -> component);
    }

    public String getId() {
        return id;
    }
}
