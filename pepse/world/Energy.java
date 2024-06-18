package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.util.function.Supplier;

public class Energy extends GameObject {
    public static final int SIZE = 30;
    public Supplier<Double> energyCallback;

    public Energy(Supplier<Double> energyCallback) {
        TextRenderable readable = new TextRenderable("Energy: " + energyCallback.get());
        super(Vector2.ONES.multX(30).multY(20), Vector2.ONES.mult(SIZE), readable);
        this.energyCallback = energyCallback;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        TextRenderable renderable = (TextRenderable) this.renderer().getRenderable();
        renderable.setString("Energy: " + energyCallback.get());
    }
}
