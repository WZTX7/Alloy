package top.friendcraft.alloy.common.item;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import top.friendcraft.alloy.Alloy;
import top.friendcraft.alloy.core.registry.RegistrySupplier;

import java.util.EnumMap;
import java.util.Map;

public record ArmorSet(RegistrySupplier<Item, ArmorItem> head, RegistrySupplier<Item, ArmorItem> chest, RegistrySupplier<Item, ArmorItem> leg, RegistrySupplier<Item, ArmorItem> feet) {
    private static final EquipmentModel MODEL_ROSE_GOLD = new EquipmentModel("rose_gold");
    private static final EquipmentModel MODEL_OBSIDIANITE = new EquipmentModel("obsdianite");
    private static final EquipmentModel MODEL_ENHANCED_NETHERITE = new EquipmentModel("enhanced_netherite");
    private static final EquipmentModel MODEL_STEEL = new EquipmentModel("steel");
    public static ArmorMaterial OBSIDIANITE = new ArmorMaterial(80, Util.make(new EnumMap<>(ArmorType.class), (enumMap) -> {
        enumMap.put(ArmorType.BOOTS, 4);
        enumMap.put(ArmorType.LEGGINGS, 7);
        enumMap.put(ArmorType.CHESTPLATE, 10);
        enumMap.put(ArmorType.HELMET, 4);
        enumMap.put(ArmorType.BODY, 13);
    }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, MODEL_OBSIDIANITE.key(), EquipmentAssets.NETHERITE);
    public static ArmorMaterial ROSE_GOLD = new ArmorMaterial(10, Util.make(new EnumMap<>(ArmorType.class), (enumMap) -> {
        enumMap.put(ArmorType.BOOTS, 2);
        enumMap.put(ArmorType.LEGGINGS, 4);
        enumMap.put(ArmorType.CHESTPLATE, 5);
        enumMap.put(ArmorType.HELMET, 2);
        enumMap.put(ArmorType.BODY, 7);
    }), 33, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, MODEL_ROSE_GOLD.key(), MODEL_ROSE_GOLD.model());
    public static ArmorMaterial ENHANCED_NETHERITE = new ArmorMaterial(50, Util.make(new EnumMap<>(ArmorType.class), (enumMap) -> {
        enumMap.put(ArmorType.BOOTS, 4);
        enumMap.put(ArmorType.LEGGINGS, 6);
        enumMap.put(ArmorType.CHESTPLATE, 9);
        enumMap.put(ArmorType.HELMET, 4);
        enumMap.put(ArmorType.BODY, 13);
    }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, MODEL_ENHANCED_NETHERITE.key(), EquipmentAssets.NETHERITE);
    public static ArmorMaterial STEEL = new ArmorMaterial(23, Util.make(new EnumMap<>(ArmorType.class), (enumMap) -> {
        enumMap.put(ArmorType.BOOTS, 2);
        enumMap.put(ArmorType.LEGGINGS, 5);
        enumMap.put(ArmorType.CHESTPLATE, 7);
        enumMap.put(ArmorType.HELMET, 3);
        enumMap.put(ArmorType.BODY, 5);
    }), 10, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, MODEL_STEEL.key(), EquipmentAssets.IRON);
    private record EquipmentModel(String id) {
        public ResourceKey<EquipmentAsset> model() {
            return Alloy.getRegistryKey(EquipmentAssets.ROOT_ID, id);
        }

        public TagKey<Item> key() {
            return TagKey.create(Registries.ITEM, Alloy.getIdentifier("repairs_"+id+"_armor"));
        }
    }
}
