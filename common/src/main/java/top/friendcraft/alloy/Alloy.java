package top.friendcraft.alloy;

import com.mojang.logging.LogUtils;
import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import top.friendcraft.alloy.common.item.InputData;
import top.friendcraft.alloy.core.modules.AlloysModule;
import top.friendcraft.alloy.core.modules.ArmorsModule;
import top.friendcraft.alloy.core.modules.BlockModule;
import top.friendcraft.alloy.core.modules.ExtraModule;
import top.friendcraft.alloy.core.registry.RegistryHolder;
import top.friendcraft.alloy.core.services.ServicesManager;

import java.util.*;

public final class Alloy {
    // Mod Default Settings
    public static final String MOD_ID = "alloy";

    // Class Logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // Melting Settings
    public static List<InputData[]> inputData = new ArrayList<>();

    // Register Modules
    public static AlloysModule alloys;
    public static ArmorsModule armors;
    public static BlockModule blocks;
    public static ExtraModule extras;


    public static void init() {
        ServicesManager.initialize();
        log("Input Data");
        inputData.addAll(List.of(new InputData[0], new InputData[]{
                new InputData(4, 1, 20, 16),
                new InputData(1, 1, 47, 53, 32)
        }));
        log("Registry Modules");
        alloys = new AlloysModule();
        armors = new ArmorsModule();
        blocks = new BlockModule();
        extras = new ExtraModule();
    }

    public static void registerFuels() {
        FuelRegistry.register(2400, alloys.sunlight_coal.value());
        FuelRegistry.register(4800, alloys.active_sunlight_coal.value());
    }

    private static void log(String message) {
        LOGGER.info("Register {};", message);
    }

    public static ResourceLocation getIdentifier(String id) {
        return ResourceLocation.fromNamespaceAndPath(Alloy.MOD_ID, id);
    }

    public static <T> ResourceKey<T> getRegistryKey(ResourceKey<? extends Registry<T>> registry, String id) {
        return ResourceKey.create(registry, getIdentifier(id));
    }

    public static <T> ResourceKey<Registry<T>> createRegistryKey(String id) {
        return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Alloy.MOD_ID, id));
    }

}
