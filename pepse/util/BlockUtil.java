package pepse.util;

import pepse.world.Block;

public class BlockUtil {

    //todo clean
    // We are doing the tip of 2.2.5 to finding the closet location of the block by the block size
    public static int getNearestBlockLocation(int x) {
        if (x < 0) {
            return x - (Block.SIZE - x % Block.SIZE);
        } else {
            return x - x % Block.SIZE;
        }
    }
}
