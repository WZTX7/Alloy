package top.friendcraft.alloy.core.services.neoforge;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import top.friendcraft.alloy.core.registry.DeferredRegister;
import top.friendcraft.alloy.core.registry.neoforge.DeferredRegisterImpl;
import top.friendcraft.alloy.core.services.RegisterService;

import java.util.ArrayList;
import java.util.List;

public class RegisterServiceImpl implements RegisterService {

    private static final List<Registry<?>> list = new ArrayList<>();

    public <T> Registry<T> registerRegistryByPlatform(RegisterService.RegistryProperties<T> properties) {
        RegistryBuilder<T> builder = new RegistryBuilder<>(properties.registryKey())
                .defaultKey(properties.defaultKey())
                .maxId(properties.maxId())
                .sync(properties.isSync());
        if (!properties.isRegistrationCheck()) builder.disableRegistrationCheck();
        Registry<T> registry = builder.create();
        list.add(registry);
        return registry;
    }

    @Override
    public <T> DeferredRegister<T> createDeferredRegister(ResourceKey<Registry<T>> key, String modId) {
        return DeferredRegisterImpl.create(key, modId);
    }

    public static void registerRegistries(NewRegistryEvent event) {
        for (Registry<?> registry : list) {
            event.register(registry);
        }
    }
}
