package com.fastasyncworldedit.core.regions.filter;

import com.fastasyncworldedit.core.util.collection.LongHashSet;
import com.sk89q.worldedit.math.BlockVector2;

public abstract class CuboidRegionFilter implements RegionFilter {

    private final LongHashSet occupiedRegions;
    private final LongHashSet unoccupiedChunks;

    public CuboidRegionFilter() {
        this.occupiedRegions = new LongHashSet();
        this.unoccupiedChunks = new LongHashSet();
    }

    /**
     * Loop over all regions and call add(...) with the corners
     */
    public abstract void calculateRegions();

    public void add(BlockVector2 pos1, BlockVector2 pos2) {
        int ccx1 = pos1.x() >> 9;
        int ccz1 = pos1.z() >> 9;
        int ccx2 = pos2.x() >> 9;
        int ccz2 = pos2.z() >> 9;
        for (int x = ccx1; x <= ccx2; x++) {
            for (int z = ccz1; z <= ccz2; z++) {
                if (!occupiedRegions.containsKey(x, z)) {
                    occupiedRegions.add(x, z);
                    int bcx = x << 5;
                    int bcz = z << 5;
                    int tcx = bcx + 32;
                    int tcz = bcz + 32;
                    for (int cz = bcz; cz < tcz; cz++) {
                        for (int cx = bcx; cx < tcx; cx++) {
                            unoccupiedChunks.add(cx, cz);
                        }
                    }
                }
            }
        }
        int cx1 = pos1.x() >> 4;
        int cz1 = pos1.z() >> 4;
        int cx2 = pos2.x() >> 4;
        int cz2 = pos2.z() >> 4;
        for (int chunkZ = cz1; chunkZ <= cz2; chunkZ++) {
            for (int chunkX = cx1; chunkX <= cx2; chunkX++) {
                unoccupiedChunks.remove(chunkX, chunkZ);
            }
        }
    }

    public void clear() {
        occupiedRegions.popAll();
        unoccupiedChunks.popAll();
    }

    @Override
    public boolean containsRegion(int mcaX, int mcaZ) {
        return occupiedRegions.containsKey(mcaX, mcaZ);
    }

    @Override
    public boolean containsChunk(int chunkX, int chunkZ) {
        int mcaX = chunkX >> 5;
        int mcaZ = chunkZ >> 5;
        return occupiedRegions.containsKey(mcaX, mcaZ) && !unoccupiedChunks.containsKey(chunkX, chunkZ);
    }

}
