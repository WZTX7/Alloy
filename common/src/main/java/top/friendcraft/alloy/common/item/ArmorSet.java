package top.friendcraft.alloy.common.item;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentModels;
import top.friendcraft.alloy.Alloy;

import java.util.EnumMap;

public record ArmorSet(RegistrySupplier<ArmorItem> head, RegistrySupplier<ArmorItem> chest, RegistrySupplier<ArmorItem> leg, RegistrySupplier<ArmorItem> feet) {
    private static final EquipmentModel MODEL_ROSE_GOLD = new EquipmentModel("rose_gold");
    private static final EquipmentModel MODEL_OBSIDIANITE = new EquipmentModel("obsdianite");
    private static final EquipmentModel MODEL_ENHANCED_NETHERITE = new EquipmentModel("enhanced_netherite");
    public static ArmorMaterial OBSIDIANITE = new ArmorMaterial(80, Util.make(new EnumMap<>(ArmorType.class), (enumMap) -> {
        enumMap.put(ArmorType.BOOTS, 4);
        enumMap.put(ArmorType.LEGGINGS, 7);
        enumMap.put(ArmorType.CHESTPLATE, 10);
        enumMap.put(ArmorType.HELMET, 4);
        enumMap.put(ArmorType.BODY, 13);
    }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, MODEL_OBSIDIANITE.key(), EquipmentModels.NETHERITE);
    public static ArmorMaterial ROSE_GOLD = new ArmorMaterial(10, Util.make(new EnumMap<>(ArmorType.class), (enumMap) -> {
        enumMap.put(ArmorType.BOOTS, 2);
        enumMap.put(ArmorType.LEGGINGS, 4);
        enumMap.put(ArmorType.CHESTPLATE, 5);
        enumMap.put(ArmorType.HELMET, 2);
        enumMap.put(ArmorType.BODY, 7);
    }), 33, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, MODEL_ROSE_GOLD.key(), MODEL_ROSE_GOLD.model());
    public static ArmorMaterial ENHANCED_NETHERITE = new ArmorMaterial(80, Util.make(new EnumMap<>(ArmorType.class), (enumMap) -> {
        enumMap.put(ArmorType.BOOTS, 4);
        enumMap.put(ArmorType.LEGGINGS, 6);
        enumMap.put(ArmorType.CHESTPLATE, 9);
        enumMap.put(ArmorType.HELMET, 4);
        enumMap.put(ArmorType.BODY, 13);
    }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, MODEL_ENHANCED_NETHERITE.key(), EquipmentModels.NETHERITE);
    private record EquipmentModel(String id) {
        public ResourceLocation model() {
            return ResourceLocation.fromNamespaceAndPath(Alloy.MOD_ID, id);
        }

        public TagKey<Item> key() {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Alloy.MOD_ID, "repairs_"+id+"_armor"));
        }
    }
}
