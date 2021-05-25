package io.github.lucasstarsz.bulletheck.scripts;

import io.github.lucasstarsz.fastj.engine.FastJEngine;
import io.github.lucasstarsz.fastj.math.Pointf;
import io.github.lucasstarsz.fastj.graphics.Drawable;
import io.github.lucasstarsz.fastj.graphics.game.GameObject;
import io.github.lucasstarsz.fastj.graphics.game.Model2D;
import io.github.lucasstarsz.fastj.graphics.game.Polygon2D;

import io.github.lucasstarsz.fastj.systems.behaviors.Behavior;
import io.github.lucasstarsz.fastj.systems.tags.TagManager;

import java.util.List;

import io.github.lucasstarsz.bulletheck.scenes.GameScene;
import io.github.lucasstarsz.bulletheck.util.Tags;

public class EnemyMovement implements Behavior {

    private final GameScene gameScene;

    private Model2D player;
    private Polygon2D playerHealthBar;

    public EnemyMovement(GameScene gameScene) {
        this.gameScene = gameScene;
    }

    @Override
    public void init(GameObject obj) {
        player = (Model2D) TagManager.getAllWithTag(Tags.Player).get(0);
        playerHealthBar = (Polygon2D) TagManager.getAllWithTag(Tags.PlayerHealthBar).get(0);
    }

    @Override
    public void update(GameObject obj) {
        checkCollisions(obj);
        moveToPlayer(obj);
    }

    private void checkCollisions(GameObject obj) {
        if (obj.collidesWith(player)) {
            ((PlayerHealthBar) playerHealthBar.getBehaviors().get(0)).takeDamage();
        }

        List<Drawable> bullets = TagManager.getAllWithTag(Tags.Bullet);
        for (Drawable bullet : bullets) {
            if (obj.collidesWith(bullet)) {
                BulletMovement bulletMovementScript = (BulletMovement) ((GameObject) bullet).getBehaviors().get(0);
                bulletMovementScript.bulletDied((GameObject) bullet);
                enemyDied(obj);
            }
        }
    }

    private void enemyDied(GameObject enemy) {
        FastJEngine.runAfterUpdate(() -> gameScene.enemyDied(enemy));
    }

    private void moveToPlayer(GameObject obj) {
        Pointf travelDistance = Pointf.subtract(player.getCenter(), obj.getCenter())
                .normalized()
                .multiply(2f);
        obj.translate(travelDistance);
    }
}
