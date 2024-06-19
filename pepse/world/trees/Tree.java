package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class Tree extends GameObject {
    public static final int TREE_HEIGHT = Block.SIZE * 10;
    public static final Color TREE_BLOCK_COLOR = new Color(100, 50, 20);
    public static final Vector2 Tree_Block_Size = Vector2.ONES.multY(TREE_HEIGHT).multX(Block.SIZE);
    private List<Flower> flowers;
    private List<Fruit> fruits;
    private Consumer<Float> addEnergy;

    public Tree(Vector2 groundHeight, Consumer<Float> addEnergy) {
        super(groundHeight.subtract(Vector2.DOWN.mult(TREE_HEIGHT)), Tree_Block_Size, new RectangleRenderable(TREE_BLOCK_COLOR));
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        this.addEnergy = addEnergy;
        this.flowers = this.addFlowerAroundTreeTopInCircle(this.getTopLeftCorner());
        this.fruits = this.addFruitsAroundTreeTopInCircle(this.getTopLeftCorner());
    }

    public List<Flower> getFlowers() {
        return flowers;
    }

    public List<Fruit> getFruits() {
        return this.fruits;
    }

    private List<Flower> addFlowerAroundTreeTopInCircle(Vector2 treeCenter) {
        List<Flower> flowers = new LinkedList<>();
        int numFlowers = new Random().nextInt(20, 80);
        for (int i = 0; i < numFlowers; i++) {
            Vector2 topLeft = treeCenter.add(Vector2.ONES.mult(Block.SIZE).multX(new Random().nextInt(-5, 5)).multY(new Random().nextInt(-5, 5)));
            flowers.add(new Flower(topLeft));
        }
        return flowers;
    }

    private List<Fruit> addFruitsAroundTreeTopInCircle(Vector2 treeCenter) {
        List<Fruit> fruits = new LinkedList<>();
        int numFlowers = new Random().nextInt(0, 10);
        for (int i = 0; i < numFlowers; i++) {
            Vector2 fruitTopLeft = treeCenter.add(Vector2.ONES.mult(Block.SIZE).multX(new Random().nextInt(-5, 5)).multY(new Random().nextInt(-5, 5)));
            fruits.add(new Fruit(fruitTopLeft, addEnergy));
        }
        return fruits;
    }

}
