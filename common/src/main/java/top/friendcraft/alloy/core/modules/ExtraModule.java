package top.friendcraft.alloy.core.modules;

import top.friendcraft.alloy.Alloy;
import top.friendcraft.alloy.common.item.ModularItemComponent;
import top.friendcraft.alloy.core.RegisterModule;
import top.friendcraft.alloy.core.registry.RegistryHolder;

public class ExtraModule extends RegisterModule {
    public ExtraModule() {
        super("extra", false);
        registerComponent("empty", ModularItemComponent.EMPTY);
        registerComponent("blade", new ModularItemComponent(new ModularItemComponent.Properties().setId(
                Alloy.getRegistryKey(RegistryHolder.Registries.MODULAR_ITEM_COMPONENT, "blade")
        )));
    }
}
