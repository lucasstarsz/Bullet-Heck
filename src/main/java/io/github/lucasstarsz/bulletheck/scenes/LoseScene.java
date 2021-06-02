package io.github.lucasstarsz.bulletheck.scenes;

import tech.fastj.engine.FastJEngine;
import tech.fastj.math.Pointf;
import tech.fastj.graphics.Boundary;
import tech.fastj.graphics.Display;
import tech.fastj.graphics.game.Text2D;
import tech.fastj.graphics.ui.elements.Button;
import tech.fastj.graphics.util.gradients.Gradients;

import tech.fastj.systems.control.Scene;
import tech.fastj.systems.control.SceneManager;

import java.awt.Color;
import java.awt.Font;

import io.github.lucasstarsz.bulletheck.util.SceneNames;

public class LoseScene extends Scene {

    private Text2D loseText;
    private Text2D deathInfo;
    private Button playAgainBtn;

    public LoseScene() {
        super(SceneNames.LoseSceneName);
    }

    @Override
    public void load(Display display) {
        GameScene gameScene = FastJEngine.<SceneManager>getLogicManager().getScene(SceneNames.GameSceneName);
        int waveNumber = gameScene.getWaveNumber();

        loseText = new Text2D("You Lost...", new Pointf(150f, 187.5f))
                .setFont(new Font("Consolas", Font.PLAIN, 48))
                .setColor(Color.red);
        deathInfo = new Text2D("You died on wave: " + waveNumber, new Pointf(250f, 205f))
                .setFont(new Font("Consolas", Font.PLAIN, 16));

        playAgainBtn = new Button(this, new Pointf(250f, 250f), new Pointf(140, 40));
        playAgainBtn.setText("Play Again?");
        playAgainBtn.setOnAction(mouseEvent -> FastJEngine.runAfterUpdate(() -> {
            SceneManager sceneManager = FastJEngine.getLogicManager();
            sceneManager.switchScenes(SceneNames.GameSceneName);
            sceneManager.getScene(SceneNames.LoseSceneName).unload(FastJEngine.getDisplay());
        }));
        playAgainBtn.setFont(deathInfo.getFont().deriveFont(Font.BOLD, 24));
        playAgainBtn.setPaint(Gradients.linearGradient()
                .position(playAgainBtn, Boundary.TopLeft, Boundary.BottomRight)
                .withColor(Color.red)
                .withColor(Color.white)
                .withColor(Color.white)
                .withColor(Color.white)
                .withColor(Color.white)
                .withColor(Color.white)
                .withColor(Color.white)
                .withColor(Color.red)
                .build()
        );

        loseText.addAsGameObject(this);
        deathInfo.addAsGameObject(this);
        playAgainBtn.addAsGUIObject(this);
    }

    @Override
    public void unload(Display display) {
        if (loseText != null) {
            loseText.destroy(this);
            loseText = null;
        }

        if (deathInfo != null) {
            deathInfo.destroy(this);
            deathInfo = null;
        }

        if (playAgainBtn != null) {
            playAgainBtn.destroy(this);
            playAgainBtn = null;
        }

        setInitialized(false);
        reset();
    }

    @Override
    public void update(Display display) {

    }
}
