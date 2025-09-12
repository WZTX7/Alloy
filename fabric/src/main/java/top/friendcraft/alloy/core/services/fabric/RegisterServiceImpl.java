package top.friendcraft.alloy.core.services.fabric;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import top.friendcraft.alloy.core.registry.DeferredRegister;
import top.friendcraft.alloy.core.registry.fabric.DeferredRegisterImpl;
import top.friendcraft.alloy.core.services.RegisterService;

public class RegisterServiceImpl implements RegisterService {
    public <T> Registry<T> registerRegistryByPlatform(RegisterService.RegistryProperties<T> properties) {
        FabricRegistryBuilder<T, ? extends WritableRegistry<T>> builder = (
                (properties.defaultKey() == null) ?
                        FabricRegistryBuilder.createSimple(properties.registryKey()) :
                        FabricRegistryBuilder.createDefaulted(properties.registryKey(), properties.defaultKey())
                );
        if (properties.isSync()) builder.attribute(RegistryAttribute.SYNCED);
        return builder.buildAndRegister();
    }

    @Override
    public <T> DeferredRegister<T> createDeferredRegister(ResourceKey<Registry<T>> key, String modId) {
        return DeferredRegisterImpl.create(key, modId);
    }
}
