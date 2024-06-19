package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.util.BlockUtil;
import pepse.world.Block;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

public class Flora {
    public Function<Float, Float> groundHeightAtX;
    private Consumer<Float> addEnergy;

    public Flora(Function<Float, Float> groundHeightAtX, Consumer<Float> addEnergy) {
        this.groundHeightAtX = groundHeightAtX;
        this.addEnergy = addEnergy;
    }

    public List<Tree> createInRange(int minX, int maxX) {
        LinkedList<Tree> trees = new LinkedList<>();
        int finalMinX = BlockUtil.getNearestBlockLocation(minX);
        int finalMaxX = BlockUtil.getNearestBlockLocation(maxX);
        int currentX = finalMinX;

        while (currentX < finalMaxX) {
            float groundHeightAtX = this.groundHeightAtX.apply((float) currentX);
            maybeAddTree(Vector2.of(currentX, groundHeightAtX), trees);
            currentX += Block.SIZE;
        }

        return trees;
    }

    private void maybeAddTree(Vector2 groundHeight, LinkedList<Tree> trees) {
        if (new Random().nextDouble(0, 1) < 0.1) {
            trees.add(new Tree(groundHeight, addEnergy));
        }
    }
}
