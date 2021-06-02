package io.github.lucasstarsz.bulletheck.scenes;

import tech.fastj.engine.FastJEngine;
import tech.fastj.math.Maths;
import tech.fastj.math.Pointf;
import tech.fastj.graphics.Display;
import tech.fastj.graphics.game.GameObject;
import tech.fastj.graphics.game.Model2D;
import tech.fastj.graphics.game.Polygon2D;
import tech.fastj.graphics.game.Text2D;
import tech.fastj.graphics.util.DrawUtil;
import tech.fastj.graphics.util.PsdfUtil;

import tech.fastj.systems.audio.Audio;
import tech.fastj.systems.audio.AudioManager;
import tech.fastj.systems.control.Scene;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import io.github.lucasstarsz.bulletheck.scripts.EnemyMovement;
import io.github.lucasstarsz.bulletheck.scripts.PlayerCannon;
import io.github.lucasstarsz.bulletheck.scripts.PlayerController;
import io.github.lucasstarsz.bulletheck.scripts.PlayerHealthBar;
import io.github.lucasstarsz.bulletheck.util.FilePaths;
import io.github.lucasstarsz.bulletheck.util.SceneNames;
import io.github.lucasstarsz.bulletheck.util.Tags;

public class GameScene extends Scene {

    private Model2D player;
    private Text2D playerMetadata;
    private Polygon2D playerHealthBar;

    private List<Model2D> enemies;
    private int enemyCount = 0;
    private int wave = 0;

    public GameScene() {
        super(SceneNames.GameSceneName);
    }

    @Override
    public void load(Display display) {
        playerMetadata = createPlayerMetaData();
        playerHealthBar = createPlayerHealthBar();
        PlayerHealthBar playerHealthBarScript = new PlayerHealthBar(playerMetadata, this);
        playerHealthBar.addBehavior(playerHealthBarScript, this)
                .<GameObject>addTag(Tags.PlayerHealthBar, this);


        PlayerController playerControllerScript = new PlayerController(5f, 3f);
        PlayerCannon playerCannonScript = new PlayerCannon(this);
        player = createPlayer();
        player.addBehavior(playerControllerScript, this)
                .addBehavior(playerCannonScript, this)
                .<GameObject>addTag(Tags.Player, this);


        // add game objects to the screen in order!
        player.addAsGameObject(this);
        playerHealthBar.addAsGameObject(this);
        playerMetadata.addAsGameObject(this);

        // scale all content down by 50%
        player.scale(new Pointf(-0.5f), Pointf.Origin);


        enemies = new ArrayList<>();
        wave = 0;
        newWave();

        Audio backgroundMusic = AudioManager.getAudio(FilePaths.BackgroundMusicAudioPath);
        backgroundMusic.setLoopCount(Audio.ContinuousLoop);
        backgroundMusic.setLoopPoints(Audio.LoopFromStart, Audio.LoopAtEnd);
        backgroundMusic.setPlaybackPosition(0L);
        switch (backgroundMusic.getCurrentPlaybackState()) {
            case Paused:
                backgroundMusic.resume();
                break;
            case Stopped:
                backgroundMusic.play();
                break;
        }
    }

    @Override
    public void unload(Display display) {
        if (player != null) {
            player.destroy(this);
            player = null;
        }

        if (playerMetadata != null) {
            playerMetadata.destroy(this);
            playerMetadata = null;
        }

        if (playerHealthBar != null) {
            playerHealthBar.destroy(this);
            playerHealthBar = null;
        }

        if (enemies != null) {
            enemies.forEach(enemy -> enemy.destroy(this));
            enemies.clear();
            enemies = null;
        }

        enemyCount = 0;

        AudioManager.getAudio(FilePaths.BackgroundMusicAudioPath).pause();
        setInitialized(false);
        reset();
    }

    @Override
    public void update(Display display) {
    }

    public void enemyDied(GameObject enemy) {
        if (enemies.remove((Model2D) enemy)) {
            enemy.destroy(this);
            enemyCount--;

            if (enemyCount == 0) {
                newWave();
            }
        }
    }

    public int getWaveNumber() {
        return wave;
    }

    private Text2D createPlayerMetaData() {
        return new Text2D("Health: 100", new Pointf(27.5f, 55f))
                .setFont(new Font("Consolas", Font.BOLD, 16));
    }

    private Polygon2D createPlayerHealthBar() {
        Pointf playerHealthBarMeshLocation = new Pointf(25f, 40f);
        Pointf playerHealthBarMeshSize = new Pointf(100f, 20f);
        Pointf[] playerHealthBarMesh = DrawUtil.createBox(playerHealthBarMeshLocation, playerHealthBarMeshSize);

        return new Polygon2D(playerHealthBarMesh, Color.green, true, true);
    }

    private Model2D createPlayer() {
        return new Model2D(PsdfUtil.loadPsdf(FilePaths.PathToResources + "player.psdf"));
    }

    private void newWave() {
        wave++;
        enemyCount = calculateEnemyCount(wave);
        for (int i = 0; i < enemyCount; i++) {
            Model2D enemy = createEnemy();
            enemy.initBehaviors();
            enemy.scale(new Pointf(-0.5f), Pointf.Origin);
            enemies.add(enemy);
        }
    }

    private int calculateEnemyCount(int wave) {
        return (int) Math.max(wave * (Math.sqrt(wave) / 2), 3);
    }

    private Model2D createEnemy() {
        Pointf randomPosition = new Pointf(
                Maths.random(-500f, 1780f),
                Maths.randomAtEdge(-500f, 1220f)
        );

        return (Model2D) new Model2D(PsdfUtil.loadPsdf(FilePaths.PathToResources + "enemy.psdf"))
                .setTranslation(randomPosition)
                .addBehavior(new EnemyMovement(this), this)
                .addAsGameObject(this);
    }
}
