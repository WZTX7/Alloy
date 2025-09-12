package top.friendcraft.alloy.core.modules;

import top.friendcraft.alloy.common.item.ArmorSet;
import top.friendcraft.alloy.core.RegisterModule;

public class ArmorsModule extends RegisterModule {
    public ArmorSet obsidanites, rose_golds, enhanced_netherites, steels;
    public ArmorsModule() {
        super("armors", true);
        obsidanites = registerArmors("obsidianite", ArmorSet.OBSIDIANITE);
        rose_golds = registerArmors("rose_golden", ArmorSet.ROSE_GOLD);
        enhanced_netherites = registerArmors("enhanced_netherite", ArmorSet.ENHANCED_NETHERITE);
        steels = registerArmors("steel", ArmorSet.STEEL);
        try {
            registerTab(obsidanites.chest());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
