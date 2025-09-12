package top.friendcraft.alloy.neoforge;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import top.friendcraft.alloy.Alloy;
import net.neoforged.fml.common.Mod;
import top.friendcraft.alloy.core.registry.RegistryHolder;
import top.friendcraft.alloy.core.services.neoforge.RegisterServiceImpl;

@Mod(Alloy.MOD_ID)
public final class AlloyNeoForge {

    public AlloyNeoForge(IEventBus bus) {
        // Run our common setup.
        bus.register(AlloyNeoForge.class);
        Alloy.init();
        RegistryHolder.register();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onNewRegistryCreate(NewRegistryEvent event) {
        RegisterServiceImpl.registerRegistries(event);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLLoadCompleteEvent event) {
        Alloy.registerFuels();
    }
}
