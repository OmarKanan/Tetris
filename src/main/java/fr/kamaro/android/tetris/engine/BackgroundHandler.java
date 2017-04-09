package fr.kamaro.android.tetris.engine;

import android.os.Handler;
import android.os.Message;

import fr.kamaro.android.tetris.gui.GameBackgroundView;

public class BackgroundHandler extends Handler {

    private GameBackgroundView gameBackgroundView;

    public BackgroundHandler(GameBackgroundView gameBackgroundView) {
        this.gameBackgroundView = gameBackgroundView;
    }

    @Override
    public void handleMessage(Message msg) {
        gameBackgroundView.invalidate();
    }

}
