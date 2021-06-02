package io.github.lucasstarsz.bulletheck;

import tech.fastj.engine.FastJEngine;
import tech.fastj.math.Point;
import tech.fastj.graphics.Display;

import tech.fastj.systems.control.SceneManager;

import java.awt.Color;

import io.github.lucasstarsz.bulletheck.scenes.GameScene;
import io.github.lucasstarsz.bulletheck.scenes.LoseScene;

public class Main extends SceneManager {

    @Override
    public void init(Display display) {
        GameScene gameScene = new GameScene();
        this.addScene(gameScene);
        this.setCurrentScene(gameScene);
        this.loadCurrentScene();

        LoseScene loseScene = new LoseScene();
        this.addScene(loseScene);

        display.setBackgroundColor(Color.lightGray);
        display.showFPSInTitle(true);
        display.getJFrame().setResizable(false);
    }

    public static void main(String[] args) {
        FastJEngine.init("Simple Bullet Hell", new Main());
        FastJEngine.setTargetFPS(15);
        FastJEngine.configureViewerResolution(new Point(640, 360));
        FastJEngine.run();
    }
}
