package top.friendcraft.alloy.core.registry;

import net.minecraft.core.Holder;

public interface RegistrySupplier<B, T extends B> extends Holder<B>, DeferredSupplier<T> {
}
