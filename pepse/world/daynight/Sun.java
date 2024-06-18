package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {
    public static GameObject create(Vector2 windowDimensions,
                                    float cycleLength) {

        //todo not sure about the location of the sun
        GameObject sun = new GameObject(
                Vector2.of(windowDimensions.x() / 2, windowDimensions.y() / 9), Vector2.of(windowDimensions.x() / 10, windowDimensions.x() / 10),
                new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("sun");
        Vector2 initialSunCenter = sun.getCenter();
        // todo based on the ground level not sure here
        Vector2 cycleCenter = Vector2.of(windowDimensions.x() / 2, windowDimensions.y() * 2 / 3);
        new Transition<Float>(sun, (Float angle) -> sun.setCenter(initialSunCenter.subtract(cycleCenter)
                .rotated(angle)
                .add(cycleCenter)), 0f, 360f, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength, Transition.TransitionType.TRANSITION_LOOP,null);
        return sun;

    }
}
