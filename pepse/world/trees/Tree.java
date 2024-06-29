package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import static pepse.util.ColorSupplier.approximateColor;

public class Tree extends FloraGameObject {
    public static final int TREE_HEIGHT_BASE = Block.SIZE * 10;
    public static final Color TREE_BLOCK_COLOR = new Color(100, 50, 20);
    private List<Flower> flowers;
    private List<Fruit> fruits;
    private Consumer<Float> addEnergy;
    private GameObjectCollection gameObjects;
    private int cycleLength;

    public Tree(Vector2 groundHeight, Consumer<Float> addEnergy, GameObjectCollection gameObjects, int cycleLength) {
        Random random = new Random();
        float treeHeight = TREE_HEIGHT_BASE * random.nextFloat(0.6f, 1.2f);
        float treeWidth = Block.SIZE * random.nextFloat(1f, 1.8f);
        Vector2 treeBlockSize = Vector2.ONES.multY(treeHeight).multX(treeWidth);
        super(groundHeight.subtract(Vector2.DOWN.mult(treeHeight)), treeBlockSize, new RectangleRenderable(approximateColor(TREE_BLOCK_COLOR, 50)));
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        this.addEnergy = addEnergy;
        this.gameObjects = gameObjects;
        this.cycleLength = cycleLength;
        this.flowers = this.addFlowerAroundTreeTopInCircle(this.getTopLeftCorner());
        this.fruits = this.addFruitsAroundTreeTopInCircle(this.getTopLeftCorner());
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        new ScheduledTask(this, this.cycleLength, true, this::regenrateEatenFruits);
    }

    public List<Flower> getFlowers() {
        return flowers;
    }

    public List<Fruit> getFruits() {
        return this.fruits;
    }

    private void regenrateEatenFruits() {
        for (Fruit fruit : this.fruits) {
            if (fruit.isEaten()) {
                fruit.setEaten(false);
                gameObjects.addGameObject(fruit, Layer.DEFAULT);
            }
        }
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
            fruits.add(new Fruit(fruitTopLeft, addEnergy, gameObjects));
        }
        return fruits;
    }

    public List<FloraGameObject> getAllTreeElements() {
        List<FloraGameObject> floraGameObjects = new LinkedList<>();
        floraGameObjects.addAll(flowers);
        floraGameObjects.addAll(fruits);
        floraGameObjects.add(this);
        return floraGameObjects;
    }

    @Override
    public Runnable onJump() {
        return () -> {
            this.renderer().setRenderable(new RectangleRenderable((approximateColor(TREE_BLOCK_COLOR, 50))));
        };
    }
}
