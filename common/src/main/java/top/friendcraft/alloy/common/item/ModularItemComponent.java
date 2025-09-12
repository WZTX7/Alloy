package top.friendcraft.alloy.common.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.DependantName;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import top.friendcraft.alloy.Alloy;
import top.friendcraft.alloy.core.registry.RegistryHolder;

import java.util.Objects;

public class ModularItemComponent {
    public static final Codec<Holder<ModularItemComponent>> CODEC;
    public static final ModularItemComponent EMPTY = new ModularItemComponent(new Properties().setId(
            Alloy.getRegistryKey(RegistryHolder.Registries.MODULAR_ITEM_COMPONENT, "empty")
    ));
    private Properties properties;
    protected final String descriptionId;
    public ModularItemComponent(Properties properties) {
        this.properties = properties;
        this.descriptionId = properties.effectiveDescriptionId();
    }
    static {
        CODEC = RegistryHolder.BuiltinRegistries.MODULAR_ITEM_COMPONENT.holderByNameCodec().validate((holder) ->
                holder.is(Alloy.getRegistryKey(RegistryHolder.Registries.MODULAR_ITEM_COMPONENT, "empty")) ? DataResult.error(() ->
                        "Item module must not be alloy:empty"
                ) : DataResult.success(holder)
        );
    }

    public static class Properties {
        private final DependantName<ModularItemComponent, String> descriptionId = (resourceKey) ->
                Util.makeDescriptionId("modular_item_components", resourceKey.location());
        private DependantName<ModularItemComponent, ResourceLocation> model;
        @Nullable
        private ResourceKey<ModularItemComponent> id;
        public Properties() {
            model = ResourceKey::location;
        }

        public Properties setId(ResourceKey<ModularItemComponent> id) {
            this.id = id;
            return this;
        }

        protected String effectiveDescriptionId() {
            return this.descriptionId.get(Objects.requireNonNull(this.id, "Modular Component id not set"));
        }
    }
}
