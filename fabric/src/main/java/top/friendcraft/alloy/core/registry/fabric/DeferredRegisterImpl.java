package top.friendcraft.alloy.core.registry.fabric;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;
import top.friendcraft.alloy.core.registry.DeferredRegister;
import top.friendcraft.alloy.core.registry.RegistrySupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DeferredRegisterImpl<T> implements DeferredRegister<T> {

    private final ResourceKey<Registry<T>> key;
    private final List<RegisterSupplier<? extends T>> suppliers = new ArrayList<>();
    @Nullable
    private String modId;

    private DeferredRegisterImpl(ResourceKey<Registry<T>> key) {
        this.key = key;
    }

    private DeferredRegisterImpl(ResourceKey<Registry<T>> key, String modId) {
        this.key = key;
        this.modId = modId;
    }

    @Override
    public <I extends T> RegistrySupplier<T, I> register(String id, Supplier<? extends I> supplier) {
        return register(ResourceLocation.fromNamespaceAndPath(Objects.requireNonNull(modId), id), supplier);
    }

    @Override
    public <I extends T> RegistrySupplier<T, I> register(ResourceLocation id, Supplier<? extends I> supplier) {
        RegistrySupplierImpl<T, I> sup = new RegistrySupplierImpl<>(ResourceKey.create(key, id));
        suppliers.add(new RegisterSupplier<>(supplier, id));
        return sup;
    }

    @Override
    public void register() {
        Registry<T> regsitry = (Registry<T>) BuiltInRegistries.REGISTRY.getValue(this.key.registry());
        suppliers.stream()
                .forEach(a -> Registry.register(regsitry, a.getId(), a.get()));
    }

    public static <T> DeferredRegister<T> create(ResourceKey<Registry<T>> key, String modId) {
        return new DeferredRegisterImpl<>(key, modId);
    }

    public static <T> DeferredRegisterImpl<T> createWithoutModId(ResourceKey<Registry<T>> key) {
        return new DeferredRegisterImpl<>(key);
    }

    private static class RegisterSupplier<B> implements Supplier<B> {
        final Supplier<B> sup;
        final ResourceLocation id;
        private RegisterSupplier(Supplier<B> sup, ResourceLocation id) {
            this.sup = sup;
            this.id = id;
        }

        @Override
        public B get() {
            return sup.get();
        }

        public ResourceLocation getId() {
            return this.id;
        }
    }

    private static class RegistrySupplierImpl<B, C extends B> implements RegistrySupplier<B, C> {
        @Nullable
        Holder<B> holder;
        final ResourceKey<B> key;

        private RegistrySupplierImpl(ResourceKey<B> key) {
            this.key = key;
        }

        protected final void bind() {
            if (this.holder != null) return;
            Registry<B> registry = (Registry<B>) BuiltInRegistries.REGISTRY.getValue(this.key.registry());
            if (registry != null) {
                this.holder = registry.get(this.key).orElse(null);
                return;
            } throw new IllegalStateException("Registry is not prepared for" + this + ": " + this.key.registry());
        }

        @Override
        public B value() {
            bind();
            return holder.value();
        }

        @Override
        public boolean isBound() {
            bind();
            return holder.isBound();
        }

        @Override
        public boolean is(ResourceLocation location) {
            bind();
            return holder.is(location);
        }

        @Override
        public boolean is(ResourceKey<B> resourceKey) {
            bind();
            return holder.is(resourceKey);
        }

        @Override
        public boolean is(Predicate<ResourceKey<B>> predicate) {
            bind();
            return holder.is(predicate);
        }

        @Override
        public boolean is(TagKey<B> tagKey) {
            bind();
            return holder.is(tagKey);
        }

        @Override
        public boolean is(Holder<B> holder) {
            bind();
            return holder.is(holder);
        }

        @Override
        public Stream<TagKey<B>> tags() {
            bind();
            return holder.tags();
        }

        @Override
        public Either<ResourceKey<B>, B> unwrap() {
            bind();
            return holder.unwrap();
        }

        @Override
        public Optional<ResourceKey<B>> unwrapKey() {
            bind();
            return holder.unwrapKey();
        }

        @Override
        public Kind kind() {
            bind();
            return holder.kind();
        }

        @Override
        public boolean canSerializeIn(HolderOwner<B> owner) {
            bind();
            return holder.canSerializeIn(owner);
        }

        @Override
        public String getRegisteredName() {
            bind();
            return holder.getRegisteredName();
        }

        @Override
        public ResourceLocation getRegistryId() {
            return this.key.registry();
        }

        @Override
        public ResourceLocation getSelfId() {
            return this.key.location();
        }

        @Override
        public boolean isPresent() {
            return this.holder != null;
        }

        @Override
        public C get() {
            bind();
            return (C) this.holder.value();
        }
    }
}
