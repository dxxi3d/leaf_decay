package dxxi3d.leaf_decay;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class PendingDecays {
    private static final int DELAY_RANGE_TICKS = 40;
    private static final int PRUNE_INTERVAL_TICKS = 80;
    private static final int MAX_PENDING = 16_384;

    private Long2LongMap deadlines = new Long2LongOpenHashMap();
    private long lastPrune;

    public void tick(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState state = level.getBlockState(pos);
        long key = pos.asLong();

        if (state.getValue(LeavesBlock.PERSISTENT) || state.getValue(LeavesBlock.DISTANCE) < 7) {
            this.deadlines.remove(key);
            return;
        }

        if (this.deadlines.containsKey(key)) {
            this.deadlines.remove(key);
            state.randomTick(level, pos, random);
            return;
        }

        long now = level.getGameTime();
        if (now - this.lastPrune >= PRUNE_INTERVAL_TICKS) {
            this.lastPrune = now;
            prune(now);
        }

        int delay = random.nextInt(DELAY_RANGE_TICKS);
        this.deadlines.put(key, now + delay);
        level.scheduleTick(pos, state.getBlock(), delay);
    }

    private void prune(long now) {
        LongIterator iterator = this.deadlines.keySet().iterator();
        while (iterator.hasNext()) {
            if (this.deadlines.get(iterator.nextLong()) <= now) {
                iterator.remove();
            }
        }

        if (this.deadlines.isEmpty() || this.deadlines.size() > MAX_PENDING) {
            this.deadlines = new Long2LongOpenHashMap();
        }
    }

    public interface Accessor {
        PendingDecays dxxi3d$leafDecay$pending();
    }
}