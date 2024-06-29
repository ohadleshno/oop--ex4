package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.*;

import java.util.LinkedList;
import java.util.List;

import static pepse.world.Avatar.AVATAR_HEIGHT;

public class PepseGameManager extends GameManager {

    public static final int skyLayer = Layer.BACKGROUND;

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        GameObjectCollection gameObjects = gameObjects();
        gameObjects.addGameObject(Sky.create(windowDimensions), skyLayer);
        Terrain terrain = new Terrain(windowDimensions, 0);
        float groundHeightAt0 = terrain.groundHeightAt(0f);
        List<Block> blocks = terrain.createInRange(0, (int) windowDimensions.x());
        blocks.forEach(block -> {
            gameObjects.addGameObject(block, Layer.STATIC_OBJECTS);
        });
        int cycleLength = 30;
        GameObject sun = Sun.create(windowDimensions, cycleLength);
        gameObjects.addGameObject(sun, skyLayer);
        gameObjects.addGameObject(Night.create(windowDimensions, (float) cycleLength / 2), Layer.BACKGROUND);
        gameObjects.addGameObject(SunHalo.create(sun), Layer.BACKGROUND);
        Avatar avatar = new Avatar(Vector2.of(0, groundHeightAt0 - AVATAR_HEIGHT * 2), inputListener, imageReader);
        Energy energy = new Energy(avatar::getEnergy);
        gameObjects.addGameObject(energy, Layer.FOREGROUND);
        gameObjects.addGameObject(avatar, Layer.DEFAULT);
        List<Tree> trees = new Flora(terrain::groundHeightAt, avatar::addEnergy, gameObjects, cycleLength).createInRange(0, (int) windowDimensions.x());
        List<FloraGameObject> floraGameObjects = new LinkedList<>();
        trees.forEach(tree -> {
            floraGameObjects.addAll(tree.getAllTreeElements());
            gameObjects.addGameObject(tree, Layer.DEFAULT);
            for (Flower flower : tree.getFlowers()) {
                gameObjects.addGameObject(flower, Layer.STATIC_OBJECTS);
            }
            for (Fruit fruit : tree.getFruits()) {
                gameObjects.addGameObject(fruit, Layer.DEFAULT);
            }
        });
        avatar.setOnJump(floraGameObjects.stream().map(FloraGameObject::onJump).toList());
    }
}
