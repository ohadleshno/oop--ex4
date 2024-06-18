package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.Component;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;

import java.awt.*;

public class SunHalo {

    public static GameObject create(GameObject sun) {
        Color color = new Color(255, 255, 0, 40);
        GameObject sunHalo = new GameObject(sun.getTopLeftCorner(), sun.getDimensions().mult(1.5f), new OvalRenderable(color));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag("sunHalo");
        sunHalo.addComponent(new Component() {
            @Override
            public void update(float deltaTime) {
                sunHalo.setCenter(sun.getCenter());
            }
        });
        return sunHalo;
    }
}

