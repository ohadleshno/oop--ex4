package pepse.world.trees;

import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;

public class Flower extends FloraGameObject {
    public static final int FLOWER_HEIGHT = 25;
    public static final int FLOWER_WIDTH = 25;
    public static final Color FLOWER_COLOR = new Color(37, 189, 19);
    public static final Vector2 FLOWER_SIZE = Vector2.ONES.multY(FLOWER_HEIGHT).multX(FLOWER_WIDTH);
    private static final int cycleLength = 2;

    public Flower(Vector2 flowerTopLeftRight) {
        super(flowerTopLeftRight.subtract(Vector2.DOWN.mult(FLOWER_HEIGHT)), FLOWER_SIZE, new RectangleRenderable(FLOWER_COLOR));
        this.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        new ScheduledTask(this, new Random().nextFloat(0, 0.5f), false, () -> {
            new Transition<Float>(this, this.renderer()::setRenderableAngle, 0f, 5f, Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength, Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
            new Transition<Vector2>(this, this::setDimensions, FLOWER_SIZE, FLOWER_SIZE.mult(1.1f), Transition.CUBIC_INTERPOLATOR_VECTOR, cycleLength, Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        });
    }

    @Override
    public Runnable onJump() {
        return () -> {
            new Transition<Float>(this, this.renderer()::setRenderableAngle, 0f, 90f, Transition.CUBIC_INTERPOLATOR_FLOAT, 1, Transition.TransitionType.TRANSITION_ONCE, null);
        };
    }
}
