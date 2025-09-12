package top.friendcraft.alloy.core.services;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import top.friendcraft.alloy.core.registry.DeferredRegister;

public interface RegisterService {
    class RegistryProperties<T> {
        private int maxId = -1;
        private boolean isSync = false;
        private @Nullable ResourceLocation defaultKey;
        private final ResourceKey<Registry<T>> registryKey;
        private boolean isRegistrationCheck = true;

        public RegistryProperties(ResourceKey<Registry<T>> registryKey) {
            this.registryKey = registryKey;
        }

        public final ResourceKey<Registry<T>> registryKey() {
            return this.registryKey;
        }

        public RegistryProperties<T> defaultKey(ResourceLocation key) {
            this.defaultKey = key;
            return this;
        }

        public RegistryProperties<T> defaultKey(ResourceKey<T> key) {
            this.defaultKey = key.location();
            return this;
        }

        public ResourceLocation defaultKey() {
            return this.defaultKey;
        }

        public RegistryProperties<T> maxId(int maxId) {
            this.maxId = maxId;
            return this;
        }

        public int maxId() {
            return this.maxId;
        }

        public RegistryProperties<T> setIsSync(boolean sync) {
            this.isSync = sync;
            return this;
        }

        public boolean isSync() {
            return this.isSync;
        }

        public RegistryProperties<T> disableRegistrationCheck() {
            this.isRegistrationCheck = false;
            return this;
        }

        public boolean isRegistrationCheck() {
            return isRegistrationCheck;
        }
    }

    <T> Registry<T> registerRegistryByPlatform(RegistryProperties<T> properties);
    <T> DeferredRegister<T> createDeferredRegister(ResourceKey<Registry<T>> key, String modId);
}
