package com.example.veronica.minesweeper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.example.veronica.minesweeper.MainActivity;
import com.example.veronica.minesweeper.R;
import com.example.veronica.minesweeper.model.MineSweeperModel;

/**
 * Created by Veronica on 9/27/16.
 * Contains the gameboard and notifications for the Mine Sweeper game
 */

public class MineSweeperView extends View {

    private Paint paintBg;
    private Paint paintLine;
    private Paint paintCovered;
    private Paint paintFlag;
    private Paint paintMine;
    private Paint paintNumber;

    public MineSweeperView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //light grey field
        paintBg = new Paint();
        paintBg.setColor(Color.LTGRAY);

        //dark grey covered squares
        paintCovered = new Paint();
        paintCovered.setColor(Color.DKGRAY);

        //white grid lines
        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(10);

        //black mines
        paintMine = new Paint();
        paintMine.setColor(Color.BLACK);
        paintMine.setStyle(Paint.Style.FILL_AND_STROKE);
        paintMine.setStrokeWidth(10);

        //red flag
        paintFlag = new Paint();
        paintFlag.setColor(Color.RED);
        paintFlag.setStyle(Paint.Style.FILL_AND_STROKE);
        paintFlag.setStrokeWidth(10);

        //Cyan numbers
        paintNumber = new Paint();
        paintNumber.setColor(Color.CYAN);
        paintNumber.setStyle(Paint.Style.STROKE);
        paintNumber.setTextSize(95);
        paintNumber.setStrokeWidth(8);
    }

    /**
     * Constructs the view of flags, uncovered numbers, and mines
     *
     * @param canvas Canvas object that is needed to draw on
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);

        drawBoard(canvas);
        drawGameField(canvas);
    }

    /**
     * Iterates over board, drawing numbers, flags, or mines on uncovered squares
     *
     * @param canvas Object where board is drawn
     */
    private void drawGameField(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (MineSweeperModel.getInstance().getCoverContent(i, j) ==
                        MineSweeperModel.COVERED) {
                    //Covered - draws a grey square;
                    drawCoveredSquare(i, j, canvas);
                } else if (MineSweeperModel.getInstance().getCoverContent(i, j) ==
                        MineSweeperModel.FLAGGED) {
                    // draws a circle at the center of the square
                    drawFlag(i, j, canvas);
                } else if (MineSweeperModel.getInstance().getCoverContent(i, j) ==
                        MineSweeperModel.UNCOVERED) {
                    //Board has been uncovered
                    if (MineSweeperModel.getInstance().getFieldContent(i, j) ==
                            MineSweeperModel.MINE) {
                        //If mine uncovered, draws a mine
                        drawMine(i, j, canvas);
                    } else {
                        //Draws a number
                        Log.d("TAG_DRAW", "Drawing number at: " + i + ", " + j);
                        drawNumber(i, j, canvas);
                    }
                }
            }
        }
    }

    /**
     * Draws the number showing how many adjacent tiles have a mine
     *
     * @param i      the row of the square
     * @param j      the column of the square
     * @param canvas Canvas object that the number is drawn upon
     */
    private void drawNumber(int i, int j, Canvas canvas) {
        short value = MineSweeperModel.getInstance().getFieldContent(i, j);
        String num = Short.toString(value);
        float centerX = i * getWidth() / 5 + getWidth() / 13;
        float centerY = j * getHeight() / 5 + getHeight() / 7;
        canvas.drawText(num, centerX, centerY, paintNumber);
    }

    /**
     * Draws a red flag on specified covered square
     *
     * @param i      the row of the square
     * @param j      the column of the square
     * @param canvas Canvas object that the flag is drawn upon
     */
    private void drawFlag(int i, int j, Canvas canvas) {
        drawCoveredSquare(i, j, canvas);
        // X coordinate: left side of the square + half width of the square
        float centerX = i * getWidth() / 5 + getWidth() / 10;
        float centerY = j * getHeight() / 5 + getHeight() / 10;
        int radius = getHeight() / 20;

        canvas.drawCircle(centerX, centerY, radius, paintFlag);
    }

    //Generates a square based on coordinates
    private void drawCoveredSquare(int x, int y, Canvas canvas) {
        int leftX = (x * getWidth() / 5) + 10;
        int topY = (y * getHeight() / 5) + 10;
        int rightX = leftX + (getWidth() / 6) + 10;
        int bottomY = topY + (getHeight() / 6) + 10;

        canvas.drawRect(leftX, topY, rightX, bottomY, paintCovered);
    }

    /**
     * Draws a six-line mine at the specified square
     *
     * @param i      the row of the square
     * @param j      the column of the square
     * @param canvas object which the mine is drawn upon
     */
    private void drawMine(int i, int j, Canvas canvas) {
        canvas.drawLine((i * getWidth() / 5) + 40, (j * getHeight() / 5) + 40,
                ((i + 1) * getWidth() / 5) - 40,
                ((j + 1) * getHeight() / 5) - 40, paintMine);
        canvas.drawLine(((i + 1) * getWidth() / 5) - 40, (j * getHeight() / 5) + 40,
                (i * getWidth() / 5) + 40, ((j + 1) * getHeight() / 5) - 40, paintMine);
        canvas.drawLine((i * getWidth() / 5) + 35, (j * getHeight() / 5) + 98,
                ((i + 1) * getWidth() / 5) - 35,
                ((j + 1) * getHeight() / 5) - 96, paintMine);
        canvas.drawLine((i * getWidth() / 5) + 98, (j * getHeight() / 5) + 35,
                ((i + 1) * getWidth() / 5) - 96,
                ((j + 1) * getHeight() / 5) - 35, paintMine);
        float centerX = i * getWidth() / 5 + getWidth() / 10;
        float centerY = j * getHeight() / 5 + getHeight() / 10;

        canvas.drawCircle(centerX, centerY, 35, paintMine);
        canvas.drawCircle(centerX + 1, centerY, 10, paintFlag);
    }

    /**
     * Draws grid layout for MineSweeper
     *
     * @param canvas Object to display board
     */
    private void drawBoard(Canvas canvas) {
        // border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);
        // four horizontal lines
        canvas.drawLine(0, getHeight() / 5, getWidth(), getHeight() / 5,
                paintLine);
        canvas.drawLine(0, 2 * getHeight() / 5, getWidth(),
                2 * getHeight() / 5, paintLine);
        canvas.drawLine(0, 3 * getHeight() / 5, getWidth(),
                3 * getHeight() / 5, paintLine);
        canvas.drawLine(0, 4 * getHeight() / 5, getWidth(),
                4 * getHeight() / 5, paintLine);

        // four vertical lines
        canvas.drawLine(getWidth() / 5, 0, getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(2 * getWidth() / 5, 0, 2 * getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(3 * getWidth() / 5, 0, 3 * getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(4 * getWidth() / 5, 0, 4 * getWidth() / 5, getHeight(),
                paintLine);
    }

    //Handles whether or not touch has been detected
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean inFlagMode = ((MainActivity) getContext()).getFlagMode();

        // if statement prevents drag motion to create a stream of circles by focusing on the one
        // down motion
        if (event.getAction() == MotionEvent.ACTION_DOWN &&
                MineSweeperModel.getInstance().getGameState() == 0) {
            // Equation that makes it easy for the touched area to be assigned to a certain square;
            int tX = ((int) event.getX()) / (getWidth() / 5);
            int tY = ((int) event.getY()) / (getHeight() / 5);
            Log.d(getContext().getString(R.string.tag_touch),
                    getContext().getString(R.string.log_touched, tX, tY));

            if (tX < 5 && tY < 5 && inFlagMode) {
                //Flag mode on:
                if (MineSweeperModel.getInstance().getCoverContent(tX, tY) ==
                        MineSweeperModel.COVERED) {
                    //Places a flag on a covered square
                    Log.d(getContext().getString(R.string.tag_touch),
                            getContext().getString(R.string.log_flagged, tX, tY));
                    MineSweeperModel.getInstance().setCoverContent(tX, tY,
                            MineSweeperModel.FLAGGED);
                }
                invalidate();
            } else if (tX < 5 && tY < 5 && !inFlagMode) {
                //If flagged mode off, in try mode:
                if (MineSweeperModel.getInstance().getCoverContent(tX, tY) ==
                        MineSweeperModel.COVERED) {
                    //Sets touch to current player's mark
                    MineSweeperModel.getInstance().setCoverContent(tX, tY,
                            MineSweeperModel.UNCOVERED);
                    //redraws the View
                    invalidate();
                }
            }
            showEndGame();
            invalidate();
        }
        return true;
    }

    /**
     * Shows endgame message upon loss or win
     */
    private void showEndGame() {
        MineSweeperModel.getInstance().checkGameState();
        if (MineSweeperModel.getInstance().getGameState() == MineSweeperModel.WIN) {
            showWinnerMessage();
            ((MainActivity) getContext()).stopTimer();
        } else if (MineSweeperModel.getInstance().getGameState() == MineSweeperModel.mineLoss) {
            showCoveredMines();
            showMineLossMessage();
            ((MainActivity) getContext()).stopTimer();
        } else if (MineSweeperModel.getInstance().getGameState() == MineSweeperModel.flagLoss) {
            showCoveredMines();
            showFlagLossMessage();
            ((MainActivity) getContext()).stopTimer();
        }
    }

    /**
     * Change original size of the Game View so that it's always a square
     **/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }

    /**
     * Resets board view
     */
    public void restartGame() {
        MineSweeperModel.getInstance().resetModel();
        invalidate();
    }

    /**
     * Shows a snackbar message upon winning the game
     */
    private void showWinnerMessage() {
        ((MainActivity) getContext()).showSimpleSnackbarMessage(
                getResources().getString(R.string.text_win));
    }

    /**
     * Shows a snackbar message upon losing the game
     */
    private void showMineLossMessage() {
        ((MainActivity) getContext()).showSimpleSnackbarMessage(
                getResources().getString(R.string.text_mine_loss));
    }

    /**
     * Shows a snackbar message upon losing the game
     */
    private void showFlagLossMessage() {
        ((MainActivity) getContext()).showSimpleSnackbarMessage(
                getResources().getString(R.string.text_flag_loss));
    }

    /**
     * Reveals all covered mine locations
     */
    private void showCoveredMines() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (MineSweeperModel.getInstance().getFieldContent(i, j) ==
                        MineSweeperModel.MINE &&
                        MineSweeperModel.getInstance().getCoverContent(i, j) ==
                                MineSweeperModel.COVERED) {
                    MineSweeperModel.getInstance().setCoverContent(i, j, MineSweeperModel.UNCOVERED);
                }
            }
        }
    }
}
