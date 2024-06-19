package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static pepse.world.trees.Flower.FLOWER_HEIGHT;
import static pepse.world.trees.Flower.FLOWER_WIDTH;

public class Fruit extends GameObject {
    public static final int FRUIT_WIDTH = FLOWER_WIDTH / 3 * 2;
    public static final int FRUIT_HEIGHT = FLOWER_HEIGHT / 3 * 2;
    public static final Color FRUIT_COLOR = new Color(189, 19, 84);
    public static final Vector2 FRUIT_SIZE = Vector2.ONES.multY(FRUIT_HEIGHT).multX(FRUIT_WIDTH);
    private Consumer<Float> addEnergy;

    public Fruit(Vector2 fruitTopLeftRight, Consumer<Float> addEnergy) {
        super(fruitTopLeftRight.subtract(Vector2.DOWN.mult(FRUIT_HEIGHT)), FRUIT_SIZE, new RectangleRenderable(FRUIT_COLOR));
        this.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        this.addEnergy = addEnergy;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.addEnergy.accept(10f);
    }
}
