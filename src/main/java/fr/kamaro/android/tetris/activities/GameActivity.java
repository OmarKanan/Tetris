package fr.kamaro.android.tetris.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import fr.kamaro.android.tetris.R;
import fr.kamaro.android.tetris.engine.GameEngine;
import fr.kamaro.android.tetris.gui.DropListener;
import fr.kamaro.android.tetris.gui.GameBackgroundView;
import fr.kamaro.android.tetris.gui.GameSurfaceView;
import fr.kamaro.android.tetris.gui.LeftRightListener;
import fr.kamaro.android.tetris.gui.NextPieceView;
import fr.kamaro.android.tetris.gui.RotationListener;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_layout);

        GameSurfaceView surfaceView = (GameSurfaceView) findViewById(R.id.game_surface_view);
        GameBackgroundView background = (GameBackgroundView) findViewById(R.id.game_background_view);
        NextPieceView next = (NextPieceView) findViewById(R.id.next_piece_view);
        ImageView leftButton = (ImageView) findViewById(R.id.move_left_imageview);
        ImageView rightButton = (ImageView) findViewById(R.id.move_right_imageview);
        ImageView rotateButton = (ImageView) findViewById(R.id.rotate_imageview);
        ImageView dropButton = (ImageView) findViewById(R.id.drop_imageview);

        GameEngine engine = new GameEngine(surfaceView.drawThread, background, next, 1, this);
        View.OnTouchListener leftRight = new LeftRightListener(engine, leftButton, rightButton, this);
        View.OnTouchListener rotation = new RotationListener(engine);
        View.OnTouchListener drop = new DropListener(engine);

        new Thread(engine).start();

        leftButton.setOnTouchListener(leftRight);
        rightButton.setOnTouchListener(leftRight);
        rotateButton.setOnTouchListener(rotation);
        dropButton.setOnTouchListener(drop);

    }

}
