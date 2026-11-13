package dxxi3d.leaf_decay;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeafDecay implements ModInitializer {

    private static final String MOD_NAME = "Leaf Decay";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        LOGGER.info("[{}] mod loaded!", MOD_NAME);
    }
}
