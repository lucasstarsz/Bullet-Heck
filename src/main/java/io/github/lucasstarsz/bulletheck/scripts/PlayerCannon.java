package io.github.lucasstarsz.bulletheck.scripts;

import tech.fastj.engine.FastJEngine;
import tech.fastj.math.Pointf;
import tech.fastj.graphics.Drawable;
import tech.fastj.graphics.game.GameObject;
import tech.fastj.graphics.game.Polygon2D;
import tech.fastj.graphics.util.DrawUtil;

import tech.fastj.systems.behaviors.Behavior;
import tech.fastj.systems.input.keyboard.Keyboard;
import tech.fastj.systems.input.keyboard.Keys;
import tech.fastj.systems.tags.TagManager;

import java.awt.Color;
import java.util.Objects;

import io.github.lucasstarsz.bulletheck.scenes.GameScene;
import io.github.lucasstarsz.bulletheck.util.Tags;

public class PlayerCannon implements Behavior {

    private static final Pointf BulletSize = new Pointf(5f);
    private static final int MaxBulletCount = 4;

    private final GameScene gameScene;
    private int bulletCount;

    public PlayerCannon(GameScene scene) {
        gameScene = Objects.requireNonNull(scene);
    }

    @Override
    public void init(GameObject obj) {
        bulletCount = 0;
    }

    @Override
    public void update(GameObject obj) {
        if (Keyboard.isKeyRecentlyPressed(Keys.Space) && bulletCount < MaxBulletCount) {
            FastJEngine.runAfterUpdate(() -> createBullet(obj));
        }
    }

    private void createBullet(GameObject player) {
        Pointf startingPoint = Pointf.add(player.getCenter(), new Pointf(0f, -50f));
        float rotationAngle = 360f - Math.abs(player.getRotation());
        Pointf rotationPoint = player.getCenter();

        BulletMovement bulletMovementScript = new BulletMovement(gameScene, player.getRotation(), this);
        Pointf cannonFront = Pointf.rotate(startingPoint, rotationAngle, rotationPoint);
        Pointf[] bulletMesh = DrawUtil.createBox(cannonFront, BulletSize);

        Polygon2D bullet = (Polygon2D) new Polygon2D(bulletMesh, Color.red, true, true)
                .addBehavior(bulletMovementScript, gameScene)
                .<GameObject>addTag(Tags.Bullet, gameScene)
                .addAsGameObject(gameScene);
        bullet.initBehaviors();

        bulletCount++;
    }

    void bulletDied() {
        bulletCount--;
    }

    @Override
    public void destroy() {
        for (Drawable bullet : TagManager.getAllWithTag(Tags.Bullet)) {
            bullet.destroy(gameScene);
        }

        bulletCount = 0;
    }
}
