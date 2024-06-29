package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.BlockUtil;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Terrain {
    private float groundHeightAtX0 = 0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private int seed = 1234;

    public Terrain(Vector2 windowDimensions, int seed) {
        this.groundHeightAtX0 = windowDimensions.y() * 2 / 3;
        this.seed = seed;
    }

    public float groundHeightAt(float x) {
        float noise = (float) new NoiseGenerator(seed, (int) groundHeightAtX0).noise(x, Block.SIZE * 7);
        return groundHeightAtX0 + noise;
    }

    public List<Block> createInRange(int minX, int maxX) {
        int finalMinX = BlockUtil.getNearestBlockLocation(minX);
        int finalMaxX = BlockUtil.getNearestBlockLocation(maxX);
        int currentX = finalMinX;
        List<Block> blocks = new LinkedList<>();

        while (currentX < finalMaxX) {
            float y = (float) (Math.floor(groundHeightAt(currentX) / Block.SIZE) * Block.SIZE);
            addBlocksAtX(currentX, y, blocks);
            currentX += Block.SIZE;
        }

        return blocks;
    }

    private static void addBlocksAtX(int currentX, float y, List<Block> blocks) {
        for (int i = 0; i < TERRAIN_DEPTH; i++) {
            Block block = new Block(Vector2.of(currentX, y), new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
            block.setTag("ground");
            blocks.add(block);
            y += Block.SIZE;
        }
    }
}
