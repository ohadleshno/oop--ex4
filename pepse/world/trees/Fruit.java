package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Avatar;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;

import static pepse.world.trees.Flower.FLOWER_HEIGHT;
import static pepse.world.trees.Flower.FLOWER_WIDTH;

public class Fruit extends FloraGameObject {
    public static final int FRUIT_WIDTH = FLOWER_WIDTH / 4 * 3;
    public static final int FRUIT_HEIGHT = FLOWER_HEIGHT / 4 * 3;
    public static final Color FRUIT_COLOR = new Color(107, 19, 189);
    public static final Vector2 FRUIT_SIZE = Vector2.ONES.multY(FRUIT_HEIGHT).multX(FRUIT_WIDTH);
    private Consumer<Float> addEnergy;
    private GameObjectCollection gameObjects;
    private boolean isEaten = false;

    public Fruit(Vector2 fruitTopLeftRight, Consumer<Float> addEnergy, GameObjectCollection gameObjects) {
        super(fruitTopLeftRight.subtract(Vector2.DOWN.mult(FRUIT_HEIGHT)), FRUIT_SIZE, new OvalRenderable(FRUIT_COLOR));
        this.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        this.addEnergy = addEnergy;
        this.gameObjects = gameObjects;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.addEnergy.accept(10f);
        if (other.getClass() == Avatar.class) {
            this.isEaten = true;
            this.gameObjects.removeGameObject(this);
        }
    }

    @Override
    public Runnable onJump() {
        Random random = new Random();
        return () -> {
            this.renderer().setRenderable(new OvalRenderable(new Color(random.nextInt(0, 256), random.nextInt(0, 256), random.nextInt(0, 256))));
        };
    }
}
