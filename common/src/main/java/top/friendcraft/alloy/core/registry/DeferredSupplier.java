package top.friendcraft.alloy.core.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface DeferredSupplier<T> extends Supplier<T> {
    ResourceLocation getRegistryId();
    ResourceLocation getSelfId();
    boolean isPresent();
    static <T> dev.architectury.registry.registries.DeferredSupplier<T> toArchitecturySupplier(DeferredSupplier<T> raw) {
        return new DeferredSupplierImpl<>(raw);
    }

    class DeferredSupplierImpl<T> implements DeferredSupplier<T>, dev.architectury.registry.registries.DeferredSupplier<T> {

        DeferredSupplier<T> raw;
        public DeferredSupplierImpl(DeferredSupplier<T> raw) {
            this.raw = raw;
        }

        @Override
        public ResourceLocation getRegistryId() {
            return raw.getRegistryId();
        }

        @Override
        public ResourceLocation getId() {
            return raw.getSelfId();
        }

        @Override
        public ResourceLocation getSelfId() {
            return raw.getSelfId();
        }

        @Override
        public boolean isPresent() {
            return raw.isPresent();
        }

        @Override
        public T get() {
            return raw.get();
        }
    }
}
