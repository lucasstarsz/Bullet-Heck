package io.github.lucasstarsz.bulletheck.scripts;

import io.github.lucasstarsz.fastj.math.Pointf;
import io.github.lucasstarsz.fastj.graphics.game.GameObject;

import io.github.lucasstarsz.fastj.systems.behaviors.Behavior;
import io.github.lucasstarsz.fastj.systems.input.keyboard.Keyboard;
import io.github.lucasstarsz.fastj.systems.input.keyboard.Keys;

public class PlayerController implements Behavior {

    private final float speed;
    private final float rotation;

    private float currentRotation;
    private float inputRotation;

    private Pointf inputTranslation;


    public PlayerController(float speedInterval, float rotationInterval) {
        speed = speedInterval;
        rotation = rotationInterval;
    }

    @Override
    public void init(GameObject obj) {
        inputTranslation = new Pointf();
        inputRotation = 0f;
        currentRotation = 0f;
    }

    @Override
    public void update(GameObject obj) {
        resetTransformations();
        pollMovement();
        movePlayer(obj);
    }

    private void resetTransformations() {
        inputTranslation.reset();
        inputRotation = 0f;
    }

    private void pollMovement() {
        if (Keyboard.isKeyDown(Keys.A)) {
            inputRotation -= rotation;
        } else if (Keyboard.isKeyDown(Keys.D)) {
            inputRotation += rotation;
        }
        currentRotation += inputRotation;

        if (Keyboard.isKeyDown(Keys.W)) {
            inputTranslation.y -= speed;
        } else if (Keyboard.isKeyDown(Keys.S)) {
            inputTranslation.y += speed;
        }
        inputTranslation.rotate(-currentRotation);
    }

    private void movePlayer(GameObject obj) {
        obj.rotate(inputRotation);
        obj.translate(inputTranslation);

        if (currentRotation >= 360f) {
            currentRotation -= 360f;
            obj.setRotation(currentRotation);
        } else if (currentRotation < -0f) {
            currentRotation += 360f;
            obj.setRotation(currentRotation);
        }
    }

    @Override
    public void destroy() {
        inputTranslation = null;
        inputRotation = 0f;
        currentRotation = 0f;
    }
}
