package top.friendcraft.alloy.core.reflection;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import top.friendcraft.alloy.Alloy;

public class TagKeys {
    public static final TagKey<EntityType<?>> LADDER_CLIMBABLE = createEntityType("ladder_climbable");
    private static TagKey<EntityType<?>> createEntityType(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Alloy.getIdentifier(name));
    }
}
