package com.example.veronica.minesweeper;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.Snackbar;

import android.view.View;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import android.widget.Chronometer;

import com.example.veronica.minesweeper.view.MineSweeperView;

/**
 * Created by Veronica Child on 9/27/16.
 * Contains main activity of a Mine Sweeper game.
 * Try tiles to see how many adjacent mines there are. Flag tiles that may have mines.
 * Game is lost when a tile is incorrectly flagged or a mine is uncovered. Win the game by
 * flagging only correct tiles.
 */


public class MainActivity extends AppCompatActivity {

    private LinearLayout layoutContent;
    private MineSweeperView gameView;
    private Chronometer timer;

    public boolean inFlagMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //layout
        layoutContent = (LinearLayout) findViewById(R.id.layoutContent);

        gameView = (MineSweeperView) findViewById(R.id.gameView);

        //Toggle button: alternates between try (disabled) and flag mode (isChecked)
        ToggleButton btnToggle = (ToggleButton) findViewById(R.id.btnToggle);
        btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Flag mode: the toggle is enabled
                    inFlagMode = true;
                } else {
                    // Try mode: the toggle is disabled
                    inFlagMode = false;
                }
            }
        });

        //btnToggle.setEnabled(false);

        //Chronometer: times the user's play round
        timer = (Chronometer) findViewById(R.id.timer);
        timer.setFormat(getResources().getString(R.string.text_timer));
        timer.start();

        //Restart button: resets game view
        Button btnRestart = (Button) findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.restartGame();
                resetTimer();
            }
        });
    }

    /**
     * Calls a snackbar message
     *
     * @param message String that snackbar will display
     */
    public void showSimpleSnackbarMessage(String message) {
        Snackbar.make(layoutContent, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Returns whether the user is in a mode to place flags or try a field
     *
     * @return boolean that if true then in flag mode, else in try mode
     */
    public boolean getFlagMode() {
        return inFlagMode;
    }

    /**
     * Resets the timer for the round
     */
    private void resetTimer() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

    }

    /**
     * Pauses the timer
     */
    public void stopTimer() {
        timer.stop();
    }
}
