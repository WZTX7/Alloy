package top.friendcraft.alloy.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import top.friendcraft.alloy.core.services.ServicesManager;

import java.util.function.Supplier;

public interface DeferredRegister<T> {
    <I extends T> RegistrySupplier<T, I> register(final String id, final Supplier<? extends I> supplier);
    <I extends T> RegistrySupplier<T, I> register(final ResourceLocation id, final Supplier<? extends I> supplier);
    void register();
    static <T> DeferredRegister<T> create(String modId, ResourceKey<Registry<T>> key) {
        return ServicesManager.REGISTER_SERVICE.createDeferredRegister(key, modId);
    }
}
