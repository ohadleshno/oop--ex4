package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class Avatar extends GameObject {
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    public static final String IDLE_0_PNG = "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/assets/idle_0.png";
    public static final int AVATAR_HEIGHT = 50;
    private static final int FULL_ENERGY = 100;
    private static final double STEP_ENERGY = 0.5;
    private static final double JUMP_ENERGY = 10;
    private AvatarState avatarState = AvatarState.IDLE;
    private List<Runnable> onJump = new LinkedList<>();

    private double energy = FULL_ENERGY;

    private UserInputListener inputListener;
    private ImageReader imageReader;

    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        BufferedImage image = imageReader.readImage(IDLE_0_PNG, false).getImage();
        super(pos, Vector2.ONES.mult(AVATAR_HEIGHT), new ImageRenderable(image));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.imageReader = imageReader;
    }

    public double getEnergy() {
        return energy;
    }

    public void setOnJump(List<Runnable> onJump) {
        this.onJump = onJump;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= handleMove();
        }

        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += handleMove();
        }

        transform().setVelocityX(xVel);

        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            handleJump();
        }

        if (getVelocity().y() == 0 && getVelocity().x() == 0) {
            handleIdle();
        }
    }

    public void addEnergy(double energy) {
        if (energy < FULL_ENERGY) {
            this.energy += energy;
            if (this.energy > FULL_ENERGY) {
                this.energy = FULL_ENERGY;
            }
        }
    }

    private float handleMove() {
        if (energy < STEP_ENERGY) return 0;
        energy -= STEP_ENERGY;
        if (avatarState != AvatarState.RUNNING && avatarState != AvatarState.JUMPING) {
            this.avatarState = AvatarState.RUNNING;
            setRunningAnimation();
        }
        return VELOCITY_X;
    }

    private void handleJump() {
        if (energy < JUMP_ENERGY) return;
        this.avatarState = AvatarState.JUMPING;
        setJumpingAnimation();
        onJump.forEach(Runnable::run);
        energy -= JUMP_ENERGY;
        transform().setVelocityY(VELOCITY_Y);
    }

    private void handleIdle() {
        this.avatarState = AvatarState.IDLE;
        setIdleAnimation();
        if (energy < FULL_ENERGY) {
            energy += 1;
            if (energy > FULL_ENERGY) {
                energy = FULL_ENERGY;
            }
        }
    }

    private void setIdleAnimation() {
        this.renderer().setRenderable(new AnimationRenderable(new String[]{
                "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/pepse/assets/idle/idle_0.png",
                "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/pepse/assets/idle/idle_1.png",
                "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/pepse/assets/idle/idle_2.png",
                "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/pepse/assets/idle/idle_3.png",
        }, imageReader, false, 0.1f));
    }

    private void setRunningAnimation() {
        this.renderer().setRenderable(new AnimationRenderable(new String[]{
                "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/pepse/assets/running/run_0.png",
                "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/pepse/assets/running/run_1.png",
                "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/pepse/assets/running/run_2.png",
                "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/pepse/assets/running/run_3.png",
                "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/pepse/assets/running/run_4.png",
                "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/pepse/assets/running/run_5.png",
        }, imageReader, false, 0.1f));
    }

    private void setJumpingAnimation() {
        this.renderer().setRenderable(new AnimationRenderable(new String[]{
                "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/pepse/assets/jumping/jump_0.png",
                "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/pepse/assets/jumping/jump_1.png",
                "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/pepse/assets/jumping/jump_2.png",
                "/Users/ohadleshno/Downloads/Ex4 - Supplied Material-20240617/pepse/assets/jumping/jump_3.png",
        }, imageReader, false, 0.1f));
    }

    public enum AvatarState {
        RUNNING,
        IDLE,
        JUMPING
    }
}
