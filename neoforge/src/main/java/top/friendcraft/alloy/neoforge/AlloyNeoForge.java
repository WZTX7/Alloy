package top.friendcraft.alloy.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import top.friendcraft.alloy.Alloy;
import net.neoforged.fml.common.Mod;

@Mod(Alloy.MOD_ID)
public final class AlloyNeoForge {

    public AlloyNeoForge(IEventBus bus) {
        // Run our common setup.
        Alloy.init();
        bus.addListener(AlloyNeoForge::onCommonSetup);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLLoadCompleteEvent event) {
        Alloy.registerFuels();
    }
}
