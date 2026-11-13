package dxxi3d.leaf_decay.mixin;

import dxxi3d.leaf_decay.PendingDecays;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements PendingDecays.Accessor {

    @Unique
    private final PendingDecays pendingDecays = new PendingDecays();

    @Override
    public PendingDecays dxxi3d$leafDecay$pending() {
        return this.pendingDecays;
    }
}