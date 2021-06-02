package io.github.lucasstarsz.bulletheck.scenes;

import tech.fastj.engine.FastJEngine;
import tech.fastj.math.Pointf;
import tech.fastj.graphics.Display;
import tech.fastj.graphics.game.Text2D;

import tech.fastj.systems.control.Scene;
import tech.fastj.systems.control.SceneManager;

import java.awt.Color;
import java.awt.Font;

import io.github.lucasstarsz.bulletheck.util.SceneNames;

public class LoseScene extends Scene {

    private Text2D loseText;
    private Text2D deathInfo;

    public LoseScene() {
        super(SceneNames.LoseSceneName);
    }

    @Override
    public void load(Display display) {
        GameScene gameScene = FastJEngine.<SceneManager>getLogicManager().getScene(SceneNames.GameSceneName);
        int waveNumber = gameScene.getWaveNumber();

        loseText = new Text2D("You Lost...", new Pointf(300f, 375f))
                .setFont(new Font("Consolas", Font.PLAIN, 96))
                .setColor(Color.red);
        deathInfo = new Text2D("You died on wave: " + waveNumber, new Pointf(500f, 400f))
                .setFont(new Font("Consolas", Font.PLAIN, 16));

        loseText.addAsGameObject(this);
        deathInfo.addAsGameObject(this);
    }

    @Override
    public void unload(Display display) {
        loseText.destroy(this);
        deathInfo.destroy(this);
    }

    @Override
    public void update(Display display) {

    }
}
