package top.friendcraft.alloy.core.registry.neoforge;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import top.friendcraft.alloy.Alloy;
import top.friendcraft.alloy.core.registry.DeferredRegister;
import top.friendcraft.alloy.core.registry.RegistrySupplier;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DeferredRegisterImpl<T> implements DeferredRegister<T> {

    private final net.neoforged.neoforge.registries.DeferredRegister<T> raw;
    private final ResourceKey<Registry<T>> key;

    private DeferredRegisterImpl(ResourceKey<Registry<T>> key, String modId) {
        raw = net.neoforged.neoforge.registries.DeferredRegister.create(key, modId);
        this.key = key;
    }

    @Override
    public <I extends T> RegistrySupplier<T, I> register(String id, Supplier<? extends I> supplier) {
        return new RegistrySupplierImpl<>(raw.register(id, supplier));
    }

    @Override
    public <I extends T> RegistrySupplier<T, I> register(ResourceLocation id, Supplier<? extends I> supplier) {
        if (!id.getNamespace().equals(raw.getNamespace())) {
            throw new RuntimeException("Namespace wrong");
        }
        return register(id.getPath(), supplier);
    }

    @Override
    public void register() {
        raw.register(ModList.get().getModContainerById(Alloy.MOD_ID)
                .map(ModContainer::getEventBus).orElseThrow());
    }

    public static <T> DeferredRegister<T> create(ResourceKey<Registry<T>> key, String modId) {
        return new DeferredRegisterImpl<>(key, modId);
    }

    private class RegistrySupplierImpl<B, C extends B> implements RegistrySupplier<B, C> {
        private final DeferredHolder<B, C> holder;

        public RegistrySupplierImpl(DeferredHolder<B, C> holder) {
            this.holder = holder;
        }

        @Override
        public B value() {
            return holder.value();
        }

        @Override
        public boolean isBound() {
            return holder.isBound();
        }

        @Override
        public boolean is(@Nonnull ResourceLocation location) {
            return holder.is(location);
        }

        @Override
        public boolean is(@Nonnull ResourceKey<B> resourceKey) {
            return holder.is(resourceKey);
        }

        @Override
        public boolean is(@Nonnull Predicate<ResourceKey<B>> predicate) {
            return holder.is(predicate);
        }

        @Override
        public boolean is(@Nonnull TagKey<B> tagKey) {
            return holder.is(tagKey);
        }

        @Override
        public boolean is(Holder<B> holder) {
            return holder.is(holder);
        }

        @Override
        @Nonnull
        public Stream<TagKey<B>> tags() {
            return holder.tags();
        }

        @Override
        @Nonnull
        public Either<ResourceKey<B>, B> unwrap() {
            return holder.unwrap();
        }

        @Override
        @Nonnull
        public Optional<ResourceKey<B>> unwrapKey() {
            return holder.unwrapKey();
        }

        @Override
        @Nonnull
        public Kind kind() {
            return holder.kind();
        }

        @Override
        public boolean canSerializeIn(@Nonnull HolderOwner<B> owner) {
            return holder.canSerializeIn(owner);
        }

        @Override
        public ResourceLocation getRegistryId() {
            return key.location();
        }

        @Override
        public ResourceLocation getSelfId() {
            return holder.getId();
        }

        @Override
        public boolean isPresent() {
            return holder.isBound();
        }

        @Override
        public C get() {
            return holder.get();
        }
    }
}
