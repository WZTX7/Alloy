package top.friendcraft.alloy.core.services;

import java.util.ServiceLoader;

public class ServicesManager {
    public static FluidService FLUID_SERVICE;
    public static RegisterService REGISTER_SERVICE;
    public static void initialize() {
        ServiceLoader<FluidService> loader = ServiceLoader.load(FluidService.class);
        FLUID_SERVICE = loader.findFirst().orElseThrow(IllegalStateException::new);
        ServiceLoader<RegisterService> loader2 = ServiceLoader.load(RegisterService.class);
        REGISTER_SERVICE = loader2.findFirst().orElseThrow(IllegalStateException::new);
    }
}
