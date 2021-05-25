package io.github.lucasstarsz.bulletheck;

import io.github.lucasstarsz.fastj.engine.FastJEngine;
import io.github.lucasstarsz.fastj.math.Point;
import io.github.lucasstarsz.fastj.graphics.Display;

import io.github.lucasstarsz.fastj.systems.control.LogicManager;

import java.awt.Color;

import io.github.lucasstarsz.bulletheck.scenes.GameScene;
import io.github.lucasstarsz.bulletheck.scenes.LoseScene;

public class Main extends LogicManager {

    @Override
    public void setup(Display display) {
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
        FastJEngine.setTargetFPS(30);
        FastJEngine.configureViewerResolution(new Point(800, 450));
        FastJEngine.run();
    }
}
